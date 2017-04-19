package controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.RandRQueryRequest;

import utilities.WatsonDocument;

public class WatsonControl extends Controller {
	
	
//public CompletionStage<Result>AskWatson()
//	
//	{
//		List<docstructure> WatsonResponse=null;
//	    try {
//		
//		DynamicForm requestData = Form.form().bindFromRequest();
//    	String querytext = requestData.get("querytext");
//    	System.out.println(querytext);
//		RetrieveAndRankJava RandRobj=new RetrieveAndRankJava();
//		
//		WatsonResponse=RandRobj.searchAllDocs(querytext);
//	
//		System.out.print(WatsonResponse.get(0).getBodytext());
//		//return ok(views.html.AskWatson.scala.html.render(WatsonResponse));
//	    }
//	    catch (Exception e)
//	    {
//	    	System.out.println(e.getMessage());
//	    }
//	    return ok(views.html.AskWatson.render(WatsonResponse));
//	    
//	    
//	}

}
