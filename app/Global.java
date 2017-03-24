import java.util.ArrayList;
import java.util.Arrays;

import com.avaje.ebean.Ebean;

import models.SecurityRole;
import models.User;
import models.UserPermission;
import play.Application;
import models.Category;
import models.Player;
import models.RdxAlertReport;
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
        	
            for (String name : Arrays.asList("admin", "redoxadmin", "redoxview", "coach", "assistant", "player", "fan"))
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
        	
        	
        	// first admin person
        	
            User user1 = new User();
            user1.email = "anthony.jackson@orreco.com";
            user1.userName = "Anthony";
            user1.setPassword("CTDIctdi10");
            user1.roles = new ArrayList<SecurityRole>();
            user1.roles.add(SecurityRole.findByName("admin"));
            user1.roles.add(SecurityRole.findByName("coach"));
            user1.roles.add(SecurityRole.findByName("redoxadmin"));
            user1.permissions = new ArrayList<UserPermission>();
            user1.permissions.add(UserPermission.findByValue("view"));

            user1.save();
            Ebean.saveManyToManyAssociations(user1,
                    "roles");
            Ebean.saveManyToManyAssociations(user1,
                    "permissions");
            
            User user1a = new User();
            user1a.email = "grainne.conefrey@orreco.com";
            user1a.userName = "Grainne";
            user1a.setPassword("gc0rrec0");
            user1a.roles = new ArrayList<SecurityRole>();
            user1a.roles.add(SecurityRole.findByName("admin"));
            user1a.roles.add(SecurityRole.findByName("coach"));
            user1a.roles.add(SecurityRole.findByName("redoxadmin"));
            user1a.permissions = new ArrayList<UserPermission>();
            user1a.permissions.add(UserPermission.findByValue("view"));

            user1a.save();
            Ebean.saveManyToManyAssociations(user1a,
                    "roles");
            Ebean.saveManyToManyAssociations(user1a,
                    "permissions");
            
            User user2 = new User();
            user2.email = "demo@orreco.com";
            user2.userName = "Demo";
            user2.setPassword("demopass");
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
            user3.email = "jeremyholsopple@gmail.com";
            user3.userName = "Jeremy";
            user3.setPassword("MAV$0rrec0");
            user3.roles = new ArrayList<SecurityRole>();
            user3.roles.add(SecurityRole.findByName("coach"));
            user3.permissions = new ArrayList<UserPermission>();
            user3.permissions.add(UserPermission.findByValue("view"));

            user3.save();
            Ebean.saveManyToManyAssociations(user3,
                    "roles");
            Ebean.saveManyToManyAssociations(user3,
                    "permissions");
            
            User user4 = new User();
            user4.email = "paul.colgan@orreco.com";
            user4.userName = "Paul";
            user4.setPassword("paul$0rrec0");
            user4.roles = new ArrayList<SecurityRole>();
            user4.roles.add(SecurityRole.findByName("coach"));
            user4.permissions = new ArrayList<UserPermission>();
            user4.permissions.add(UserPermission.findByValue("view"));

            user4.save();
            Ebean.saveManyToManyAssociations(user4,
                    "roles");
            Ebean.saveManyToManyAssociations(user4,
                    "permissions");
            
            
        }
        
        // add default category "All"
        if (Category.find.findRowCount() == 0)
        {
        	System.out.println("adding default player Categorys");
        	Category category = new Category("All");
        	category.save();
        	
        	Category starting5 = new Category("Starting 5");
        	starting5.save();
        	
        	Category forwards = new Category("Forwards");
        	forwards.save();
        	
        	Category center = new Category("Centers");
        	center.save();
        	
        	Category guard = new Category("Guards");
        	guard.save();
        	
        	Category reserves = new Category("Reserves");
        	reserves.save();
        	
        	Category inj = new Category("Injured");
        	inj.save();
        	
        }
        
        // adding default players
        if (Player.find.findRowCount() < 4)
        {
        	User userAnthony = User.findByEmail("anthony.jackson@orreco.com");
        	User userGrainne = User.findByEmail("grainne.conefrey@orreco.com");
        	User userDemo = User.findByEmail("demo@orreco.com");
        	User userJeremy = User.findByEmail("jeremyholsopple@gmail.com");
        	User userPaul = User.findByEmail("paul.colgan@orreco.com");
        	
        	System.out.println("adding default demo Players");
        	
        	
        	Player player0 = new Player("no players", 0, null, userAnthony);
        	userAnthony.players.add(player0);
        	userGrainne.players.add(player0);
        	player0.save();
        	
    	Player player1 = new Player("Tremon Hudson", 1, null, userAnthony);
            player1.dob = "December 14, 1990";
	       	player1.height = "6'2";
	       	player1.weight = "220lbs";
	       	player1.position = "Guard";
	       	player1.categories.add(Category.findByName("Guards"));
	       	userAnthony.players.add(player1);
	       	userGrainne.players.add(player1);
	       	userDemo.players.add(player1);
	       	userPaul.players.add(player1);
	       	player1.save();
        	
    	Player player2 = new Player("Mosi Ruines", 2, null, userAnthony);
        	player2.dob = "June 25, 1989";
		   	player2.height = "7'1";
		   	player2.weight = "215lbs";
		   	player2.position = "Center";
		   	player2.categories.add(Category.findByName("Centers"));
		   	userAnthony.players.add(player2);
		   	userGrainne.players.add(player2);
		   	userDemo.players.add(player2);
		   	userPaul.players.add(player2);
		   	player2.save();
        	
        	
    	Player player3 = new Player("Brett Workman", 3, null, userAnthony);
            player3.dob = "July 24, 1993";
	       	player3.height = "6'11";
	       	player3.weight = "240lbs";
	       	player3.position = "Forward";
	       	player3.categories.add(Category.findByName("Forwards"));
	       	userAnthony.players.add(player3);
	       	userGrainne.players.add(player3);
	       	userDemo.players.add(player3);
	       	userPaul.players.add(player3);
	       	player3.save();
        	
        	Player player4 = new Player("Finn Browne", 4, null, userAnthony);
        	player4.dob = "October 16, 1992";
        	player4.height = "6'7";
        	player4.weight = "240lbs";
        	player4.position = "Forward";
        	player4.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player4);
        	userGrainne.players.add(player4);
        	userDemo.players.add(player4);
        	userPaul.players.add(player4);
        	player4.save();
        	
        	Player player5 = new Player("Harry Anderstown", 5, null, userAnthony);
        	player5.dob = "November 19, 1993";
        	player5.height = "6'6";
        	player5.weight = "228lbs";
        	player5.position = "Forward";
        	player5.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player5);
        	userGrainne.players.add(player5);
        	userDemo.players.add(player5);
        	userPaul.players.add(player5);
        	player5.save();
        	
        	Player player6 = new Player("Jack McGowan", 6, null, userAnthony);
        	player6.dob = "June 26, 1984";
        	player6.height = "6'0";
        	player6.weight = "185lbs";
        	player6.position = "Guard";
        	player6.categories.add(Category.findByName("Guards"));
        	userAnthony.players.add(player6);
        	userGrainne.players.add(player6);
        	userDemo.players.add(player6);
        	userPaul.players.add(player6);
        	player6.save();
        	
        	Player player7 = new Player("Niall Smith", 7, null, userAnthony);
             player7.dob = "May 15, 1994";
        	player7.height = "6'9";
        	player7.weight = "225lbs";
        	player7.position = "Foward";
        	player7.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player7);
        	userGrainne.players.add(player7);
        	userDemo.players.add(player7);
        	userPaul.players.add(player7);
        	player7.save();
        	
        	Player player8 = new Player("Sam Fernandez", 8, null, userAnthony);
             player8.dob = "November 02, 1990";
        	player8.height = "7'0";
        	player8.weight = "250lbs";
        	player8.position = "Center";
        	player8.categories.add(Category.findByName("Centers"));
        	userAnthony.players.add(player8);
        	userGrainne.players.add(player8);
        	userDemo.players.add(player8);
        	userPaul.players.add(player8);
        	player8.save();
        	
        	Player player9 = new Player("Brian Harvey", 9, null, userAnthony);
             player9.dob = "March 22, 1993";
        	player9.height = "6'7";
        	player9.weight = "225lbs";
        	player9.position = "Guard";
        	player9.categories.add(Category.findByName("Centers"));
        	userAnthony.players.add(player9);
        	userGrainne.players.add(player9);
        	userDemo.players.add(player9);
        	userPaul.players.add(player9);
        	player9.save();
        	
        	Player player10 = new Player("Michael Stewart", 10, null, userAnthony);
             player10.dob = "July 23, 1993";
        	player10.height = "6'5";
        	player10.weight = "195lbs";
        	player10.position = "Guard";
        	player10.categories.add(Category.findByName("Guards"));
        	userAnthony.players.add(player10);
        	userGrainne.players.add(player10);
        	userDemo.players.add(player10);
        	userPaul.players.add(player10);
        	player10.save();
        	
        	
        	// **** Mavs data
        	
        	Player player11 = new Player("Quincy Acy", 11, null, userAnthony);
        	player11.dob = "October 6, 1990";
        	player11.height = "6'7";
        	player11.weight = "240lbs";
        	player11.position = "Forward";
        	player11.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player11);
        	userGrainne.players.add(player11);
        	player11.save();
        	
        	Player player12 = new Player("Justin Anderson", 12, null, userAnthony);
        	player12.dob = "November 19, 1993";
        	player12.height = "6'6";
        	player12.weight = "228lbs";
        	player12.position = "Forward";
        	player12.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player12);
        	userGrainne.players.add(player12);
        	player12.save();
        	
        	Player player13 = new Player("JJ Barea", 13, null, userGrainne);
        	player13.dob = "June 26, 1984";
        	player13.height = "6'0";
        	player13.weight = "185lbs";
        	player13.position = "Guard";
        	player13.categories.add(Category.findByName("Guards"));
        	player13.addAlias("J.J. Barea");
        	userAnthony.players.add(player13);
        	userGrainne.players.add(player13);
        	userJeremy.players.add(player13);
        	player13.save();
        	
        	Player player14 = new Player("Harrison Barnes", 14, null, userAnthony);
             player14.dob = "May 30, 1992";
        	player14.height = "6'8";
        	player14.weight = "225lbs";
        	player14.position = "Foward";
        	player14.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player14);
        	userGrainne.players.add(player14);
        	userJeremy.players.add(player14);
        	player14.save();
        	
        	Player player15 = new Player("Andrew Bogut", 15, null, userAnthony);
             player15.dob = "November 28, 1984";
        	player15.height = "7'0";
        	player15.weight = "260lbs";
        	player15.position = "Center";
        	player15.categories.add(Category.findByName("Centers"));
        	userAnthony.players.add(player15);
        	userGrainne.players.add(player15);
        	player15.save();
        	
        	Player player16 = new Player("Nico Brussino", 16, null, userAnthony);
             player16.dob = "March 02, 1993";
        	player16.height = "6'7";
        	player16.weight = "215lbs";
        	player16.position = "Guard";
        	player16.categories.add(Category.findByName("Centers"));
        	player16.addAlias("Nicolas Brussino");
        	userAnthony.players.add(player16);
        	userGrainne.players.add(player16);
        	userJeremy.players.add(player16);
        	player16.save();
        	
        	Player player17 = new Player("Seth Curry", 17, null, userAnthony);
             player17.dob = "August 23, 1990";
        	player17.height = "6'2";
        	player17.weight = "185lbs";
        	player17.position = "Guard";
        	player17.categories.add(Category.findByName("Guards"));
        	userAnthony.players.add(player17);
        	userGrainne.players.add(player17);
        	userJeremy.players.add(player17);
        	player17.save();
        	
        	Player player18 = new Player("Jonathan Gibson", 18, null, userAnthony);
             player18.dob = "November 08, 1987";
        	player18.height = "6'2";
        	player18.weight = "185lbs";
        	player18.position = "Guard";
        	player18.categories.add(Category.findByName("Guards"));
        	userAnthony.players.add(player18);
        	userGrainne.players.add(player18);
        	userJeremy.players.add(player18);
        	player18.save();
        	
        	Player player19 = new Player("AJ Hammons", 19, null, userAnthony);
             player19.dob = "August 27, 1992";
        	player19.height = "7'0";
        	player19.weight = "260lbs";
        	player19.position = "Center";
        	player19.categories.add(Category.findByName("Centers"));
        	player19.addAlias("A.J. Hammons");
        	userAnthony.players.add(player19);
        	userGrainne.players.add(player19);
        	userJeremy.players.add(player19);
        	player19.save();
        	
        	
        	
        	
        	
        	Player player20 = new Player("Devin Harris", 20, null, userAnthony);
             player20.dob = "February 27, 1983";
        	player20.height = "6'3";
        	player20.weight = "185lbs";
        	player20.position = "Guard";
        	player20.categories.add(Category.findByName("Guards"));
        	userAnthony.players.add(player20);
        	userGrainne.players.add(player20);
        	userJeremy.players.add(player20);
        	player20.save();
        	
        	
        	
        	
        	Player player21 = new Player("Wes Matthews", 21, null, userAnthony);
             player21.dob = "October 14, 1986";
        	player21.height = "6'5";
        	player21.weight = "220lbs";
        	player21.position = "Guard";
        	player21.categories.add(Category.findByName("Guards"));
        	player21.addAlias("Wesley Matthews");
        	userAnthony.players.add(player21);
        	userGrainne.players.add(player21);
        	userJeremy.players.add(player21);
        	player21.save();
        	
        	
        	
        	Player player22 = new Player("Salah Mejri", 22, null, userAnthony);
             player22.dob = "June 15, 1986";
        	player22.height = "7'2";
        	player22.weight = "245lbs";
        	player22.position = "Center";
        	player14.categories.add(Category.findByName("Centers"));
        	userAnthony.players.add(player22);
        	userGrainne.players.add(player22);
        	userJeremy.players.add(player22);
        	player22.save();
        	
        	Player player23 = new Player("Dirk Nowitzki", 23, null, userAnthony);
             player23.dob = "June 19, 1978";
        	player23.height = "7'0";
        	player23.weight = "245lbs";
        	player23.position = "Centre";
        	player23.categories.add(Category.findByName("Centers"));
        	userAnthony.players.add(player23);
        	userGrainne.players.add(player23);
        	userJeremy.players.add(player23);
        	player23.save();
        	
        	Player player24 = new Player("Dwight Powell", 24, null, userAnthony);
             player24.dob = "July 20, 1991";
        	player24.height = "6'11";
        	player24.weight = "240lbs";
        	player24.position = "Forward";
        	player24.categories.add(Category.findByName("Forwards"));
        	userAnthony.players.add(player24);
        	userGrainne.players.add(player24);
        	userJeremy.players.add(player24);
        	player24.save();
        
    	Player player25 = new Player("Dorian Finney-Smith", 25, null, userAnthony);
             player25.dob = "October 03, 1991";
        	player25.height = "6'8";
        	player25.weight = "220lbs";
        	player25.position = "Forward";
        	player25.categories.add(Category.findByName("Forwards"));
        	player25.addAlias("Dorian Finney Smith");
        	userAnthony.players.add(player25);
        	userGrainne.players.add(player25);
        	userJeremy.players.add(player25);
        	player25.save();
        	
        Player player26 = new Player("Deron Williams", 26, null, userAnthony);
	        player26.dob = "June 26, 1984";
	        player26.height = "6'3";
	        player26.weight = "195lbs";
	        player26.position = "Guard";
	        player26.categories.add(Category.findByName("Guards"));
	       	userAnthony.players.add(player26);
	       	userGrainne.players.add(player26);
	       	player26.save();
	       	
	    Player player27 = new Player("Pierre Jackson", 27, null, userAnthony);
		    player27.dob = "August 29, 1991";
		    player27.height = "5'10";
		    player27.weight = "180lbs";
		    player27.position = "Guard";
		    player27.categories.add(Category.findByName("Guards"));
	       	userAnthony.players.add(player27);
	       	userGrainne.players.add(player27);
	       	userJeremy.players.add(player27);
	       	player27.save();
	       	
	    Player player28 = new Player("Nerlens Noel", 28, null, userAnthony);
	    player28.dob = "April 10, 1994";
	    player28.height = "6'11";
	    player28.weight = "228lbs";
	    player28.position = "Guard";
	    player28.categories.add(Category.findByName("Forwards"));
	       	userAnthony.players.add(player28);
	       	userGrainne.players.add(player28);
	       	userJeremy.players.add(player28);
	       	player28.save();
	       	
	      
        	
        	Ebean.saveManyToManyAssociations(userAnthony, "players");
        	Ebean.saveManyToManyAssociations(userGrainne, "players");
        	Ebean.saveManyToManyAssociations(userDemo, "players");
        	Ebean.saveManyToManyAssociations(userJeremy, "players");
        	
        }
        
        if (RdxAlertReport.find.findRowCount() < 9) {
        	
        	String sleep1 = "Aim for at least 8 hours sleep consistently every night"
					+"Sleep in a cool§ noise free and blacked out room"
					+"Switch off all electronic devices 2 hours before bedtime"
					+"Spend at least 1 hour every day outside in natural daylight§ particularly in the mornings§ to increase melatonin and to regulate sleep quality and anti-oxidant defenses"
					+"Avoid caffeine after 3 pm"
					+"Optimise hydration during the day to avoid excessive drinking before bed and waking during the night"
					+"If sleep is disrupted by games and travel schedule§ try to take a nap after training/practice";
        	
        	String diet1 = "Review protein requirements to reflect training load;"
        				+ "Increase dietary intake of anti-oxidants found in fruit and vegetables to aid recovery;"
        				+ "Minimum of 5 serves required§ but aim for 7-10 serves per day;"
        				+ "Take tart cherry or a berry smoothie to help optimise recovery and reduce oxidative stress;" 
        				+ "Consider probiotics"; 
        	
        	String diet2 = "Review diet to reflect training load and injury/illness status";
        			
        	//1
        	RdxAlertReport RG = new RdxAlertReport();
        	RG.setAlert("RED", "GREEN", 
        			"Outside of critical threshold;Physiological and psychological stresses are high and increased oxidative stress is indicated", 
        			"Illness (present or pending);Increased risk of injury and non-functional overreaching;Higher probability of underperforming", 
        			"Review training load;View diet and sleep advice", 
        			"Review training load", 
        			diet1,
        			sleep1);
        	//2
        	RdxAlertReport RR = new RdxAlertReport();
        	RR.setAlert("RED", "RED", 
        			"Poor profile. Physiological and psychological stresses are high§ underlying defenses are low and increased oxidative stress is indicated", 
        			"Illness (present or pending);Increased risk of injury and non-functional overreaching;Higher probability of underperforming", 
        			"Review training load;View diet and sleep advice", 
        			"Review training load", 
        			diet1,
        			sleep1);
        	//3
        	RdxAlertReport GR = new RdxAlertReport();
        	GR.setAlert("GREEN", "RED", 
        			"Outside of critical threshold. Underlying defenses are low and a reduced anti-oxidant capacity is indicated", 
        			"Athlete may be in a detrained state§ a rehabilitation phase or recovering from illness", 
        			"Check injury/illness status;Check sleep quality;Review training load;Review diet to reflect training load and injury/illness status", 
        			"Review training load", 
        			"Review diet to reflect training load and injury/illness status",
        			sleep1);
        	//4
        	RdxAlertReport GG = new RdxAlertReport();
        	GG.setAlert("GREEN", "GREEN", 
        			"Good profile. No action required", 
        			"", 
        			"", 
        			"", 
        			"",
        			"");
        	//5
           	RdxAlertReport RA = new RdxAlertReport();
        	RA.setAlert("RED", "AMBER", 
        			"Outside of critical threshold;Physiological and psychological stresses are high§ underlying defenses are low and increased oxidative stress is indicated", 
        			"Illness (present or pending);Increased risk of injury and non-functional overreaching;Higher probability of underperforming", 
        			"Review training load;View diet and sleep advice", 
        			"Review training load", 
        			diet1,
        			sleep1);
        	//6
           	RdxAlertReport AG = new RdxAlertReport();
        	AG.setAlert("AMBER", "GREEN", 
        			"Close to critical threshold;Athlete may be in a period of heavy training and game load", 
        			"",
        			"", 
        			"", 
        			"",
        			"");
        	
        	//7
           	RdxAlertReport AA = new RdxAlertReport();
        	AA.setAlert("AMBER", "AMBER", 
        			"Close to critical threshold;Athlete may be in a period of heavy training and game load;Close observation required. Athlete may become overreached if extended", 
        			"",
        			"", 
        			"", 
        			"",
        			"");
        	
           	//8
           	RdxAlertReport AR = new RdxAlertReport();
        	AR.setAlert("AMBER", "RED", 
        			"Outside critical threshold. Monitor closely;Athlete may become overreached if extended", 
        			"",
        			"", 
        			"", 
        			"",
        			"");
        	
           	//9
           	RdxAlertReport GA = new RdxAlertReport();
        	GA.setAlert("GREEN", "AMBER", 
        			"Close to critical threshold. Observation required", 
        			"",
        			"", 
        			"", 
        			"",
        			"");
        			
        	
        }
    }
}

