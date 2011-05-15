//import com.google.gdata.*;
import oauth.signpost.*;
import oauth.signpost.basic.*;
//import oauth.signpost.exception.*;
//import oauth.signpost.http.*;
//import oauth.signpost.signature.*;

import java.net.*;

public class TwitterOAuth {


	// need to put our keys in - Stephen
	OAuthConsumer consumer = new DefaultOAuthConsumer(
			// the consumer key of this app (replace this with yours)
			"iIlNngv1KdV6XzNYkoLA",
			// the consumer secret of this app (replace this with yours)
			"exQ94pBpLXFcyttvLoxU2nrktThrlsj580zjYzmoM"
	);

	OAuthProvider provider = new DefaultOAuthProvider(
			"http://twitter.com/oauth/request_token",
			"http://twitter.com/oauth/access_token",
			"http://twitter.com/oauth/authorize"
	);

	public TwitterOAuth() throws Exception {
		/****************************************************
		 * The following steps should only be performed ONCE
		 ***************************************************/

		// we do not support callbacks, thus pass OOB
		String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

		// bring the user to authUrl, e.g. open a web browser and note the PIN code
		// ...         

		String pinCode = "?";// ... you have to ask this from the user, or obtain it
		// from the callback if you didn't do an out of band request

		// user must have granted authorization at this point
		provider.retrieveAccessToken(consumer, pinCode);

		// store consumer.getToken() and consumer.getTokenSecret(),
		// for the current user, e.g. in a relational database
		// or a flat file
		// ...
	}

	// might want this to return something better - Stephen
	public void oAuthRequest(String urlString, String ACCESS_TOKEN, String TOKEN_SECRET) throws Exception {
		/****************************************************
		 * The following steps are performed everytime you
		 * send a request accessing a resource on Twitter
		 ***************************************************/

		// if not yet done, load the token and token secret for
		// the current user and set them
		consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);

		// create a request that requires authentication
		URL url = new URL("http://twitter.com/statuses/mentions.xml");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();

		// sign the request
		consumer.sign(request);

		// send the request
		request.connect();

		// response status should be 200 OK
		int statusCode = request.getResponseCode();
	}
}