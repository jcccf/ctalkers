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
	static String dbpOwl = "http://dbpedia.org/ontology/";
	
	static String dbpSparql = "http://dbpedia.org/sparql";

	public static void main(String[] args) throws Exception {

		// Read celebrities in and get twitter XML for each
		String[] celebNames = Utils.readFile("../ontology/celebrities.txt").split("\n");
		
		ArrayList<Celeb> celebs = new ArrayList<Celeb>();
		for(String sn: celebNames) {
			celebs.add(new Celeb(sn));
		}

		// use xpath to extract all the mention tweeter's <uri>, and <twitter:source>
		XPath uriXPath = XPath.newInstance("//atom:author/atom:uri");
		uriXPath.addNamespace("atom", "http://www.w3.org/2005/Atom");

		XPath sourceXPath = XPath.newInstance("//twitter:source");
		sourceXPath.addNamespace("twitter", "http://api.twitter.com/");

		// for follower id's
		XPath idXPath = XPath.newInstance("//id");

		int i=0;
		for(String celeb: celebNames) {
			// get the celeb talkers (mentioners)

			SAXBuilder m = new SAXBuilder();
			String celebMentionURL = "http://search.twitter.com/search.atom?q=@" + celeb;
			Document mentionDoc = m.build(Utils.getURLStream(celebMentionURL));


			// extract the screen_names
			ArrayList<String> userSourceList = doXPath(mentionDoc, sourceXPath);

			int j = 0;
			for(String uriString:doXPath(mentionDoc, uriXPath)) {
				String screenName = uriString.replaceFirst("http://twitter.com/", "");
				Person talker = new Person(screenName);
				talker.twitterClient = userSourceList.get(j);
				celebs.get(i).talkers.add(talker);
				j++;
			}			


			System.out.println(celeb + " tweeters: " + Utils.join(celebs.get(i).talkers, ", "));

			// get the celeb stalkers (followers)
			
			 // Site isn't responding for celeb followers (code is working fine though)
			/*
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
			for(String id:stalkerIDs) {
				Person talker = new Person();
				talker.twitterID = id;
				celebs.get(i).stalkers.add(talker);
			}
			
			System.out.println(celeb + " stalker ids: " + Utils.join(celebs.get(i).stalkers, ", "));
			*/
			i++;
		}

		// Possibly alter location field by searching for it
		//        search_xml = Nokogiri::XML(open("http://api.search.live.net/xml.aspx?Appid=D922B026428E58D0B1B38C3CB94E227BF6B113BB&query=#{CGI.escape(search_query)}&sources=web"))
		//        #puts search_xml
		//        search_ns = {"xmlns:sr" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/element", "xmlns:web" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/web"}
		//        search_top_path = search_xml.xpath("/sr:SearchResponse/web:Web/web:Results/web:WebResult/web:Description",search_ns).first
		//        search_top = search_top_path ? search_top_path.content : ""
		//        search_altered_xpath = search_xml.xpath("/sr:SearchResponse/sr:Query/sr:AlteredQuery",search_ns)
		//        search_altered = search_altered_xpath.first ? search_altered_xpath.first.content : ""


		// Search DBpedia for location information
		HashSet<String> locNames = new HashSet<String>();

		/// fill with test locations for now
		locNames.add("San_Francisco");
		locNames.add("Ithaca,_New_York");
		//////////////////////////////

		// Extract the following triples from each location
		String [] props = {"latd", "latm", "latns", "lats", "longd", "longew", "longm", "longs"};
		String [] owlProps = {"populationTotal", "areaTotal"};
		
		ArrayList<Location> locations = new ArrayList();
		for(String locName: locNames) {

			// Create the prop query string
			String queryString = "SELECT ?" + Utils.join(props, " ?") + " ?" + Utils.join(owlProps, " ?") + "\nWHERE {\n";
			for(String prop: props) {
				queryString += "<" + dbpResource + locName + "> <" + dbpProp + prop + "> ?" + prop + ".\n";
			}
			for(String owlProp:owlProps) {
				queryString += "<" + dbpResource + locName + "> <" + dbpOwl + owlProp + "> ?" + owlProp + ".\n";
			}
			queryString += "}";

//			System.out.println(queryString);
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
			Location loc = new Location(locName, locFields);

			qe.close();

			System.out.println(loc);

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


}