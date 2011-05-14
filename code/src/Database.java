
import org.openjena.atlas.iterator.Filter ;
import org.openjena.atlas.lib.Tuple ;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.query.Dataset ;
import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.query.QueryExecution ;
import com.hp.hpl.jena.query.QueryExecutionFactory ;
import com.hp.hpl.jena.query.QueryFactory ;
import com.hp.hpl.jena.query.ResultSetFormatter ;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.core.Quad ;
import com.hp.hpl.jena.sparql.sse.SSE ;
import com.hp.hpl.jena.tdb.TDB ;
import com.hp.hpl.jena.tdb.TDBFactory ;
import com.hp.hpl.jena.tdb.nodetable.NodeTable ;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB ;
import com.hp.hpl.jena.tdb.store.NodeId ;
import com.hp.hpl.jena.tdb.sys.SystemTDB ;
import com.hp.hpl.jena.vocabulary.ResultSet;

/** Example of how to filter quads as they are accessed at the lowest level.
 * Can be used to exclude daat from specific graphs.   
 * This mechanism is not limited to graphs - it works for properties or anything
 * where the visibility of otherwise is determined by the elements of the quad. 
 * See <a href="http://openjena.org/wiki/TDB/QuadFilter">TDB/QuadFilter</a>
 * for further details.
 */

public class Database
{
    private static String graphToHide = "http://example/g2" ;

    public static void main(String[] args) {
    	String fldr = "../TDB-0.8.10/work/data/project2";
    	Model model = TDBFactory.createModel(fldr);
    	
    	model.close();
    }
    
    
    public static void mai(String ... args)
    {
        // This also works for default union graph ....
        TDB.getContext().setTrue(TDB.symUnionDefaultGraph) ;
        
        Dataset ds = setup() ;
        Filter<Tuple<NodeId>> filter = createFilter(ds) ;
        example(ds, filter) ;
    }
    


    
    /** Example setup - in-memory dataset with two graphs, one triple in each */
    private static Dataset setup()
    {
        Dataset ds = TDBFactory.createDataset() ;
        DatasetGraphTDB dsg = (DatasetGraphTDB)(ds.asDatasetGraph()) ;
        Quad q1 = SSE.parseQuad("(<http://example/g1> <http://example/s> <http://example/p> <http://example/o1>)") ;
        Quad q2 = SSE.parseQuad("(<http://example/g2> <http://example/s> <http://example/p> <http://example/o2>)") ;
        dsg.add(q1) ;
        dsg.add(q2) ;
        return ds ;
    }
        
    /** Create a filter to exclude the graph http://example/g2 */
    private static Filter<Tuple<NodeId>> createFilter(Dataset ds)
    {
        DatasetGraphTDB dsg = (DatasetGraphTDB)(ds.asDatasetGraph()) ;
        final NodeTable nodeTable = dsg.getQuadTable().getNodeTupleTable().getNodeTable() ;
        // Filtering operates at a very low level: 
        // need to know the internal identifier for the graph name. 
        final NodeId target = nodeTable.getNodeIdForNode(Node.createURI(graphToHide)) ;

        System.out.println("Hide graph: "+graphToHide+" --> "+target) ;
        
        // Filter for accept/reject as quad as being visible.
        // Return true for "accept", false for "reject"
        Filter<Tuple<NodeId>> filter = new Filter<Tuple<NodeId>>() {
            public boolean accept(Tuple<NodeId> item)
            {
                // Reverse the lookup as a demo
                //Node n = nodeTable.getNodeForNodeId(target) ;
                //System.err.println(item) ;
                if ( item.size() == 4 && item.get(0).equals(target) )
                {
                    //System.out.println("Reject: "+item) ;
                    return false ;
                }
                //System.out.println("Accept: "+item) ;
                return true ;
            } } ;
            
        return filter ;
    }            
        
    private static void example(Dataset ds, Filter<Tuple<NodeId>> filter)
    {
        String[] x = {
            "SELECT * { GRAPH ?g { ?s ?p ?o } }",
            "SELECT * { ?s ?p ?o }",
            // THis filter does not hide the graph itself, just the quads associated with the graph.
            "SELECT * { GRAPH ?g {} }"
            } ;
        
        for ( String qs : x )
        {
            example(ds, qs, filter) ;
            example(ds, qs, null) ;
        }
        
    }

    private static void example(Dataset ds, String qs, Filter<Tuple<NodeId>> filter)
    {
        System.out.println() ;
        Query query = QueryFactory.create(qs) ;
        System.out.println(qs) ;
        QueryExecution qExec = QueryExecutionFactory.create(query, ds) ;
        // Install filter for this query only.
        if ( filter != null )
        {
            System.out.println("Install quad-level filter") ;
            qExec.getContext().set(SystemTDB.symTupleFilter, filter) ;
        }
        else
            System.out.println("No quad-level filter") ;
        ResultSetFormatter.out(qExec.execSelect()) ;
        qExec.close() ;

    }
        
}
