import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;


import javax.xml.xpath.*;
import org.xml.sax.InputSource;


//import oauth.signpost.signature.SignatureMethod;                                                           

public class TwitterClient {
	
	public static void addUserLocations(ArrayList<Person> usrs, HashMap<String, Location> locationMap) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, XPathExpressionException {

		OAuthConsumer consumer = new DefaultOAuthConsumer(
				// the consumer key of this app (replace this with yours)
				"2GybNeEQ8u3h0kB8LwdQ",
				"X3mq3vl7x4AVuCsU33VxCl2VmHITsEneTD516losEo");

		OAuthProvider provider = new DefaultOAuthProvider(
				"http://twitter.com/oauth/request_token",
				"http://twitter.com/oauth/access_token",
				"http://twitter.com/oauth/authorize");

		String ACCESS_TOKEN = "104269424-KoVBZsk7GVmeRCUIDYeQDsFDcIEa0w7mOiBuHwFY";
		String TOKEN_SECRET = "FOTCYUjkH2dc2qB8coMxOyruUQsezNXmvOStt64";

		// if not yet done, load the token and token secret for
		// the current user and set them
		consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

		Map<String, String> loc_name = new HashMap<String, String>();
		
		int i=0;
		for (Person person : usrs) {
			String url_str = null;
			if(person.twitterID != null) { 
				url_str = "http://api.twitter.com/1/users/lookup.xml?user_id="+ person.twitterID;
			} else if(person.screenName != null) {
				url_str = "http://api.twitter.com/1/users/lookup.xml?screen_name="+ person.screenName;
			} else {
				i++;
				continue;
			}
			
			// create a request that requires authentication
			URL url = new URL(url_str);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();

			// sign the request
			consumer.sign(request);

			// send the request
			request.connect();

			// response status should be 200 OK
			int statusCode = request.getResponseCode();
			System.out.println(statusCode);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}

			//x path!
			String data = sb.toString();
			System.out.println(data);
			
			XPathFactory factory=XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			
			InputSource inputSource = new InputSource(new StringReader(data));
			String rawLocation = xPath.evaluate("/users/user/location", inputSource);
			
			inputSource = new InputSource(new StringReader(data));
			String name = xPath.evaluate("/users/user/name", inputSource);
			
			inputSource = new InputSource(new StringReader(data));
			String screen_name = xPath.evaluate("/users/user/screen_name", inputSource);
			
			inputSource = new InputSource(new StringReader(data));
			String id = xPath.evaluate("/users/user/id", inputSource);
			
			
			// Either retrieve the location from the hashmap, or create a new one
			String cleanedLocation = Utils.cleanLocation(rawLocation);
			Location locationObj  = locationMap.get(cleanedLocation);
			if(locationObj == null) {
				locationObj = Location.createLocation(cleanedLocation);
				locationMap.put(locationObj.name, locationObj);
			}
			
			person.location = locationObj;
			person.actualName = name;
			person.screenName = screen_name;
			person.twitterID = id;


			System.out.println(person);

			i++;
		}
//		return loc_name;
	}


	public static void main(String[] args) throws Exception {
		
		Person p = new Person("smoseson");
//		p.twitterID = "783214";
		ArrayList<Person> test = new ArrayList<Person>();
		test.add(p);
		addUserLocations(test);
//		List<String> usr_ids = new ArrayList<String>();
//		usr_ids.add("783214");
//		usr_ids.add("6253282");
//		Map<String,String> loc_name = addUserLocation(usr_ids);
	}
	
}
