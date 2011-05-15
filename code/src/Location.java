

public class Location {

	public String name;
	
	public double longitude;
	public double latitude;
	public int populationTotal;
	public int areaTotal;
	public static int fieldCount = 10;
	
	public Location(String n, String[] f) {
		name = n;
		
		
		double[] ls = new double[8];
		for (int i = 0; i < 8; i++) {
			if(i != 2 && i != 5)
				ls[i] = Float.parseFloat(f[i]);
		}
		
		populationTotal = Integer.parseInt(f[8]);
		areaTotal = Integer.parseInt(f[9]);
		
		int ns = (f[2].equals("N")) ? 1 : -1;
		int ew = (f[5].equals("E")) ? 1 : -1;
		latitude = (ls[0] + ls[1] / 60 + ls[3]/3600) * ns;
		longitude = (ls[4] + ls[6] / 60 + ls[7]/3600) * ew;
	}
	
	
	
	public String toString() {
		return "{" +
		name + ", " +
		latitude + ", " +
		longitude + ", " +
		populationTotal + "," +
		areaTotal + "}";
	}

}

