package controllers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Category;
import models.Player;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.*;
import play.mvc.*;
import views.html.*;
import views.html.Admin.*;


@Restrict({@Group({"admin"})})
public class PlayerAdmin extends Controller {
	
		String filepath = "data/attachments/GraphCSVFiles/";
		//String filepath = "/tmp/";
	
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
	    	User user = User.findByEmail(session().get("connected"));
	    	List<Player> players = Player.find.all();
	    	//List<Player> players = user.getPlayersCategorisedWith(Category.findByName("All"));
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    
	    
	    public CompletionStage<Result> playerAdd() {
	    	System.out.println("get playerAdd called");
	    	User user = User.findByEmail(session().get("connected"));
	    	

	    	return CompletableFuture.completedFuture(ok(playerAdd.render(user, "players")));
	    	
	    }
	    
	    public CompletionStage<Result> editPlayer(Integer playernumber) {
	    	System.out.println("get playerEdit called");
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	Player player = Player.findByNumber(playernumber);
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playerEdit.render(user, "players", player, categories)));
	    	
	    }
	    
	    
	    //GET page
	    public CompletionStage<Result> editPlayerData(Integer playernumber) {
	    	System.out.println("get editPlayerData called");
	    	
	    	//String filepath = "data/attachments/GraphCSVFiles/";
	    	Player player = Player.findByNumber(playernumber);
	    	String fileName = filepath + player.filename;
	    	List<String> headerstrings = new ArrayList<String>();
	    	List<List<String>> datalines = new ArrayList<List<String>>();
	    	
	    		BufferedReader fileReader = null;
	    		String header;
	    		String line = "";
	    		String newLine = "";

	    		try {
	    			fileReader = new BufferedReader(new FileReader(fileName));

	    			// this next line should contain the column headings
	    			header = fileReader.readLine();

	    			// split the line into an array
	    			String[] headertokens = header.split(",");

	    			headerstrings = Arrays.asList(headertokens);
	    			int timeindex = headerstrings.indexOf("SESS_START");

	    			// Read the file line by line

	    			while ((line = fileReader.readLine()) != null) {
	    				
	    				// split the line into string tokens
	    				String[] tokens = line.split(",");
	    				String time = tokens[timeindex];
	    				Long timestamp = Long.parseLong(time) *1000;
	    				
	    				DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm");
	    				
	    				Date date = new Date(timestamp);
	    				tokens[timeindex] = df.format(date);

	    				List<String> dataline = new ArrayList<String>();
	    				for(String s : tokens){
	    					
	    					
	    					dataline.add(s);
	    				}
	    				datalines.add(dataline);
	    			} // end while loop reading lines
	    			

	    			fileReader.close();
	    		} catch (IOException e) {
	    			// TODO return an error to the user
	    			e.printStackTrace();
	    		}
	    		
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playerDataEdit.render(user, "players", player, categories, headerstrings, datalines)));
	    	
	    }
	    
	   
	  //POST page
	    public CompletionStage<Result> updatePlayerData(Integer playernumber) {
	    
	    	System.out.println("post updatePlayerData called");
	    	
	    	// TODO:	grab whatever was changed in the form and update the file
	    	
	    	//String filepath = "data/attachments/GraphCSVFiles/";
	    	//String filepath = "/tmp/";
	    	
	    	
	    	Player player = Player.findByNumber(playernumber);
	    	String fileName = filepath + player.filename;
	    	List<String> headerstrings = new ArrayList<String>();
	    	List<List<String>> datalines = new ArrayList<List<String>>();
	    	
	    		BufferedReader fileReader = null;
	    		String header;
	    		String line = "";
	    		String newLine = "";

	    		try {
	    			fileReader = new BufferedReader(new FileReader(fileName));

	    			// this next line should contain the column headings
	    			header = fileReader.readLine();

	    			// split the line into an array
	    			String[] headertokens = header.split(",");

	    			headerstrings = Arrays.asList(headertokens);
	    			int timeindex = headerstrings.indexOf("SESS_START");

	    			// Read the file line by line

	    			while ((line = fileReader.readLine()) != null) {
	    				
	    				// split the line into string tokens
	    				String[] tokens = line.split(",");
	    				String time = tokens[timeindex];
	    				Long timestamp = Long.parseLong(time) *1000;
	    				
	    				DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm");
	    				
	    				Date date = new Date(timestamp);
	    				tokens[timeindex] = df.format(date);

	    				List<String> dataline = new ArrayList<String>();
	    				for(String s : tokens){
	    					
	    					
	    					dataline.add(s);
	    				}
	    				datalines.add(dataline);
	    			} // end while loop reading lines
	    			

	    			fileReader.close();
	    		} catch (IOException e) {
	    			// TODO return an error to the user
	    			e.printStackTrace();
	    		}
	    		
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	
	    	List<Category> categories = Category.find.all();
	    	
	    	return CompletableFuture.completedFuture(ok(playerDataEdit.render(user, "players", player, categories, headerstrings, datalines)));
	    }
	    
	    public CompletionStage<Result> addPlayerToCategory(Integer playernumber, String cat) {
	    	
	    	
	    	System.out.println("get addCategoryToPlayer called with " + playernumber + " and " + cat);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	Player player = Player.findByNumber(playernumber);
	    	player.addToCategory(cat);
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playerEdit.render(user, "players", player, categories)));
	    	
	    	
	    }
	    
	    public CompletionStage<Result> removePlayerFromCategory(Integer playernumber, String cat) {
	    	
	    	
	    	System.out.println("get removePlayerFromCategory called with " + playernumber + " and " + cat);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	Player player = Player.findByNumber(playernumber);
	    	player.removeFromCategory(cat);
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playerEdit.render(user, "players", player, categories)));
	    	
	    	
	    }
	    
	    
	    
	    public CompletionStage<Result> updatePlayer(Integer playernumber) {
	    	System.out.println("updatePlayer called");
	    	
	    	DynamicForm form = Form.form().bindFromRequest();
	    	
	    	Player player = Player.findByNumber(playernumber);
	    	player.playername = form.get("playername");
	    	player.playernumber = Integer.parseInt(form.get("playernumber"));
	    	player.height = form.get("height");
	    	player.weight = form.get("weight");
	    	player.dob = form.get("dob");
	    	
	        MultipartFormData<File> body = request().body().asMultipartFormData();
	        FilePart<File> filename = body.getFile("playerPhoto");
	        
	        if (filename != null) {
	        	System.out.println("filename not null");
	            String fileName = filename.getFilename();
	            String contentType = filename.getContentType();
	            File file = filename.getFile();
	            System.out.println("size = "+file.length());
	            if(file.length() > 0){
	            	 player.playerPhotofilename = fileName;
	 	            player.playerPhoto = new byte[(int)file.length()];
	 	            
	 	            InputStream inStream = null;
	 	            try {
	 	                inStream = new BufferedInputStream(new FileInputStream(file));
	 	                inStream.read(player.playerPhoto);
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            } finally {
	 	                if (inStream != null) {
	 	                    try {
	 	                        inStream.close();
	 	                    } catch (IOException e) {
	 	                        e.printStackTrace();
	 	                    }
	 	                }
	 	            }
	            }
	        }
	        player.save();   
	            
	    	User user = User.findByEmail(session().get("connected"));
	    	List<Player> players = Player.find.all();
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    
	    
	    
	    
	    public CompletionStage<Result> savePlayer() {
	    	System.out.println("savePlayer called");
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	DynamicForm form = Form.form().bindFromRequest();
	    	String name = form.get("playername");
	    	String number = form.get("playernumber");
	    	Integer anumber;
	    	
	    	System.out.println(name +" -- "+number);
	    	
	    	if(number == null || number.equalsIgnoreCase("")){
	    		// automatic number generation
	    		Player p = Player.find.select("playernumber").orderBy().desc("playernumber").findList().get(0);
	    		anumber = ++p.playernumber;
	    		
	    	} else {
	    		anumber = Integer.parseInt(form.get("playernumber"));
	    	}
	    	
	    	if(Player.findByNumber(anumber) != null){
	    		flash("error", "Number already used, must be unique.  Leave number blank if you want it assigned automatically");
	    		return CompletableFuture.completedFuture(ok(playerAdd.render(user, "players")));
	    	}
	    	
	    	Player player = new Player(name, anumber, null, user);
//	    	player.playername = form.get("playername");
//	    	player.playernumber = Integer.parseInt(form.get("playernumber"));
	    	
	        MultipartFormData<File> body = request().body().asMultipartFormData();
	        FilePart<File> filename = body.getFile("playerPhoto");
	        
	        if (filename != null) {
	        	System.out.println("filename not null");
	            String fileName = filename.getFilename();
	            String contentType = filename.getContentType();
	            File file = filename.getFile();
	            System.out.println("size = "+file.length());
	            if(file.length() > 0){
	            	 player.playerPhotofilename = fileName;
	 	            player.playerPhoto = new byte[(int)file.length()];
	 	            
	 	            InputStream inStream = null;
	 	            try {
	 	                inStream = new BufferedInputStream(new FileInputStream(file));
	 	                inStream.read(player.playerPhoto);
	 	            } catch (IOException e) {
	 	                e.printStackTrace();
	 	            } finally {
	 	                if (inStream != null) {
	 	                    try {
	 	                        inStream.close();
	 	                    } catch (IOException e) {
	 	                        e.printStackTrace();
	 	                    }
	 	                }
	 	            }
	            }
	        }
	        player.save();
	        user.addPlayer(anumber);
	        
	        List<Player> players = Player.find.all();
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    

	    
	    public CompletionStage<Result> deletePlayer(Integer playernumber) {
	    	System.out.println("deletePlayer called");
	    	
	    	Player player = Player.findByNumber(playernumber);
	    	Set<Category> cats = player.getCategories();
	    	for(Category c :cats){
	    		player.removeFromCategory(c.name);
	    		System.out.println("removing "+c.name);
	    	}
	    	
	    	for(User u : player.getUsers()){
	    		System.out.println("removing " + u.email);
	    		u.getPlayers().remove(player);
	    	}
	    	
	    	
	    	
	    	
	    	player.delete();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	List<Player> players = Player.find.all();
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    

}
