import java.util.ArrayList;
import java.util.Arrays;

import com.avaje.ebean.Ebean;

import models.SecurityRole;
import models.User;
import models.UserPermission;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application)
    {
    	
    	System.out.println("onStart called");
        if (SecurityRole.find.findRowCount() == 0)
        {
        	System.out.println("first test entered");
        	
            for (String name : Arrays.asList("foo", "bar", "hurdy", "gurdy"))
            {
                SecurityRole role = new SecurityRole();
                role.roleName = name;
                role.save();
            }
        }

        if (UserPermission.find.findRowCount() == 0)
        {
        	
        	System.out.println("second test entered");
            UserPermission permission = new UserPermission();
            permission.value = "printers.edit";
            permission.save();
        }
        
        if (User.find.findRowCount() == 0)
        {
        	System.out.println("third test entered");
            User user = new User();
            user.userName = "bob";
            user.password = "fred";
            user.roles = new ArrayList<SecurityRole>();
            user.roles.add(SecurityRole.findByName("foo"));
            user.permissions = new ArrayList<UserPermission>();
            user.permissions.add(UserPermission.findByValue("printers.edit"));

            user.save();
            Ebean.saveManyToManyAssociations(user,
                                             "roles");
            Ebean.saveManyToManyAssociations(user,
                                             "permissions");
            
        }
    }
}
