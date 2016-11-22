import java.util.ArrayList;
import java.util.Arrays;

import com.avaje.ebean.Ebean;

import models.SecurityRole;
import models.User;
import models.UserPermission;
import play.Application;
import models.Category;
import models.Player;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application)
    {
    	
    	System.out.println("onStart called");
    	
    	// add default security roles
        if (SecurityRole.find.findRowCount() < 5)
        {
        	System.out.println("adding default SecurityRoles");
        	
            for (String name : Arrays.asList("admin", "coach", "assistant", "player", "fan"))
            {
                SecurityRole role = new SecurityRole();
                role.roleName = name;
                role.save();
            }
        }

        // add default permissions
        if (UserPermission.find.findRowCount() < 1)
        {
        	
        	System.out.println("sadding default UserPermissions");
            UserPermission permission = new UserPermission();
            permission.value = "view";
            permission.save();
        }
        
        // add default users
        if (User.find.findRowCount() < 2)
        {
        	System.out.println("adding default Users");
        	
            User user1 = new User();
            user1.email = "bob@gmail.com";
            user1.userName = "Bob";
            user1.password = "aaa";
            user1.roles = new ArrayList<SecurityRole>();
            user1.roles.add(SecurityRole.findByName("admin"));
            user1.roles.add(SecurityRole.findByName("coach"));
            user1.permissions = new ArrayList<UserPermission>();
            user1.permissions.add(UserPermission.findByValue("view"));

            user1.save();
            Ebean.saveManyToManyAssociations(user1,
                    "roles");
            Ebean.saveManyToManyAssociations(user1,
                    "permissions");
            
            User user2 = new User();
            user2.email = "alice@gmail.com";
            user2.userName = "Alice";
            user2.password = "aaa";
            user2.roles = new ArrayList<SecurityRole>();
            user2.roles.add(SecurityRole.findByName("coach"));
            user2.permissions = new ArrayList<UserPermission>();
            user2.permissions.add(UserPermission.findByValue("view"));

            user2.save();
            Ebean.saveManyToManyAssociations(user2,
                    "roles");
            Ebean.saveManyToManyAssociations(user2,
                    "permissions");
            
            
        }
        
        // add default category "All"
        if (Category.find.findRowCount() == 0)
        {
        	System.out.println("adding default player Categorys");
        	Category category = new Category("All");
        	category.save();
        	
        	Category cat2 = new Category("First Team");
        	cat2.save();
        	
        	Category reserves = new Category("Reserves");
        	reserves.save();
        	
        	Category inj = new Category("Injured");
        	inj.save();
        	
        }
        
        // adding default players
        if (Player.find.findRowCount() < 4)
        {
        	User prev1 = User.findByEmail("bob@gmail.com");
        	User prev2 = User.findByEmail("alice@gmail.com");
        	
        	System.out.println("adding default Players");
        	
        	Player player0 = new Player("no players", 0, null, prev1);
        	prev1.players.add(player0);
        	prev2.players.add(player0);
        	player0.save();
        	
        	Player player1 = new Player("Jed Klump", 1, null, prev1);
        	player1.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player1);
        	player1.save();
        	
        	

        	Player player2 = new Player("Nelson Riemann", 2, null, prev1);
        	player2.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player2);
        	player2.save();
        	
        	
        	Player player3 = new Player("Melvin Meriwether", 3, null, prev2);
        	prev2.players.add(player3);
        	player3.save();
        	
        	
        	Player player4 = new Player("Jack Sharp", 4, null, prev1);
        	prev1.players.add(player4);
        	player4.save();
        	
        	Player player5 = new Player("Justin Dickens", 5, null, prev1);
        	prev1.players.add(player5);
        	player5.save();
        	
        	Ebean.saveManyToManyAssociations(prev1, "players");
        	Ebean.saveManyToManyAssociations(prev2, "players");
        	
        }
    }
}
