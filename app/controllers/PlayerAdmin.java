package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
