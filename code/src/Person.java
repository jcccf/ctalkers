

public class Person {

	String actualName;

	String screenName;
	String twitterID;

	String twitterClient;

	Location location;


	public Person(String sn) {
		screenName = sn;
	}

	public Person() {
	}
	
	public void setClient(String clientURI) {
		twitterClient = clientURI.replaceAll("\\<.*?>","");
	}

	public String toString() {
		return "[" 
		+ showVar("actName", actualName) + ", "
		+ showVar("sn", screenName) + ", "
		+ showVar("id", twitterID) + ", "
		+ showVar("clnt", twitterClient) + ", "
		+ showVar("loc", location)
		+ "]";
	}

	public String showVar(String name, Object v) {
		if(v==null)
			return "";
		else
			return name + ":" + v.toString();
	}
}

