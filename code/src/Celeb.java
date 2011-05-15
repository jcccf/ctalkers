import java.util.ArrayList;
import java.util.List;

public class Celeb extends Person {

	ArrayList<Person> talkers = new ArrayList<Person>();
	ArrayList<Person> stalkers = new ArrayList<Person>();
	List<Evt> events; //= new ArrayList<Evt>();
	public String lastFmName;
	
	
	public Celeb(String sn) {
		super(sn);
	}

	public Celeb() {
		super();
	}


}

