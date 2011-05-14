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
		
		ArrayList<ArrayList<String>> celebMentionUsers = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> celebUserSource = new ArrayList<ArrayList<String>>();

		for(String celeb: celebs) {
			// get mentioners
			
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

			celebMentionUsers.add(userScreenNames);
			celebUserSource.add(userSourceList);
			
//			System.out.println(celeb + " tweeter uris: " + Utils.join(userScreenNames, ", "));
//			System.out.println(celeb + " tweeter source: " + Utils.join(userSourceList, ", "));
			
			// get followers
			SAXBuilder f = new SAXBuilder();
			Document followDoc = null;
			String celebFollowURL = "http://api.twitter.com/1/followers/ids.xml?screen_name=" + celeb;
			try {
				followDoc = f.build(Utils.getURLStream(celebFollowURL));
			} catch(Exception e) {
				System.out.println("time out");
			}
			
			if(followDoc == null) continue;
			System.out.println("Success! Celeb: "+ celeb);
			List<Element> followerIds = idXPath.selectNodes(followDoc);
			
			
			

		}
		
		


	}


}