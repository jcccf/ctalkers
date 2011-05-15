import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;



public class Location {

	public String name;
	
	public double longitude;
	public double latitude;
	public int populationTotal;
	public int areaTotal;
	public static int fieldCount = 10;
	
	public Location(String n) {
		name = n;
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
		
		String dbpSparql = "http://dbpedia.org/sparql";
		
		// try extracting country
		Location loc;
		try {
			
			
			
			
			
			
			loc = new Location(locName);
			//loc.c
		} catch(Exception e) {}
		
		// try extracting city

		
		
		// try adding longitude, latitude, total pop, and total area
		try {
			// Extract the following triples from each location
			String [] props = {"latd", "latm", "latns", "lats", "longd", "longew", "longm", "longs"};
			String [] owlProps = {"populationTotal", "areaTotal"};
			
			// Create the prop query string
			String queryString = "SELECT ?" + Utils.join(props, " ?") + " ?" + Utils.join(owlProps, " ?") + "\nWHERE {\n";
			for(String prop: props) {
				queryString += "<" + dbpResource + locName + "> <" + dbpProp + prop + "> ?" + prop + ".\n";
			}
			for(String owlProp:owlProps) {
				queryString += "<" + dbpResource + locName + "> <" + dbpOwl + owlProp + "> ?" + owlProp + ".\n";
			}
			queryString += "}";
	
			System.out.println(queryString);
			Query query = QueryFactory.create(queryString);
	
			// Execute the query and obtain results
			QueryExecution qe = QueryExecutionFactory.sparqlService(dbpSparql, query);
			ResultSet results = qe.execSelect();
	
			QuerySolution res = results.next();
			
			String[] locFields = new String[Location.fieldCount];
			int k=0;
			for(String prop: props) {
				locFields[k++] = res.get(prop).toString();
			}
			for(String owlProp: owlProps) {
				locFields[k++] = res.get(owlProp).toString();
			}
			
			loc.addLongAndLat(locFields);
			
			qe.close();
		} catch(Exception e) {
			
		}
		
		
		return loc;

	}
	

}

