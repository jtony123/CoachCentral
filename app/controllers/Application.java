package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utilities.CSVLoader;
import utilities.CSVOutput;
//import play.db.jpa.*;
import views.html.*;
import views.html.Admin.*;
import models.Category;
import models.Player;
import models.User;
import play.data.FormFactory;
import play.libs.F;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    

    @Restrict({@Group({"coach"})})
    public CompletionStage<Result> dashboard(int playernumber, String category) {
    	
    	
    	System.out.println("dashboard called");
    	System.out.println("category passed in : " + category);
    	
    	User user = User.findByEmail(session().get("connected"));
    	Category categoryFound = Category.findByName(category);
    	Player player;
    	List<Player> players;
    	
    	if(categoryFound != null){
    		System.out.println("cat not null");
    		players = user.getPlayersCategorisedWith(categoryFound);
    		
    		// check for empty category for this user
    		if(players == null || players.isEmpty()){
    		//empty category, return 'no players'
    			System.out.println("players is null, sending no players");
    			player = Player.findByPlayername("no players");
    			players.add(player);
    		} else {
    		// the category is not empty
    			System.out.println("players not null");
    	    	if (playernumber == 0) {
    	    		// to handle initial call
    	    		System.out.println("handle initial call, sending player 0");
    	    		player = players.get(0);
    	    	} else {
    	    		player = Player.findByNumber(playernumber);
    	    		System.out.println("finding player by number");
    	    		
    	    		// check first if the player is null(out of range)
    	    		if(player == null) {
    	    			player = players.get(0);
    	    		} else {
    	    			// check if the player that the user had highlighted is in this category
        	    		if (!player.categories.contains(categoryFound)){
        	    			System.out.println("player not in this category, sending player 0");
        	    			player = players.get(0);
        	    		}
    	    		}
    	    		
    	    		 
    	    	}
    		}
    		
    	} else {
    		System.out.println("cat is null");
    		players = user.getPlayersCategorisedWith(Category.findByName("All"));
    		player = players.get(0);
    		category = "All";
    	}
    	
    	List<Category> categories = Category.find.all();
    	System.out.println("player count = " + players.size());
    	int playerIndex = players.indexOf(player);
    	System.out.println("......");
    			
    	return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard", player, playerIndex, players, category, categories)));
    	
    }
    
    @Restrict({@Group({"coach"})})
    public CompletionStage<Result> record(int playernumber, String category) {
    	System.out.println("record called");
    	User user = User.findByEmail(session().get("connected"));
    	Category categoryFound = Category.findByName(category);
    	List<Player> players = user.getPlayersCategorisedWith(categoryFound);
    	
    	Player player = Player.findByNumber(playernumber);
    	if(player == null) {
    		player = Player.find.byId((long) 1);
    	}
    	
    	List<Category> categories = Category.find.all();
    	System.out.println("player count = " + players.size());
    	int playerIndex = players.indexOf(player);
    	
    	return CompletableFuture.completedFuture(ok(dashboard.render(user, "dashboard", player, playerIndex, players, category, categories)));
    	
    }
    
    @Restrict({@Group({"admin", "coach"})})
    public CompletionStage<Result> playerPhoto(Long playerId) {
    	System.out.println("dashboard called");

    	return CompletableFuture.completedFuture(ok(dashboard.render(null, "dashboard", null, null, null, null, null)));
    	
    }
    
    
    public CompletionStage<Result> loadCSV() {
    	System.out.println("loadCSV called");

    	return CompletableFuture.completedFuture(ok(getCSV.render()));
    	
    }
    
    
    public Result uploadCSV() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> filename = body.getFile("filename");
        String playroot = Play.application().path().getPath();
        System.out.println("uploadCSV called, root = "+playroot);
        
        if (filename != null) {
            String fileName = filename.getFilename();
            String contentType = filename.getContentType();
            File file = filename.getFile();
            

    		CSVLoader csvloader = new CSVLoader();
    		csvloader.loadCSVFile(file.getAbsolutePath());

    		Map<Integer, ArrayList<String>> playerfiles = csvloader.getPlayerfiles();
    		Iterator it = playerfiles.entrySet().iterator();
    		while (it.hasNext()) {
    			Map.Entry pair = (Map.Entry) it.next();
    			CSVOutput output = new CSVOutput();
    			
    			

    			Player player = Player.findByNumber((Integer) pair.getKey());
    			if(player == null){
    				player =  Player.findByNumber(0);
    			}
    			
    			System.out.println("writing out " + player.playername);
    			
    			player.filename = output.writeOutFile("/tmp/",
    					"player" + player.playernumber + "_1", csvloader.getHeader(), (List<String>) pair.getValue());

    			player.save();
    		}
    		
    		
            
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }
   
    

    public Result getCSV(Integer playernumber){
    	
    	System.out.println("getCSV called");
   	 Player player = Player.findByNumber(playernumber);//.find("byPlayernumber", playerNumber).first();
   	 System.out.println("player found = " + player.playername);
   	String playroot = Play.application().path().getPath();
   	
   	 
   	 return ok(new java.io.File("/tmp/" +player.filename));
   	 
    }
    
    
    
    
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
