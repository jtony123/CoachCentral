
package controllers;

import java.util.Date;
import java.util.List;
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
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.*;
import play.mvc.*;
import views.html.*;
import views.html.Admin.*;

@Restrict({@Group({"admin"})})
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
	    	System.out.println("get categorysList called");
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	List<Category> categories = Category.find.all();
	    	
	    	Player player = Player.findByNumber(1);

	    	return CompletableFuture.completedFuture(ok(categorysList.render(user, "playercategories", player, categories)));

	    	
	    }
	    
	    public CompletionStage<Result> categoryAdd() {
	    	System.out.println("categoryAdd called");
	    	
	    	DynamicForm form = Form.form().bindFromRequest();
	    	
	    	Category category = Category.findOrCreateByName(form.get("categoryname"));
	    	category.save();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	List<Category> categories = Category.find.all();
	    	
	    	Player player = Player.findByNumber(1);

	    	return CompletableFuture.completedFuture(ok(categorysList.render(user, "playercategories",  player, categories)));
	    	
	    }
	    
	    
	    public CompletionStage<Result> categoryDelete(String catname) {
	    	System.out.println("categoryDelete called");
	    	
	    	
	    	Category category = Category.findOrCreateByName(catname);
	    	category.delete();
	    	
	    	User user = User.findByEmail(session().get("connected"));
	    	
	    	List<Category> categories = Category.find.all();
	    	
	    	Player player = Player.findByNumber(1);

	    	return CompletableFuture.completedFuture(ok(categorysList.render(user, "playercategories",  player, categories)));
	    	
	    }
	
	    

}



