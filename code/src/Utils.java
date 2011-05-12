import java.io.*;
import java.net.*;

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
}
