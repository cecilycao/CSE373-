//  Yiran Fu, Ye Cao
//	CSE 373

package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
* This class is responsible for computing how "relevant" any given document is
* to a given search query.
*
* See the spec for more details.
*/
public class TfIdfAnalyzer {
   // This field must contain the IDF score for every single word in all
   // the documents.
   private IDictionary<String, Double> idfScores;
   
   // This field must contain the TF-IDF vector for each webpage you were given
   // in the constructor.
   //
   // We will use each webpage's page URI as a unique key.
   private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
   
   private IDictionary<URI, Double> docNorm;
   
   public TfIdfAnalyzer(ISet<Webpage> webpages) {
      // Implementation note: We have commented these method calls out so your
      // search engine doesn't immediately crash when you try running it for the
      // first time.
      //
      // You should uncomment these lines when you're ready to begin working
      // on this class.
      this.docNorm = new ChainedHashDictionary<URI, Double>();
      this.idfScores = this.computeIdfScores(webpages);
      this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
      
   }
   
   // Note: this method, strictly speaking, doesn't need to exist. However,
   // we've included it so we can add some unit tests to help verify that your
   // constructor correctly initializes your fields.
   public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
      return this.documentTfIdfVectors;
   }
   
   // Note: these private methods are suggestions or hints on how to structure your
   // code. However, since they're private, you're not obligated to implement exactly
   // these methods: Feel free to change or modify these methods if you want. The
   // important thing is that your 'computeRelevance' method ultimately returns the
   // correct answer in an efficient manner.
   
   /**
   * This method should return a dictionary mapping every single unique word found
   * in any documents to their IDF score.
   */
   
   private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
      IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
      IDictionary<String, Double> answer = new ChainedHashDictionary<String, Double>();
      
      int pagesSize = pages.size();
      for (Webpage item : pages) {
         IList<String> pageWords = item.getWords();
         ISet<String> allWords = new ChainedHashSet<String>();
         int length = 0;
         for (String word : pageWords) {
            allWords.add(word);
            if (allWords.size() > length) {
               if (!result.containsKey(word)) {
                  result.put(word, 1.0);
               } else {
                  double value = result.remove(word);
                  result.put(word, value + 1);
               }
               length++;
            }
         }
      }
      for (KVPair<String, Double> element : result) {
         String term = element.getKey();
         double value = element.getValue();
         double data = Math.log(1.0 * pagesSize / value);
         answer.put(term, data);
      }
      return answer;
   }
   
   
   /**
   * Returns a dictionary mapping every unique word found in the given list
   * to their term frequency (TF) score.
   *
   * We are treating the list of words as if it were a document.
   */
   private IDictionary<String, Double> computeTfScores(IList<String> words) {
      IDictionary<String, Double> result = new ChainedHashDictionary<String, Double>();
      for (String word : words) {
         if (!result.containsKey(word)) {
            result.put(word, 1.0 / words.size());
         } else {
            double value = result.remove(word);
            result.put(word, value + 1.0 / words.size());
         }
      }
      return result;
   }
   
   
   /**
   * See spec for more details on what this method should do.
   */
   private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
      // Hint: this method should use the idfScores field and
      // call the computeTfScores(...) method.
      IDictionary<URI, IDictionary<String, Double>> result =
      new ChainedHashDictionary<URI, IDictionary<String, Double>>();
      for (Webpage page : pages) {
         IDictionary<String, Double> element = new ChainedHashDictionary<String, Double>();
         IList<String> wordList = page.getWords();
         IDictionary<String, Double> tfScore = computeTfScores(wordList);
         double sum = 0.0;
         for (String word : wordList) {
            if (!element.containsKey(word)) {
               double value = tfScore.get(word) * idfScores.get(word);
               element.put(word, value);
               sum += value * value;
            }
         }
         result.put(page.getUri(), element);
         docNorm.put(page.getUri(), sum);
         
      }
      
      
      return result;
   }
   
   /**
   * Returns the cosine similarity between the TF-IDF vector for the given query and the
   * URI's document.
   *
   * Precondition: the given uri must have been one of the uris within the list of
   *               webpages given to the constructor.
   */
   public Double computeRelevance(IList<String> query, URI pageUri) {
      IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
      IDictionary<String, Double> qrVector = new ChainedHashDictionary<String, Double>();
      IDictionary<String, Double> temp = computeTfScores(query);
      
      double qrNormValue = 0.0;
      double docNormValue = 0.0;
      for (String word : query) {
         if (temp.containsKey(word)) {
        	 	Double wordIdfScore = 0.0;
        	 	if (idfScores.containsKey(word)) {
        	 		wordIdfScore = idfScores.get(word);
        	 	} 
            double value = temp.get(word) * wordIdfScore;
            qrVector.put(word, value);
            qrNormValue += value * value;
         }
      }
      qrNormValue = Math.sqrt(qrNormValue);
      docNormValue = Math.sqrt(docNorm.get(pageUri));
      
      double numerator = 0.0;
      for (String word : query) {
         double docScore = 0.0;
         
         if (docVector.containsKey(word)) {
            docScore = docVector.get(word);
         }
         double qrScore = qrVector.get(word);
         numerator += docScore * qrScore;
      }
      
      double denominator = qrNormValue * docNormValue;
      if (denominator != 0) {
         return numerator / denominator;
      } else {
         return 0.0;
      }
      
      // TODO: Replace this with actual, working code.
      
      // TODO: The pseudocode we gave you is not very efficient. When implementing,
      // this method, you should:
      //
      // 1. Figure out what information can be precomputed in your constructor.
      //    Add a third field containing that information.
      //
      // 2. See if you can combine or merge one or more loops.
      
   }
}
