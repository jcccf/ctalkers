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

		List<Celeb> celebs = evt_datails(artists);
	}
	
	public static List<Celeb> evt_datails(List<String> artists) {
		String key = "4292b0fbbdc718549158d5fbd6d19686";
		List<Celeb> celebs = new ArrayList<Celeb>();
		for (String artist : artists) {
			Collection<Event> evts = Artist.getEvents(artist, key);
			Celeb celeb = new Celeb(artist);
			celebs.add(celeb);
			List<Evt> ets = new ArrayList<Evt>();
			celeb.events = ets;
			for (Event evt : evts) {
				//evt.getTitle()
				float longitude = evt.getVenue().getLongitude();
			    float latitude = evt.getVenue().getLatitude();
				String city = evt.getVenue().getCity();
				String country = evt.getVenue().getCountry();
				String title = evt.getTitle();
				System.out.println("artists: " + artist + " title: " + title);
				Evt et = new Evt(longitude, latitude, city, country, title);
				ets.add(et);
			}
		}
		return celebs;
	}
	
}
