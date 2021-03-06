/* CVS $Id: $ */
 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from /Users/jcccf/Documents/git/ctalkers/ontology/ctalkology.owl 
 * @author Auto-generated by schemagen on 15 May 2011 20:49 
 */
public class Ctalkology {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final Property city = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#city" );
    
    public static final Property country = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#country" );
    
    public static final Property followedBy = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#followedBy" );
    
    public static final Property follows = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#follows" );
    
    public static final Property hasEvent = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#hasEvent" );
    
    public static final Property hasLocation = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#hasLocation" );
    
    public static final Property hasTwitterClient = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#hasTwitterClient" );
    
    public static final Property landArea = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#landArea" );
    
    public static final Property latitude = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#latitude" );
    
    public static final Property longitude = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#longitude" );
    
    public static final Property mentioned = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#mentioned" );
    
    public static final Property mentionedBy = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#mentionedBy" );
    
    public static final Property population = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#population" );
    
    public static final Property state = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#state" );
    
    public static final Property twitterId = m_model.createProperty( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#twitterId" );
    
    public static final Resource Celebrity = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#Celebrity" );
    
    public static final Resource CelebrityGroup = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#CelebrityGroup" );
    
    public static final Resource CelebrityIndividual = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#CelebrityIndividual" );
    
    public static final Resource Entities = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#Entities" );
    
    public static final Resource Event = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#Event" );
    
    public static final Resource Location = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#Location" );
    
    public static final Resource TwitterClient = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#TwitterClient" );
    
    public static final Resource TwitterUser = m_model.createResource( "http://www.semanticweb.org/ontologies/2011/4/ctalkology.owl#TwitterUser" );
    
}
