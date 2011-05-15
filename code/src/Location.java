

public class Location {

	String name;
	
	String latd = "";
	String latm = "";
	String latns = "";
	String lats = "";
	String longd = "";
	String longew = "";
	String longm = "";
	String longs = "";
	String populationTotal = "";
	String areaTotal = "";
	
	public static final int fieldCount = 10;
	
	public Location(String n) {
		name = n;
	}
	
	public Location(String n, String[] f) {
		name = n;
		latd = f[0];
		latm = f[1];
		latns = f[2];
		lats = f[3];
		longd = f[4];
		longew = f[5];
		longm = f[6];
		longs = f[7];
		populationTotal = f[8];
		areaTotal = f[9];
	}
	
	
	
	public String toString() {
		return "{" +
		name + ", " +
		latd + "," +
		latm + "," +
		latns + "," +
		lats + "," +
		longd + "," +
		longew + "," +
		longm + "," +
		longs + "," +
		populationTotal + "," +
		areaTotal + "}";
	}

}

