
package controllers;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.Player;
import models.User;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.*;
import play.mvc.*;
import views.html.*;
import views.html.Admin.*;

public class CategoryAdmin extends Controller {
	
	
	   @Inject
	    FormFactory formFactory;
	    
	    private final HttpExecutionContext ec;

	    @Inject
	    public CategoryAdmin(final HttpExecutionContext ec)
	    {
	        this.ec = ec;
	    }
	    
	    
	    public CompletionStage<Result> categorysList() {
	    	System.out.println("get answersList called");

	    	return CompletableFuture.completedFuture(ok(categorysList.render()));
	    	
	    }
	    
	    
	    public CompletionStage<Result> categoryAdd() {
	    	System.out.println("get answerAdd called");

	    	return CompletableFuture.completedFuture(ok(categoryAdd.render()));
	    	
	    }
	    
	    public CompletionStage<Result> categorySave() {
	    	System.out.println("answerSave called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(categorysList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> categoryUpdate() {
	    	System.out.println("answerUpdate called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(categorysList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> categoryDelete() {
	    	System.out.println("answerDelete called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(categorysList.render()));
	    	
	    }
	    

}



