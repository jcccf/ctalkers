import java.util.*;

import org.openjena.atlas.iterator.Filter;
import org.openjena.atlas.lib.Tuple;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.sparql.sse.SSE;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.nodetable.NodeTable;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.NodeId;
import com.hp.hpl.jena.tdb.sys.SystemTDB;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * Example of how to filter quads as they are accessed at the lowest level. Can
 * be used to exclude daat from specific graphs. This mechanism is not limited
 * to graphs - it works for properties or anything where the visibility of
 * otherwise is determined by the elements of the quad. See <a
 * href="http://openjena.org/wiki/TDB/QuadFilter">TDB/QuadFilter</a> for further
 * details.
 */

public class Database {
	private static String graphToHide = "http://example/g2";
	
	Map<String,Resource> loc_to_res;
	Map<String,Resource> tcl_to_res;
	Map<String,Resource> per_to_res;
	List<Celeb> celebs;
	Model model;
	
	public Database(List<Celeb> celebs){
		this.celebs = celebs;
		loc_to_res = new HashMap<String,Resource>();
		tcl_to_res = new HashMap<String,Resource>();
		per_to_res = new HashMap<String,Resource>();
		String directory = "TDB-0.8.10/work/data/project2";
		model = TDBFactory.createModel(directory);
		model.removeAll();
	}
	
	private Resource setLocation(Resource zzz, Location l){
		if(l != null){
			Resource locres = null;
			if(loc_to_res.containsKey(l.name)){
				locres = loc_to_res.get(l.name);
			}
			else{
				Resource r = model.createResource(Ctalkology.Location)
					.addProperty(Ctalkology.country, l.country)
					.addProperty(Ctalkology.city, l.city)
					.addLiteral(Ctalkology.latitude, l.latitude)
					.addLiteral(Ctalkology.longitude, l.longitude)
					.addLiteral(Ctalkology.population, l.populationTotal)
					.addLiteral(Ctalkology.landArea, l.areaTotal);
				loc_to_res.put(l.name,r);
				locres = r;
			}
			zzz.addProperty(Ctalkology.hasLocation, locres);
		}
		return zzz;
	}
	
	private Resource setTwitterClient(Resource zzz, Person p){
		if(p.twitterClient.length() > 0){
			Resource tcl = null;
			if(tcl_to_res.containsKey(p.twitterClient)){
				tcl = tcl_to_res.get(p.twitterClient);
			}
			else{
				tcl = model.createResource(Ctalkology.TwitterClient)
					.addLiteral(FOAF.name, p.twitterClient);
				tcl_to_res.put(p.twitterClient, tcl);
			}
			zzz.addProperty(Ctalkology.hasTwitterClient, tcl);
		}
		return zzz;
	}
	
	public void run(){
		for (Celeb c : celebs) {
			// Create Celebrity
			Resource celeb_resource = model.createResource(Ctalkology.Celebrity)
				.addLiteral(FOAF.name, c.actualName)
				.addLiteral(Ctalkology.twitterID, c.twitterID);
			setTwitterClient(celeb_resource, c);
			setLocation(celeb_resource, c.location);
			
			// Add Events
			for (Evt e : c.events) {
				Resource ev = model.createResource(Ctalkology.Event)
					.addLiteral(FOAF.name, e.title);
				setLocation(ev, e.location);
				celeb_resource.addProperty(Ctalkology.hasEvent, ev);
			}
			
			// Add Mentioners
			for (Person p : c.talkers){
				Resource ps = null;
				if(per_to_res.containsKey(p.screenName)){
					ps = per_to_res.get(p.screenName);
				}
				else{
					ps = model.createResource(Ctalkology.TwitterUser);
					if(p.actualName.length() > 0)
						ps.addLiteral(FOAF.name, p.actualName);
					if(p.twitterID != null)
						ps.addLiteral(Ctalkology.twitterID, p.twitterID);
					setLocation(ps, p.location);
					setTwitterClient(ps, p);
					per_to_res.put(p.screenName, ps);
				}
				ps.addProperty(Ctalkology.mentioned, celeb_resource);
			}
			
			// Add Followers
			for (Person p : c.talkers){
				Resource ps = null;
				if(per_to_res.containsKey(p.screenName)){
					ps = per_to_res.get(p.screenName);
				}
				else{
					ps = model.createResource(Ctalkology.TwitterUser);
					if(p.actualName.length() > 0)
						ps.addLiteral(FOAF.name, p.actualName);
					if(p.twitterID != null)
						ps.addLiteral(Ctalkology.twitterID, p.twitterID);
					setLocation(ps, p.location);
					setTwitterClient(ps, p);
					per_to_res.put(p.screenName, ps);
				}
				ps.addProperty(Ctalkology.follows, celeb_resource);
			}
		}
			
		model.close();
	}
	
