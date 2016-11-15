package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.User;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class AccessControl extends Controller {
	
	
	 @Inject
	 FormFactory formFactory;
	

    public CompletionStage<Result> login() {
    	System.out.println("login called");
    	User user = formFactory.form(User.class).bindFromRequest().get();
    	User userDB;
    	
    	if(user.userName != null){
    		
    		if (user.password != null) {
    			
    			userDB = User.findByUserName(user.userName);
    			
    			if(userDB != null){
    		    	if(userDB.password.equalsIgnoreCase(user.password)){
    		    		System.out.println("authenticated user");
    		    		session("connected", userDB.userName);
    		    		
    		    	} else {
    		    		System.out.println("password mismatch");
    		    		// return to user with message about mismatched password
    		    		flash("success", "Incorrect password for this username");
    		    		return CompletableFuture.completedFuture(ok(index.render()));
    		    	}
    			} else {
    				// user not in database
    				System.out.println("user not in db");
		    		flash("success", "No details for this username found");
		    		return CompletableFuture.completedFuture(ok(index.render()));
    				
    			}
    			
    			

	    	
    		} else {
    			// return to user requesting password to be entered
    			System.out.println("password not entered");
    			return CompletableFuture.completedFuture(ok(index.render()));
    		}
    	} else {
    		//return to user asking for username
    		System.out.println("username not entered");
    		return CompletableFuture.completedFuture(ok(index.render()));
    	}
    	
    	System.out.println("user logging in = "+user.userName + " - " + user.password);
    	System.out.println("user details saved = "+userDB.userName + " - " + userDB.password);
    	
    	return CompletableFuture.completedFuture(redirect(routes.Application.dashboard()));
    	
    }

}
