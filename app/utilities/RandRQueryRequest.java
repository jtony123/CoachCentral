package utilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;



import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
//import org.apache.tika.Tika;
//import org.apache.tika.exception.TikaException;
//import org.apache.tika.metadata.Metadata;
//import org.apache.tika.parser.AutoDetectParser;
//import org.apache.tika.parser.ParseContext;
//import org.apache.tika.parser.Parser;
//import org.apache.tika.parser.txt.TXTParser;
//import org.apache.tika.sax.BodyContentHandler;
//import org.apache.tika.sax.ExpandedTitleContentHandler;


import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


import com.google.common.io.Files;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranking;

public class RandRQueryRequest {

  private  static  String USERNAME ;
  private static   String PASSWORD ;
  private static   String SOLR_CLUSTER_ID ;
  private static   String RANKER_ID;
  
  /**
   * The name of the collection to create, index data into, and search.
   */
  private static  String COLLECTION_NAME;

  private static HttpSolrClient solrClient;
  private static RetrieveAndRank service;

 public  RandRQueryRequest()
  {		
	 	// the standard plan instance
	  	USERNAME = "dcac166b-e514-4b6b-8977-d2d420ad1ffd";
	  	PASSWORD = "E2bLz1K5fpIz";
	  	SOLR_CLUSTER_ID = "sc7e7fe48f_1a9c_454e_a661_82a21767817b";	  	
	  	RANKER_ID="81aacex30-rank-1347";
	  	COLLECTION_NAME="CorpusProduction";
	 
	 
	 
	  	// the old eco (free) plan
//	  	USERNAME = "35100b2b-5803-4483-8682-b96c0ccd96e2";
//	  	PASSWORD = "KhY2DSfn2gsu";
//	  	SOLR_CLUSTER_ID = "sc3974150c_5830_4f58_8772_2d27667c96a3";	  	
//	  	RANKER_ID="c852c8x19-rank-1978"; // This is the ranker paul created from manual training.
//	  	COLLECTION_NAME="test_collection";
	 
	  	
	  	service = new RetrieveAndRank();
	  	service.setUsernameAndPassword(USERNAME, PASSWORD);
	  	solrClient = getSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), USERNAME, PASSWORD);
	  	
  }

  public  List<WatsonDocument> searchAllDocs(String textquery) throws IOException {    
    
	  
	  List<WatsonDocument> list=new ArrayList<WatsonDocument>();
    
    try {
    	final SolrQuery query = new SolrQuery(textquery);
    	query.setHighlight(true);
    	query.addHighlightField("searchText");
    	query.setHighlightSnippets(3);
    	query.setRows(10);
    	query.setRequestHandler("/fcselect");
    	query.set("ranker_id", RANKER_ID);
    	
    	final QueryResponse response = solrClient.query(COLLECTION_NAME, query);
    	SolrDocumentList newresults ;
    	newresults =  response.getResults();
    	

    	for(int i=0;i<newresults.size();i++)
    	{
    		SolrDocument hitDoc=newresults.get(i);
    		Double confid = (Double) hitDoc.getFieldValue("ranker.confidence");
    		
    		if(confid > 0.01) {
    			WatsonDocument docnode=new WatsonDocument();
        		docnode.setId(hitDoc.getFieldValue("id").toString());
        		docnode.setBodytext(hitDoc.getFieldValue("contentHtml").toString().replace("<p dir=\"ltr\"> </p>", "")); //body contains the text response.
        		list.add(docnode);
    		}
    	}

    	return  list;
    } catch (final SolrServerException e) {
      throw new RuntimeException("Failed to search!", e);
    }
   
  }



  private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
    public void process(final HttpRequest request, final HttpContext context) throws HttpException {
      final AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

      if (authState.getAuthScheme() == null) {
        final CredentialsProvider credsProvider = (CredentialsProvider) context
            .getAttribute(HttpClientContext.CREDS_PROVIDER);
        final HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
        final Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
            targetHost.getPort()));
        if (creds == null) {
          throw new HttpException("No creds provided for preemptive auth.");
        }
        authState.update(new BasicScheme(), creds);
      }
    }
  }
  private static HttpSolrClient getSolrClient(String uri, String username, String password) {
	    return new HttpSolrClient(service.getSolrUrl(SOLR_CLUSTER_ID), createHttpClient(uri, username, password));
	  }
   private static HttpClient createHttpClient(String uri, String username, String password) {
	    final URI scopeUri = URI.create(uri);

	    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	    credentialsProvider.setCredentials(new AuthScope(scopeUri.getHost(), scopeUri.getPort()),
	        new UsernamePasswordCredentials(username, password));

	    final HttpClientBuilder builder = HttpClientBuilder.create()
	        .setMaxConnTotal(128)
	        .setMaxConnPerRoute(32)
	        .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT).setRedirectsEnabled(true).build())
	        .setDefaultCredentialsProvider(credentialsProvider)
	        .addInterceptorFirst(new PreemptiveAuthInterceptor());
	    return builder.build();
	  }
}
