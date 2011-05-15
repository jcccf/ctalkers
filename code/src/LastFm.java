import java.util.*;

import com.ibm.icu.text.DateFormat;

import de.umass.lastfm.*;


public class LastFm {

	public static void main(String[] args) {
		List<String> artists = new ArrayList<String>();
		
		//artists.add("Pink Floyd");
		//artists.add("Depeche Mode");
		//artists.add("The Beatles");
		String[] ats = {"Lady+GaGa",
		"Justin+Timberlake",
		"Katy+Perry",
		"Pink",
		"Justin+Bieber",
		"Alicia+Keys",
		"Ke$ha",
		"Mariah+Carey",
		"Eminem",
		"Kanye+West",
		"P.+Diddy",
		"Will.I.Am",
		"Bruno+Mars",
		"Demi+Lovato",
		"Ozzy+Osbourne",
		"The+Killers",
		"Jonas+Brothers",
		"Jason+Mraz",
		"Shakira",
		"Pearl+Jam",
		"Imogen+Heap",
		"50+Cent",
		"Taylor+Swift",
		"Enrique+Iglesias",
		"Coldplay",
		"Trent+Reznor",
		"Queen+Latifah",
		"Nelly+Furtado",
		"Lenny+Kravitz",
		"Moby",
		"Green+Day",
		"Mariah+Carey"};
		for (String at : ats) {
			String real_name = at.replace("+", " ");
			artists.add(real_name);
		}

		//List<Celeb> celebs = evt_details(artists);
	}
	
	public static void add_details_to_evt(List<Person> celebs) {
		String key = "4292b0fbbdc718549158d5fbd6d19686";
		for (Person d : celebs) {
			Celeb c = (Celeb) d;
			Collection<Event> evts = Artist.getEvents(c.lastFmName, key);
			List<Evt> ets = new ArrayList<Evt>();
			c.events = ets;
			for (Event evt : evts) {
				//evt.getTitle()
				float longitude = evt.getVenue().getLongitude();
			    float latitude = evt.getVenue().getLatitude();
				String city = evt.getVenue().getCity();
				String country = evt.getVenue().getCountry();
				String title = evt.getTitle();
				System.out.println("artists: " + c.lastFmName + " title: " + title);

				Location loc = new Location(longitude, latitude, city, country);
				Evt et = new Evt(title, loc);
				ets.add(et);
			}
		}
	}
	
}
