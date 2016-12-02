package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Subject;
import controllers.routes;
import models.User;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

	
    @Inject
    FormFactory formFactory;
    
    
    @Inject
	public MyDeadboltHandler(ExecutionContextProvider ecProvider) {
		super(ecProvider);
		
		System.out.println("deadbolt const called");
		
		// TODO Auto-generated constructor stub
	}
	
    public CompletionStage<Optional<Result>> beforeAuthCheck(final Http.Context context)
    {
    	//System.out.println("deadbolt beforeAuthCheck called");
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return CompletableFuture.completedFuture(Optional.empty());
    }

    public CompletionStage<Optional<? extends Subject>> getSubject(final Http.Context context)
    {
    	//System.out.println("deadbolt getSubject called with user " + context.session().get("connected"));
    	//System.out.println("User is " + context.session().get("connected"));
    	String previousTick = context.session().get("sessiontime");
    	
    	
    	if(previousTick != null && !previousTick.equals("")){
    		
    		Long previousT = Long.valueOf(previousTick);
    		Long currentT = new Date().getTime();
    		
    		// TODO: move this parameter into the config file
    		// use the longer timeout when developing
    		Long timeout = 3000000L;
    		//Long timeout = 300000L;
    		if((currentT - previousT) > timeout){
    			//System.out.println("user timed out");
    			// the user has not made a request within the timeout period, hence clearing the session
    			// on the server side.
    			context.session().clear();
    		} else {
    			// resetting the server side timeout between requests timer
	    		context.session().replace("sessiontime", Long.toString(new Date().getTime()));
    		}
    		
    	}
    	
        // fallthru' = everthing ok
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(User.findByEmail(context.session().get("connected"))),
                                             (Executor) executionContextProvider.get());
    }

    
    
//    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(final Http.Context context)
//    {
//        return CompletableFuture.completedFuture(Optional.of(new MyDynamicResourceHandler()));
//    }

    
    
    @Override
    public CompletionStage<Result> onAuthFailure(final Http.Context context,
                                                 final Optional<String> content)
    {
        // redirects to the index page if authentication fails at any point
    	// TODO: only redirect back to index page if timed out, otherwise send flash with 
    	// unauthorised access back to the user
    	System.out.println("onAuthFailure called");
    	context.flash().put("error", Messages.get("login.inactive"));
        return CompletableFuture.completedFuture(redirect(routes.Application.index()));
    }

}
