package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Subject;
import models.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

	
    @Inject
    FormFactory formFactory;
    
	public MyDeadboltHandler(ExecutionContextProvider ecProvider) {
		super(ecProvider);
		
		System.out.println("deadbolt const called");
		// TODO Auto-generated constructor stub
	}
	
    public CompletionStage<Optional<Result>> beforeAuthCheck(final Http.Context context)
    {
    	System.out.println("deadbolt beforeAuthCheck called");
    	//System.out.println("user details = " + user.userName);
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return CompletableFuture.completedFuture(Optional.empty());
    }

    public CompletionStage<Optional<? extends Subject>> getSubject(final Http.Context context)
    {
    	
    	System.out.println("deadbolt getSubject const called");
    	System.out.println("User is " + context.session().get("connected"));
        // in a real application, the user name would probably be in the session following a login process
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(User.findByUserName(context.session().get("connected"))),
                                             (Executor) executionContextProvider.get());
    }
//
//    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(final Http.Context context)
//    {
//        return CompletableFuture.completedFuture(Optional.of(new MyDynamicResourceHandler()));
//    }
//
//    @Override
//    public CompletionStage<Result> onAuthFailure(final Http.Context context,
//                                                 final Optional<String> content)
//    {
//        // you can return any result from here - forbidden, etc
//        return CompletableFuture.completedFuture(ok(accessFailed.render()));
//    }

}
