package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.Category;
import models.Player;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.*;
import play.mvc.*;
import views.html.*;
import views.html.Admin.*;

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
	    	List<Player> players = user.getPlayersCategorisedWith(Category.findByName("All"));
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    
	    
	    public CompletionStage<Result> playerAdd() {
	    	System.out.println("get playerAdd called");

	    	return CompletableFuture.completedFuture(ok(playerAdd.render()));
	    	
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
	    	List<Player> players = user.getPlayersCategorisedWith(Category.findByName("All"));
	    	List<Category> categories = Category.find.all();

	    	return CompletableFuture.completedFuture(ok(playersList.render(user, "players", players, categories)));
	    	
	    }
	    
	    
	    
	    
	    public CompletionStage<Result> savePlayer() {
	    	System.out.println("savePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(playersList.render(null, null,  null, null)));
	    	
	    }
	    

	    
	    public CompletionStage<Result> deletePlayer() {
	    	System.out.println("deletePlayer called");
	    	
	    	Player player = formFactory.form(Player.class).bindFromRequest().get();
	    	
	    	System.out.println("player name retrieved = "+player.playername);
	    	
	    	player.dateadded = new Date();
	    	player.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	user.players.add(player);

	    	return CompletableFuture.completedFuture(ok(playersList.render(null, null, null, null)));
	    	
	    }
	    

}
