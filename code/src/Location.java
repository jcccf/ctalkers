import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;



public class Location {

	public String name;
	public String city;
	public String country;

	public double longitude;
	public double latitude;
	public int populationTotal;
	public int areaTotal;
	public static int fieldCount = 10;

	public Location(String n) {
		name = Utils.cleanLocation(n);
	}

	public Location(float lon, float lat, String ct, String ctr) {
		longitude = lon;
		latitude = lat;
		city = ct;
		country = ctr;
		setName();
	}

	public void addLongAndLat(String[] f) {	
		double[] ls = new double[8];
		for (int i = 0; i < 8; i++) {
			if(i != 2 && i != 5)
				ls[i] = Float.parseFloat(f[i]);
		}

		populationTotal = Integer.parseInt(f[8]);
		areaTotal = Integer.parseInt(f[9]);

		int ns = (f[2].equals("N")) ? 1 : -1;
		int ew = (f[5].equals("E")) ? 1 : -1;
		latitude = (ls[0] + ls[1] / 60 + ls[3]/3600) * ns;
		longitude = (ls[4] + ls[6] / 60 + ls[7]/3600) * ew;
	}

	public boolean setName() {
		if (!(city.equals(""))) 
			name = city;
		else
			if (!(country.equals("")))
				name = country;
			else
				return false;
		return true;
	}

	public String toString() {
		return "{" +
		name + ", " +
		latitude + ", " +
		longitude + ", " +
		populationTotal + "," +
		areaTotal + "}";
	}


	// null if dbpedia lookup returns no city or country
	public static Location createLocation(String locName) {

		// Search DBpedia for location information

		// name spaces
		String dbpResource = "http://dbpedia.org/resource/";
		String dbpProp = "http://dbpedia.org/property/";
		String dbpOwl = "http://dbpedia.org/ontology/";
		String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String rdfs = "http://www.w3.org/2000/01/rdf-schema#";

		String locURI = dbpResource + locName;

		// try extracting city
		Location loc = null;
		try {

			/*
SELECT ?cityName ?countryname
WHERE {
<http://dbpedia.org/resource/San_Francisco>
<http://www.w3.org/2000/01/rdf-schema#label> ?cityName;
<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/City>;
<http://dbpedia.org/ontology/country> ?country.
?country <http://www.w3.org/2000/01/rdf-schema#label> ?countryname.
FILTER( lang(?countryname) = 'en' && lang(?cityName) = 'en')
}
			 */


			String queryString = 
				"SELECT ?cityName ?countryName WHERE { \n" +
				
				"<" + locURI + ">\n" +
				"<" + rdfs + "label> ?cityName;\n" +
				"<" + rdf + "type> <" + dbpOwl + "City>;\n" +
				"<" + dbpOwl + "country> ?country.\n" +
				"?country <" + rdfs + "label> ?countryName.\n" +
				"FILTER( lang(?countryName) = 'en' && lang(?cityName) = 'en')}";

			System.out.println(queryString);
			
			QuerySolution res = doSparql(queryString);

			String cityAndCountry = res.get("cityName").toString();
			String countryName = res.get("countryName").toString();

			// try to remove country from the name
			int commaLoc = cityAndCountry.indexOf(",");
			String cityName = cityAndCountry;
			if(commaLoc > 0) {
				cityName = cityAndCountry.substring(0, cityAndCountry.indexOf(","));
			}			

			loc = new Location(cityName);
			loc.city = cityName;
			loc.country = countryName;
			
			System.out.println("city success");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("no city");
			
			// try extracting country
			try {

				String queryString = "SELECT ?countryName WHERE { <"+locURI+"> <"+rdfs+"label> ?countryName; <" + rdf + "type> <" + dbpOwl + "Country>. "
				+ "FILTER langMatches(lang(?res), 'en')}";

				QuerySolution res = doSparql(queryString);

				String countryName = res.get("countryName").toString();

				loc = new Location(countryName);
				loc.country = countryName;
				System.out.println("country success");

			} catch(Exception f) {
				//f.printStackTrace();
				System.out.println("no country");
			}
			
			
		}


		// try adding longitude, latitude, total pop, and total area
		try {
			// Extract the following triples from each location
			String [] props = {"latd", "latm", "latns", "lats", "longd", "longew", "longm", "longs"};
			String [] owlProps = {"populationTotal", "areaTotal"};

			// Create the prop query string
			String queryString = "SELECT ?" + Utils.join(props, " ?") + " ?" + Utils.join(owlProps, " ?") + "\nWHERE {\n";
			for(String prop: props) {
				queryString += "<" + locURI + "> <" + dbpProp + prop + "> ?" + prop + ".\n";
			}
			for(String owlProp:owlProps) {
				queryString += "<" + locURI + "> <" + dbpOwl + owlProp + "> ?" + owlProp + ".\n";
			}
			queryString += "}";


			QuerySolution res = doSparql(queryString);

			String[] locFields = new String[Location.fieldCount];
			int k=0;
			for(String prop: props) {
				locFields[k++] = res.get(prop).toString();
			}
			for(String owlProp: owlProps) {
				locFields[k++] = res.get(owlProp).toString();
			}

			loc.addLongAndLat(locFields);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return loc;
	}

	public static QuerySolution doSparql(String queryString) {
		String dbpSparql = "http://dbpedia.org/sparql";

		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.sparqlService(dbpSparql, query);
		ResultSet results = qe.execSelect();

		QuerySolution firstResult = results.next();
		qe.close();

		return firstResult;
	}

	public static void main(String[] args) {

		System.out.println(createLocation("San_Francisco")); 

	}

}

