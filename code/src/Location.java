

public class Location {

	public String name;
	public String city;
	public String country;
	
	public double longitude;
	public double latitude;
	public int populationTotal;
	public int areaTotal;
	public static int fieldCount = 10;
	
	public Location(String n) {
		name = Utils.cleanLocation(n);
	}
	
	
	
	public Location(float lon, float lat, String ct, String ctr) {
		longitude = lon;
		latitude = lat;
		city = ct;
		country = ctr;
		set_name();
	}
	
	public Location(String n, String[] f) {
		//name = n;
		name = Utils.cleanLocation(n);
		
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
	
	public boolean set_name() {
		if (!(city.equals(""))) 
			name = city;
		else
			if (!(country.equals("")))
				name = country;
			else
				return false;
		return true;
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

