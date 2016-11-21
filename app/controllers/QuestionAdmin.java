
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

public class QuestionAdmin extends Controller {
	
	
	   @Inject
	    FormFactory formFactory;
	    
	    private final HttpExecutionContext ec;

	    @Inject
	    public QuestionAdmin(final HttpExecutionContext ec)
	    {
	        this.ec = ec;
	    }
	    
	    
	    public CompletionStage<Result> questionsList() {
	    	System.out.println("get playersList called");

	    	return CompletableFuture.completedFuture(ok(questionsList.render()));
	    	
	    }
	    
	    
	    public CompletionStage<Result> questionAdd() {
	    	System.out.println("get playerAdd called");

	    	return CompletableFuture.completedFuture(ok(questionAdd.render()));
	    	
	    }
	    
	    public CompletionStage<Result> questionSave() {
	    	System.out.println("savePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(questionsList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> questionUpdate() {
	    	System.out.println("updatePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(questionsList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> questionDelete() {
	    	System.out.println("deletePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(playersList.render()));
	    	
	    }
	    

}

