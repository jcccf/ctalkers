Ctalkers (pronounced "stalkers")
================================

XML Data sources
----------------
* Twitter - celebrity mentioners and followers
* Last.fm - event data

LOD sources
-----------
* DBpedia - location latitude and longitude, population, area

Description
-----------
In today's increasingly connected world, artists connect with their fans on social networks like Twitter and Last.fm. From knowing specific music artists' on both Twitter and Last.fm, we can get a list of people who mention or follow celebrities on Twitter, as well as events that music artists host or attend from Last.fm. From DBpedia we can also get interesting facts about the locations that Twitter users, celebrities reside in or where events are hosted.

All this information allows us to ask questions like "tell me about events that a celebrity that I'm following has near me", or "I'm a celebrity - who's talking about me? Do they live near me?"

Launch Joseki
=============

On my computer
--------------
export TDBROOT=/Users/jcccf/Documents/git/ctalkers/TDB-0.8.10
export JOSEKIROOT=/Users/jcccf/Documents/git/ctalkers/Joseki-3.4.3
export CLASSPATH=$CLASSPATH:$TDBROOT/lib
./bin/rdfserver joseki-config-project2-tdb.ttl 

./schemagen -i /Users/jcccf/Documents/git/ctalkers/ontology/ctalkology.owl -o /Users/jcccf/Documents/git/ctalkers/ontology/ --owl

On your computer
----------------
export TDBROOT=/..../TDB-0.8.10
export JOSEKIROOT=/...../Joseki-3.4.3
export CLASSPATH=$CLASSPATH:$TDBROOT/lib
./bin/rdfserver joseki-config-project2-tdb.ttl 



ja:reasoner [  
   ja:reasonerClass "org.mindswap.pellet.jena.PelletReasonerFactory";  
   ja:schema [
      a ja:MemoryModel;
      ja:content [ja:externalContent "../ontology/ctalkology.owl" ];
   ];
];