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

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Category;
import models.Player;
import models.SecurityRole;
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

@Restrict({@Group({"admin"})})
public class UserAdmin extends Controller {
	
	
	   @Inject
	    FormFactory formFactory;
	    
	    private final HttpExecutionContext ec;

	    @Inject
	    public UserAdmin(final HttpExecutionContext ec)
	    {
	        this.ec = ec;
	    }
	    
	    
	   
	    public CompletionStage<Result> getUsers() {
	    	System.out.println("get admin called");
	    	User user = User.findByEmail(session().get("connected"));
	    	List<User> allusers = User.find.all();
	    	Player player = Player.findByNumber(1);

	    	return CompletableFuture.completedFuture(ok(users.render(user, "users", player, allusers)));
	    }
	    
	    public CompletionStage<Result> newUser(){
	    	System.out.println("get newUser called");
	    	User user = User.findByEmail(session().get("connected"));
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	
	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userAdd.render(user, "users", player, roles)));
	    	
	    }
	    
	    public CompletionStage<Result> addUser(){
	    	
	    	System.out.println("addUser called");
	    	
	    	DynamicForm form = Form.form().bindFromRequest();
	    	
	    	System.out.println("email got "+form.get("useremail"));
	    	System.out.println("pass got "+form.get("password"));
	    	
	    	User newUser = new User();
	    	newUser.email = form.get("useremail");
	    	newUser.userName = form.get("username");
	    	newUser.setPassword(form.get("password"));
	    	newUser.save();
	    	
	            
	    	User user = User.findByEmail(session().get("connected"));
	    	List<User> allusers = User.find.all();
	    	
	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(users.render(user, "users", player, allusers)));
	    	
	    }
	    
	    
	    
	    
	    @Restrict({@Group({"admin"})})
	    public CompletionStage<Result> editUser(String useremail) {
	    	System.out.println("get admin called");
	    	User user = User.findByEmail(session().get("connected"));
	    
	    	User thisuser = User.findByEmail(useremail);
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	List<Player> players = Player.find.all();
	    			
	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userEdit.render(user, "users", player, thisuser, roles, players)));
	    }
	    
	    public CompletionStage<Result> updateUser(String email){
	    	
	    	System.out.println("updateUser called");
	    	
	    	DynamicForm form = Form.form().bindFromRequest();
	    	
	    	User thisuser = User.findByEmail(email);
	    	thisuser.email = form.get("useremail");
	    	thisuser.userName = form.get("username");
	    	thisuser.save();   
	            
	    	User user = User.findByEmail(session().get("connected"));
	    	List<User> allusers = User.find.all();
	    	
	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(users.render(user, "users", player, allusers)));
	    	
	    }
	    
	    public CompletionStage<Result> addRoleToUser(String useremail, String role) {
	    	
	    	
	    	System.out.println("get addRoleToUser called with " + useremail + " and " + role);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	User thisuser = User.findByEmail(useremail);
	    	thisuser.addRole(role);
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	List<Player> players = Player.find.all();

	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userEdit.render(user, "users", player, thisuser, roles, players)));
	    	
	    	
	    }
	    
	    public CompletionStage<Result> removeRoleFromUser(String useremail, String role) {
	    	
	    	
	    	System.out.println("get removeRoleFromUser called with " + useremail + " and " + role);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	User thisuser = User.findByEmail(useremail);
	    	thisuser.removeRole(role);
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	List<Player> players = Player.find.all();

	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userEdit.render(user, "users", player, thisuser, roles, players)));
	    	
	    	
	    }
	     
	    public CompletionStage<Result> addPlayerToUser(String useremail, Integer playernumber) {
	    	
	    	
	    	System.out.println("get addPlayerToUser called with " + useremail + " and " + playernumber);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	User thisuser = User.findByEmail(useremail);
	    	thisuser.addPlayer(playernumber);
	    	
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	List<Player> players = Player.find.all();

	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userEdit.render(user, "users", player, thisuser, roles, players)));
	    	
	    	
	    }
	    
	    public CompletionStage<Result> removePlayerFromUser(String useremail, Integer playernumber) {
	    	
	    	
	    	System.out.println("get removePlayerFromUser called with " + useremail + " and " + playernumber);
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	User thisuser = User.findByEmail(useremail);
	    	thisuser.removePlayer(playernumber);
	    	
	    	List<SecurityRole> roles = SecurityRole.find.all();
	    	List<Player> players = Player.find.all();

	    	Player player = Player.findByNumber(1);
	    	return CompletableFuture.completedFuture(ok(userEdit.render(user, "users", player, thisuser, roles, players)));
	    	
	    	
	    }
	    
	    
}
