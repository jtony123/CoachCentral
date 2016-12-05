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
        	
        	System.out.println("adding default UserPermissions");
            UserPermission permission = new UserPermission();
            permission.value = "view";
            permission.save();
        }
        
        // add default users
        if (User.find.findRowCount() < 2)
        {
        	System.out.println("adding default Users");
        	
            User user1 = new User();
            user1.email = "bob@orreco.com";
            user1.userName = "Bob";
            user1.setPassword("bobpass");
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
            user2.email = "alice@orreco.com";
            user2.userName = "Alice";
            user2.setPassword("alicepass");
            user2.roles = new ArrayList<SecurityRole>();
            user2.roles.add(SecurityRole.findByName("coach"));
            user2.permissions = new ArrayList<UserPermission>();
            user2.permissions.add(UserPermission.findByValue("view"));

            user2.save();
            Ebean.saveManyToManyAssociations(user2,
                    "roles");
            Ebean.saveManyToManyAssociations(user2,
                    "permissions");
            
            User user3 = new User();
            user3.email = "player@orreco.com";
            user3.userName = "Charlie";
            user3.setPassword("playerpass");
            user3.roles = new ArrayList<SecurityRole>();
            user3.roles.add(SecurityRole.findByName("player"));
            user3.permissions = new ArrayList<UserPermission>();
            user3.permissions.add(UserPermission.findByValue("view"));

            user3.save();
            Ebean.saveManyToManyAssociations(user3,
                    "roles");
            Ebean.saveManyToManyAssociations(user3,
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
        	
        	Category starting5 = new Category("Starting 5");
        	starting5.save();
        	
        	Category reserves = new Category("Reserves");
        	reserves.save();
        	
        	Category inj = new Category("Injured");
        	inj.save();
        	
        }
        
        // adding default players
        if (Player.find.findRowCount() < 4)
        {
        	User prev1 = User.findByEmail("bob@orreco.com");
        	User prev2 = User.findByEmail("alice@orreco.com");
        	
        	System.out.println("adding default Players");
        	
        	
        	Player player0 = new Player("no players", 0, null, prev1);
        	prev1.players.add(player0);
        	prev2.players.add(player0);
        	player0.save();
        	
        	Player player4 = new Player("Quincy Acy", 4, null, prev1);
        	player4.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player4);
        	prev2.players.add(player4);
        	player4.save();
        	
        	Player player5 = new Player("Justin Anderson", 5, null, prev1);
        	player5.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player5);
        	prev2.players.add(player5);
        	player5.save();
        	
        	Player player6 = new Player("JJ Barea", 6, null, prev2);
        	prev1.players.add(player6);
        	prev2.players.add(player6);
        	player6.save();
        	
        	Player player7 = new Player("Harrison Barnes", 7, null, prev1);
        	player7.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player7);
        	prev2.players.add(player7);
        	player7.save();
        	
        	Player player8 = new Player("Andrew Bogut", 8, null, prev1);
        	player8.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player8);
        	prev2.players.add(player8);
        	player8.save();
        	
        	Player player9 = new Player("Nico Brussino", 9, null, prev1);
        	prev1.players.add(player9);
        	prev2.players.add(player9);
        	player9.save();
        	
        	Player player10 = new Player("Seth Curry", 10, null, prev1);
        	player10.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player10);
        	prev2.players.add(player10);
        	player10.save();
        	
        	Player player11 = new Player("Jonathan Gibson", 11, null, prev1);
        	player10.categories.add(Category.findByName("First Team"));
        	prev1.players.add(player11);
        	prev2.players.add(player11);
        	player11.save();
        	
        	Player player12 = new Player("AJ Hammons", 12, null, prev1);
        	player12.categories.add(Category.findByName("Reserves"));
        	prev1.players.add(player12);
        	prev2.players.add(player12);
        	player12.save();
        	
        	Player player13 = new Player("Devin Harris", 13, null, prev1);
        	prev1.players.add(player13);
        	prev2.players.add(player13);
        	player13.save();
        	
        	Player player14 = new Player("Wes Matthews", 14, null, prev1);
        	player14.categories.add(Category.findByName("Reserves"));
        	prev1.players.add(player14);
        	prev2.players.add(player14);
        	player14.save();
        	
        	Player player15 = new Player("Salah Mejri", 15, null, prev1);
        	prev1.players.add(player15);
        	prev2.players.add(player15);
        	player15.save();
        	
        	Player player16 = new Player("Dirk Nowitzki", 16, null, prev1);
        	player12.categories.add(Category.findByName("Injured"));
        	prev1.players.add(player16);
        	prev2.players.add(player16);
        	player16.save();
        	
        	Player player17 = new Player("Dwight Powell", 17, null, prev1);
        	prev1.players.add(player17);
        	prev2.players.add(player17);
        	player17.save();
        	
        	
        	
        	Ebean.saveManyToManyAssociations(prev1, "players");
        	Ebean.saveManyToManyAssociations(prev2, "players");
        	
        }
    }
}
