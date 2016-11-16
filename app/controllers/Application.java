package controllers;

import play.*;
import play.mvc.*;
//import play.db.jpa.*;
import views.html.*;
import models.User;
import play.data.FormFactory;
import play.libs.F;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.data.*;
import static play.data.Form.*;

import static play.libs.Json.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Application extends Controller {
	

	

    @Inject
    FormFactory formFactory;
    
    private final HttpExecutionContext ec;

    @Inject
    public Application(final HttpExecutionContext ec)
    {
        this.ec = ec;
    }
    
 
    
    public CompletionStage<Result> index() {
    	System.out.println("index called");
    	return CompletableFuture.completedFuture(ok(index.render()));
    }
    

    @Restrict({@Group({"foo"})})
    public CompletionStage<Result> dashboard() {
    	System.out.println("dashboard called");
    	User user = User.findByUserName(session().get("connected"));
    	
    	return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard")));
    	
    }
    
    
    
    
    @Restrict({@Group({"foo"})})
    public CompletionStage<Result> addUser() {
    	System.out.println("addUser called");
    	User user = formFactory.form(User.class).bindFromRequest().get();
        user.save();
        return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard")));
    }
   
    
    public CompletionStage<Result> getUsers() {
    	List<User> users = User.find.all();
        return CompletableFuture.completedFuture(ok(toJson(users)));
    }
    
}
