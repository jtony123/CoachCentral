package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utilities.AcuteChronicUpdater;
import utilities.CSVAppender;
import utilities.CSVLoader;
import utilities.CSVLoader2;
import utilities.CSVLoader3;
import utilities.CSVOutput;
import utilities.CSVTemplateGenerator;
//import play.db.jpa.*;
import views.html.*;
import views.html.Admin.*;
import models.Category;
import models.Player;
import models.User;
import play.data.FormFactory;
import play.i18n.Messages;
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
    	System.out.println("testing reload");
    	User user = User.findByEmail(session().get("connected"));
    	if(user != null) {
    		System.out.println("user found = "+user.email);
    		return CompletableFuture.completedFuture(redirect(routes.Application.dashboard(4, "All")));
    	} 
    	return CompletableFuture.completedFuture(ok(index.render()));
    }
    
    @Restrict({@Group({"admin"})})
    public CompletionStage<Result> admin() {
    	System.out.println("get admin called");
    	User user = User.findByEmail(session().get("connected"));
    	List<User> allusers = User.find.all();
    	return CompletableFuture.completedFuture(ok(users.render(user, "users", allusers)));
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
    
    // TODO: work out whether this needs to be restricted, as its only called directly by the page, not the user
    //@Restrict({@Group({"admin", "coach"})})
    public CompletionStage<Result> playerPhoto(Long playerId) {
    	System.out.println("playerPhoto called");
		final Player player = Player.find.byId(playerId);
    	return CompletableFuture.completedFuture(ok(player.playerPhoto).as("playerPhoto"));
    }
    
    
   public Result getCalendarCSV(){
    	
	    System.out.println("getCalendarCSV called");
	   	return ok(new java.io.File("data/attachments/Schedule1.csv"));
    }
    
    
   // TODO: implement some way of getting new calendar data into the calendar CSV file.
    public CompletionStage<Result> uploadCalender() {
    	
    	System.out.println("uploadCalender called");
    	String filepath = "data/attachments/GraphCSVFiles/";
    	//String filepath = "/tmp/";
    	
    	User user = User.findByEmail(session().get("connected"));
    	List<User> allusers = User.find.all();
    	
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> filename = body.getFile("filename");
        
    	return null;
    }
    
    
    public CompletionStage<Result> uploadCSV() {
    	
    	String filepath = "data/attachments/GraphCSVFiles/";
    	//String filepath = "/tmp/";
    	
    	User user = User.findByEmail(session().get("connected"));
    	List<User> allusers = User.find.all();
    	
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> filename = body.getFile("filename");
        String playroot = Play.application().path().getPath();
        System.out.println("uploadCSV called, root = "+playroot);
        
        if (filename != null) {
            String fileName = filename.getFilename();
            String contentType = filename.getContentType();
            File file = filename.getFile();
            

    		CSVLoader3 csvloader = new CSVLoader3();
    		csvloader.loadCSVFile(file.getAbsolutePath());

    		Map<String, ArrayList<String>> playerfiles = csvloader.getPlayerfilesbyname();
    		Iterator it = playerfiles.entrySet().iterator();
    		while (it.hasNext()) {
    			Map.Entry pair = (Map.Entry) it.next();
    			CSVOutput output = new CSVOutput();
    			
    			Player player = Player.findByPlayername((String) pair.getKey());
    			//player.filename = null;
    			// dumping the file into 'no players' if no play by this name found
    			if(player == null){
    				player =  Player.findByNumber(0);
    				//player.filename = null;
    			} else {
    				//player.filename = null;

        			if(player.filename == null){
        				// generate default file with headers
        				CSVTemplateGenerator cSVTemplateGenerator = new CSVTemplateGenerator();
            			player.filename = cSVTemplateGenerator.createFile(filepath, player.playername +"_"+player.playernumber+"_");
        			}
        			
        			
        			CSVAppender cSVAppender = new CSVAppender();
        			cSVAppender.updateFile(filepath + player.filename, (List<String>) pair.getValue());
        			
        			// here recalculate the acute, chronic loads
        			AcuteChronicUpdater acuteChronicUpdater = new AcuteChronicUpdater();
        			acuteChronicUpdater.loadCSVFile(filepath + player.filename);
        			
        			// write out the new file
        			CSVOutput cSVOutput = new CSVOutput();
        			String newFileName = cSVOutput.writeOutFile(filepath, player.filename, acuteChronicUpdater.getPlayerfileData());
        			player.filename = newFileName;
        			
        			
        			player.save();
    			}
    			

    		}


        	flash("success", "File uploaded successfully");
        	return CompletableFuture.completedFuture(ok(users.render(user, "users", allusers)));
            //return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return CompletableFuture.completedFuture(ok(users.render(user, "users", allusers)));
        }
    }
   
    

    public Result getCSV(Integer playernumber){
    	
    	System.out.println("getCSV called");
   	 Player player = Player.findByNumber(playernumber);//.find("byPlayernumber", playerNumber).first();
   	 System.out.println("player found = " + player.playername);
   	 System.out.println("filename is "+ player.filename);
   	String playroot = Play.application().path().getPath();
   	
   	//data/attachments/GraphCSVFiles/
   	 return ok(new java.io.File("data/attachments/GraphCSVFiles/" +player.filename));
   	 
   	 // production mode
   	//return ok(new java.io.File("/tmp/" +player.filename));
   	 
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
