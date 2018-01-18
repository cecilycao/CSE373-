package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<URI, ISet<URI>>();
        IList<URI> URIlist = new DoubleLinkedList<URI>();
        //set of all URI
        ISet<URI> allURIs = new ChainedHashSet<URI>();
        for (Webpage webpage : webpages) {
        		allURIs.add(webpage.getUri());
        }
        for (Webpage webpage : webpages) {
        		ISet<URI> URISet = new ChainedHashSet<URI>();
        		URIlist = webpage.getLinks();
        		URI pageURI = webpage.getUri();
        		for (URI link : URIlist) {
        			if (allURIs.contains(link) && link != pageURI) {
        				URISet.add(link);
        			}
        		}
        		graph.put(pageURI, URISet);
        }                             

        return graph;
    }
    
    
    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
    		
    		IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<URI, Double>();
    		IDictionary<URI, Double> sum = new ChainedHashDictionary<URI, Double>();
    		for (KVPair<URI, ISet<URI>> element : graph) {
    			sum.put(element.getKey(), 0.0);
    			oldRanks.put(element.getKey(), 1.0 / graph.size());
    		}
    		
        for (int i = 0; i < limit; i++) {
        		IDictionary<URI, Double> newRanks = new ChainedHashDictionary<URI, Double>();
        		newRanks = stepUpdate(sum, graph, oldRanks, decay);

        		if (checkEarlyEnding(epsilon, oldRanks, newRanks)) {
        			return newRanks;
        		}
        		
        		oldRanks = newRanks;
        }
        return oldRanks;
    }
    
    private boolean checkEarlyEnding(double epsilon, 
    									IDictionary<URI, Double> oldRanks, 
    									IDictionary<URI, Double> newRanks) {
    		boolean conclusion = true;
    		for (KVPair<URI, Double> element : oldRanks) {
    			URI key = element.getKey();
    			if (Math.abs((oldRanks.get(key) - newRanks.get(key))) > epsilon) {
    				return false;
    			}
    		}
    		return conclusion;
    }
    
    private IDictionary<URI, Double> stepUpdate(IDictionary<URI, Double> sum, 
    											   IDictionary<URI, ISet<URI>> graph, 
    											   IDictionary<URI, Double> oldRanks, double decay) {
    		IDictionary<URI, Double> newRanks = new ChainedHashDictionary<URI, Double>();
    		Double emptyOldRankSum = 0.0;
    		for (KVPair<URI, ISet<URI>> element : graph) {
    			ISet<URI> value = element.getValue();
    			URI key = element.getKey();
    			int outDegree = value.size();
    			
    			if (outDegree != 0) {
	    			for (URI term : value) {
	    				sum.put(term, sum.get(term) + (decay * oldRanks.get(key) / (1.0 * outDegree)));
	    			}
    			} else {
    				emptyOldRankSum += oldRanks.get(key);
    			}
    		}
    		
    		for (KVPair<URI, Double> element : sum) {
    			double additional = decay * emptyOldRankSum / (1.0 * graph.size());
    			newRanks.put(element.getKey(), element.getValue() + additional 
    					+ (1 - decay) / (1.0 * graph.size()));
    			sum.put(element.getKey(), 0.0);
    		}
    		return newRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        // TODO: Add working code here
        return pageRanks.get(pageUri);
    }
}
