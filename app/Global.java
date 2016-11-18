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
        if (SecurityRole.find.findRowCount() == 0)
        {
        	System.out.println("adding default SecurityRoles");
        	
            for (String name : Arrays.asList("admin", "coach", "assistant", "player", "fan"))
            {
                SecurityRole role = new SecurityRole();
                role.roleName = name;
                role.save();
            }
        }

        if (UserPermission.find.findRowCount() == 0)
        {
        	
        	System.out.println("sadding default UserPermissions");
            UserPermission permission = new UserPermission();
            permission.value = "view";
            permission.save();
        }
        
        if (User.find.findRowCount() == 0)
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
        if (Category.find.findRowCount() == 0)
        {
        	System.out.println("adding default player Categorys");
        	Category category = new Category("All");
        	category.save();
        	
        }
        
        if (Player.find.findRowCount() < 1)
        {
        	User prev = User.findByEmail("bob@gmail.com");
        	
        	System.out.println("adding default Players");
        	System.out.println("bob found " + prev.email);
        	Player player1 = new Player("Jed Klump", 1, null, prev);
        	player1.save();
        	
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
        	Player player2 = new Player("Nelson Riemann", 2, null, User.findByEmail("bob@gmail.com"));
        	player2.save();
        	Player player3 = new Player("Melvin Meriwether", 3, null, User.findByEmail("alice@gmail.com"));
        	player3.save();
        	
        }
    }
}
