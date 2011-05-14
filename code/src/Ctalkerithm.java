import org.jdom.input.SAXBuilder;
import org.jdom.*;
import org.jdom.xpath.*;
import java.util.*;


import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.*;

public class Ctalkerithm {


	public static void main(String[] args) throws Exception {

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

			List<Element> userList = uriXPath.selectNodes(mentionDoc);
			List<Element> sourceList = sourceXPath.selectNodes(mentionDoc);

			ArrayList<String> userScreenNames = new ArrayList<String>();
			ArrayList<String> userSourceList = new ArrayList<String>();
			
			for(Element uriElement:userList) {
				String uriString = uriElement.getValue();
				String screenName = uriString.split("http://twitter.com/")[1];
				userScreenNames.add(screenName);
			}
			
			for(Element source:sourceList) {
				userSourceList.add(source.getValue());
			}

			cTalkers.add(userScreenNames);
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
				System.out.println("Time out: "+ celeb);
			}
			
			if(followDoc == null) continue;
			List<Element> followerIds = idXPath.selectNodes(followDoc);
			
			ArrayList<String> stalkerIDs = new ArrayList<String>();

			for(Element id:followerIds) {
				stalkerIDs.add(id.getValue());
			}

			cStalkers.add(stalkerIDs);
			System.out.println(celeb + " stalker ids: " + Utils.join(stalkerIDs, ", "));
			
		}
		
		
		
		
		// Possibly alter location search
//        search_xml = Nokogiri::XML(open("http://api.search.live.net/xml.aspx?Appid=D922B026428E58D0B1B38C3CB94E227BF6B113BB&query=#{CGI.escape(search_query)}&sources=web"))
//        #puts search_xml
//        search_ns = {"xmlns:sr" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/element", "xmlns:web" => "http://schemas.microsoft.com/LiveSearch/2008/04/XML/web"}
//        search_top_path = search_xml.xpath("/sr:SearchResponse/web:Web/web:Results/web:WebResult/web:Description",search_ns).first
//        search_top = search_top_path ? search_top_path.content : ""
//        search_altered_xpath = search_xml.xpath("/sr:SearchResponse/sr:Query/sr:AlteredQuery",search_ns)
//        search_altered = search_altered_xpath.first ? search_altered_xpath.first.content : ""

		
		
		
		// Search DBpedia for location information
		
		
		


	}	
	

	
	

}