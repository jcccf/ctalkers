import java.io.*;
import java.net.*;
import java.util.*;

public class Utils {
	
	
	public static String openURL(String url) throws Exception {
		URL celebURL = new URL(url);
		URLConnection celebTwitterXML = celebURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(celebTwitterXML.getInputStream()));

		return readLines(in);
	}

	public static String readFile(String filePath) throws Exception {
		FileInputStream fstream = new FileInputStream(filePath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String fileString = readLines(br);
		in.close();
		return fileString;
	}

	public static String readLines(BufferedReader br) throws Exception {
		String inputLine;
		String strAcc = "";
		while ((inputLine = br.readLine()) != null) {
			strAcc += inputLine + "\n";
		}
		br.close();		

		return strAcc;
	}
	
	public static InputStreamReader getURLStream(String urlString) throws Exception {
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		return new InputStreamReader(connection.getInputStream());
	}
	
	public static String join(ArrayList array, String delimiter) {
		String joined = "";
		if(array != null) {
			for(int i=0; i<array.size(); i++) {
				Object obj = array.get(i);
				joined += obj.toString();
				if(i < array.size() - 1) {
					joined += delimiter;
				}
			}
		}
		return joined;
	}
	
	
}
