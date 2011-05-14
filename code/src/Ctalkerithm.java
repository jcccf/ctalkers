import java.util.*;
import java.io.*;

import org.jdom.input.SAXBuilder;
import org.jdom.*;
import org.jdom.xpath.*;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.*;

public class Ctalkerithm {

	// name spaces
	static String dbpResource = "http://dbpedia.org/resource/";
	static String dbpProp = "http://dbpedia.org/property/";
	static String dbpOwl = "http://dbpedia.org/dbpedia-owl/";
	
	static String dbpSparql = "http://dbpedia.org/sparql";

	public static void main(String[] args) throws Exception {

		/*
		// Read celebrities in and get twitter XML for each
		String[] celebs = Utils.readFile("../ontology/celebrities.txt").split("\n");

		//		String[] celebXMLList = new String[celebs.length];



		// use xpath to extract all the mention tweeter's <uri>, and <twitter:source>
		XPath uriXPath = XPath.newInstance("//atom:author/atom:uri");
		uriXPath.addNamespace("atom", "http://www.w3.org/2005/Atom");

		XPath sourceXPath = XPath.newInstance("//twitter:source");
		sourceXPath.addNamespace("twitter", "http://api.twitter.com/");

		// for follower id's
		XPath idXPath = XPath.newInstance("//id");		

		ArrayList<ArrayList<String>> cTalkers = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> cTalkerSources = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> cStalkers= new ArrayList<ArrayList<String>>();


		for(String celeb: celebs) {
			// get the celeb talkers

			SAXBuilder m = new SAXBuilder();
			String celebMentionURL = "http://search.twitter.com/search.atom?q=@" + celeb;
			Document mentionDoc = m.build(Utils.getURLStream(celebMentionURL));

			ArrayList<String> userScreenNames = new ArrayList<String>();

			// extract the screen_names
			for(String uriString:doXPath(mentionDoc, uriXPath)) {
				String screenName = uriString.split("http://twitter.com/")[1];
				userScreenNames.add(screenName);
			}			

			cTalkers.add(userScreenNames);

			ArrayList<String> userSourceList = doXPath(mentionDoc, sourceXPath);
			cTalkerSources.add(userSourceList);

//			System.out.println(celeb + " tweeter uris: " + Utils.join(userScreenNames, ", "));
//			System.out.println(celeb + " tweeter source: " + Utils.join(userSourceList, ", "));

			// get the celeb stalkers (followers)
			SAXBuilder f = new SAXBuilder();
			Document followDoc = null;
			String celebFollowURL = "http://api.twitter.com/1/followers/ids.xml?screen_name=" + celeb;
			try {
				followDoc = f.build(Utils.getURLStream(celebFollowURL));
			} catch(Exception e) {
				System.out.println("Get stalkers time out: "+ celeb);
			}

			if(followDoc == null) continue;

			ArrayList<String> stalkerIDs = doXPath(followDoc, idXPath);
			cStalkers.add(stalkerIDs);

			System.out.println(celeb + " stalker ids: " + Utils.join(stalkerIDs, ", "));

		}

		 */


		// Possibly alter location search
		//        search_xml = Nokogiri::XML(open("http://api.search.live.net/xml.aspx?Appid=D922B026428E58D0B1B38C3CB94E227BF6B113BB&query=#{CGI.escape(search_query)}&sources=web"))
		//        #puts search_xml
		//        search_ns = {"xmlns:sr" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/element", "xmlns:web" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/web"}
		//        search_top_path = search_xml.xpath("/sr:SearchResponse/web:Web/web:Results/web:WebResult/web:Description",search_ns).first
		//        search_top = search_top_path ? search_top_path.content : ""
		//        search_altered_xpath = search_xml.xpath("/sr:SearchResponse/sr:Query/sr:AlteredQuery",search_ns)
		//        search_altered = search_altered_xpath.first ? search_altered_xpath.first.content : ""




		// Search DBpedia for location information
		HashSet<String> locations = new HashSet<String>();

		/// fill with test locations
		locations.add("San_Francisco");
		locations.add("Ithaca,_New_York");
		//////////////////////////////


		// Extract the following triples from each location
		String [] props = {"latd", "latm", "latns", "lats", "longd", "longew", "longm", "longs"};
		String [] owlProps = {"populationTotal", "areaTotal"};

		for(String location: locations) {

			// Create the prop query string
//			String queryString = "SELECT ?" + Utils.join(props, " ?") + Utils.join(owlProps, " ?") + "\nWHERE {\n";
			String queryString = "SELECT ?" + Utils.join(props, " ?") + "\nWHERE {\n";
			for(String prop: props) {
				queryString += "<" + dbpResource + location + "> <" + dbpProp + prop + "> ?" + prop + ".\n";
			}
//			for(String owlProp:owlProps) {
//				queryString += "<" + dbpResource + location + "> <" + dbpOwl + owlProp + "> ?" + owlProp + ".\n";
//			}
			queryString += "}";

//			System.out.println(queryString);
			Query query = QueryFactory.create(queryString);

			// Execute the query and obtain results
			QueryExecution qe = QueryExecutionFactory.sparqlService(dbpSparql, query);
			ResultSet results = qe.execSelect();


			QuerySolution res = results.next();
			
			System.out.println("\n==================================================");
			System.out.println("Location: "+ location);
			
			for(String prop: props) {
				System.out.println(prop + ": " + res.get(prop));
			}
			
//			for(String owlProp: owlProps) {
//				System.out.println(owlProp + ": " + res.get(owlProp));
//			}

			// Output query results
			//			ResultSetFormatter.out(System.out, results, query);

			qe.close();


		}
	}


	public static ArrayList<String> doXPath(Document doc, XPath xPathSearch) throws Exception {
		List<Element> xPathResult = xPathSearch.selectNodes(doc);

		ArrayList<String> stringResult = new ArrayList<String>();

		for(Element e:xPathResult) {
			stringResult.add(e.getValue());
		}

		return stringResult;
	}

	public static ResultSet doSparql(String subj, String pred, String obj) {
		return null;
	}


}