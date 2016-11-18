package controllers;

import play.*;
import play.mvc.*;
//import play.db.jpa.*;
import views.html.*;
import models.Category;
import models.Player;
import models.User;
import play.data.FormFactory;
import play.libs.F;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;

import java.util.Date;
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
    
    public CompletionStage<Result> admin() {
    	System.out.println("get admin called");

    	return CompletableFuture.completedFuture(ok(admin.render()));
    }
    

    @Restrict({@Group({"admin", "coach"})})
    public CompletionStage<Result> dashboard(int playernumber, String category) {
    	System.out.println("dashboard called");
    	User user = User.findByEmail(session().get("connected"));
    	Category categoryFound = Category.findByName("All");
    	List<Player> players = user.getPlayersCategorisedWith(categoryFound);
    	
    	//List<Player> players = Player.find.all();
    	Player player = Player.find.byId((long) 1);
    	return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard", player, players, category)));
    	
    }
    
    @Restrict({@Group({"admin", "coach"})})
    public CompletionStage<Result> record(int playernumber, String category) {
    	System.out.println("dashboard called");
    	User user = User.findByEmail(session().get("connected"));
    	Category categoryFound = Category.findByName(category);
    	List<Player> players = user.getPlayersCategorisedWith(categoryFound);
    	Player player = Player.find.byId((long) 1);
    	return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard", player, players, category)));
    	
    }
    
    @Restrict({@Group({"admin", "coach"})})
    public CompletionStage<Result> playerPhoto(Long playerId) {
    	System.out.println("dashboard called");

    	return CompletableFuture.completedFuture(ok(dashboard.render(null, "dashboard", null, null, null)));
    	
    }
    
    //@Restrict({@Group({"admin", "coach"})})
//    public CompletionStage<Result> admin() {
//    	System.out.println("get admin called");
//
//    	return CompletableFuture.completedFuture(ok(admin.render()));
//    	
//    }
//    
//    public CompletionStage<Result> savePlayer() {
//    	System.out.println("savePlayer called");
//    	
//    	Player player = formFactory.form(Player.class).bindFromRequest().get();
//    	
//    	System.out.println("player name retrieved = "+player.playername);
//    	
//    	player.dateadded = new Date();
//    	player.save();
//    	
//    	User user = User.findByEmail(session().get("connected"));
//    	user.players.add(player);
//
//    	return CompletableFuture.completedFuture(ok(admin.render()));
//    	
//    }
    
    
    
    
//    @Restrict({@Group({"advanced"})})
//    public CompletionStage<Result> addUser() {
//    	System.out.println("addUser called");
//    	User user = formFactory.form(User.class).bindFromRequest().get();
//        user.save();
//        return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard")));
//    }
//   
//    
//    public CompletionStage<Result> getUsers() {
//    	List<User> users = User.find.all();
//        return CompletableFuture.completedFuture(ok(toJson(users)));
//    }
    
}
