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

public class PlayerAdmin extends Controller {
	
	
	   @Inject
	    FormFactory formFactory;
	    
	    private final HttpExecutionContext ec;

	    @Inject
	    public PlayerAdmin(final HttpExecutionContext ec)
	    {
	        this.ec = ec;
	    }
	    

	    
	    public CompletionStage<Result> playersList() {
	    	System.out.println("get playersList called");

	    	return CompletableFuture.completedFuture(ok(playersList.render()));
	    	
	    }
	    
	    
	    public CompletionStage<Result> playerAdd() {
	    	System.out.println("get playerAdd called");

	    	return CompletableFuture.completedFuture(ok(playerAdd.render()));
	    	
	    }
	    
	    public CompletionStage<Result> savePlayer() {
	    	System.out.println("savePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(playersList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> updatePlayer() {
	    	System.out.println("updatePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(playersList.render()));
	    	
	    }
	    
	    public CompletionStage<Result> deletePlayer() {
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