	public static void main(String... args) {
		
		Database db = new Database(LIST OF CELEBRITIES);
		db.run();
		
		// // This also works for default union graph ....
		// TDB.getContext().setTrue(TDB.symUnionDefaultGraph) ;
		//
		// Dataset ds = setup() ;
		// Filter<Tuple<NodeId>> filter = createFilter(ds) ;
		// example(ds, filter) ;
		// String assemblerFile = "Joseki-3.4.3/joseki-config-project2-tdb.ttl"
		// ;
		// Model model = TDBFactory.assembleModel(assemblerFile) ;
		// model.close();
		
		/**
		 * Resource loc = model.createResource(Ctalkology.Location)
		 * .addProperty(Ctalkology.city, "New York")
		 * .addLiteral(Ctalkology.latitude, 40.7141667)
		 * .addLiteral(Ctalkology.longitude, -74.0063889)
		 * .addLiteral(Ctalkology.population, 8);
		 * 
		 * Resource loc2 = model.createResource(Ctalkology.Location)
		 * .addProperty(Ctalkology.city, "San Francisco")
		 * .addLiteral(Ctalkology.latitude, 37.775)
		 * .addLiteral(Ctalkology.longitude, -122.4183333)
		 * .addLiteral(Ctalkology.population, 100);
		 * 
		 * Resource ev = model.createResource(Ctalkology.Event)
		 * .addLiteral(FOAF.name, "Calevent")
		 * .addProperty(Ctalkology.hasLocation, loc2);
		 * 
		 * Resource tc = model.createResource(Ctalkology.TwitterClient)
		 * .addLiteral(FOAF.name, "Tweetie"); Resource lg =
		 * model.createResource(Ctalkology.Celebrity) .addLiteral(FOAF.name,
		 * "Lady Gaga") .addProperty(Ctalkology.hasTwitterClient, tc)
		 * .addProperty(Ctalkology.hasEvent, ev)
		 * .addProperty(Ctalkology.hasLocation, loc); Resource lgfan =
		 * model.createResource(Ctalkology.TwitterUser) .addLiteral(FOAF.name,
		 * "Gagaga") .addProperty(Ctalkology.follows, lg)
		 * .addProperty(Ctalkology.hasLocation, loc2); Resource lgfan2 =
		 * model.createResource(Ctalkology.TwitterUser) .addLiteral(FOAF.name,
		 * "Kakaka") .addProperty(Ctalkology.mentioned, lg)
		 * .addProperty(Ctalkology.hasLocation, loc)
		 * .addProperty(Ctalkology.hasTwitterClient, tc);
		 **/

	}

	/** Example setup - in-memory dataset with two graphs, one triple in each */
	private static Dataset setup() {
		Dataset ds = TDBFactory.createDataset();
		DatasetGraphTDB dsg = (DatasetGraphTDB) (ds.asDatasetGraph());
		Quad q1 = SSE
				.parseQuad("(<http://example/g1> <http://example/s> <http://example/p> <http://example/o1>)");
		Quad q2 = SSE
				.parseQuad("(<http://example/g2> <http://example/s> <http://example/p> <http://example/o2>)");
		dsg.add(q1);
		dsg.add(q2);
		return ds;
	}

	/** Create a filter to exclude the graph http://example/g2 */
	private static Filter<Tuple<NodeId>> createFilter(Dataset ds) {
		DatasetGraphTDB dsg = (DatasetGraphTDB) (ds.asDatasetGraph());
		final NodeTable nodeTable = dsg.getQuadTable().getNodeTupleTable()
				.getNodeTable();
		// Filtering operates at a very low level:
		// need to know the internal identifier for the graph name.
		final NodeId target = nodeTable.getNodeIdForNode(Node
				.createURI(graphToHide));

		System.out.println("Hide graph: " + graphToHide + " --> " + target);

		// Filter for accept/reject as quad as being visible.
		// Return true for "accept", false for "reject"
		Filter<Tuple<NodeId>> filter = new Filter<Tuple<NodeId>>() {
			public boolean accept(Tuple<NodeId> item) {
				// Reverse the lookup as a demo
				// Node n = nodeTable.getNodeForNodeId(target) ;
				// System.err.println(item) ;
				if (item.size() == 4 && item.get(0).equals(target)) {
					// System.out.println("Reject: "+item) ;
					return false;
				}
				// System.out.println("Accept: "+item) ;
				return true;
			}
		};

		return filter;
	}

	private static void example(Dataset ds, Filter<Tuple<NodeId>> filter) {
		String[] x = { "SELECT * { GRAPH ?g { ?s ?p ?o } }",
				"SELECT * { ?s ?p ?o }",
				// THis filter does not hide the graph itself, just the quads
				// associated with the graph.
				"SELECT * { GRAPH ?g {} }" };

		for (String qs : x) {
			example(ds, qs, filter);
			example(ds, qs, null);
		}

	}

	private static void example(Dataset ds, String qs,
			Filter<Tuple<NodeId>> filter) {
		System.out.println();
		Query query = QueryFactory.create(qs);
		System.out.println(qs);
		QueryExecution qExec = QueryExecutionFactory.create(query, ds);
		// Install filter for this query only.
		if (filter != null) {
			System.out.println("Install quad-level filter");
			qExec.getContext().set(SystemTDB.symTupleFilter, filter);
		} else
			System.out.println("No quad-level filter");
		ResultSetFormatter.out(qExec.execSelect());
		qExec.close();

	}

}

/*
 * (c) Copyright 2010 Epimorphics Ltd. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */