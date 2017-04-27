package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Category;
import models.Player;
import models.Redox;
import models.User;
import play.Configuration;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import utilities.RedoxCSVUpdater;
import utilities.RedoxUtilities;
import views.html.redox;
import views.html.redoxQuestionnaire;
import views.html.Admin.users;

public class RedoxController extends Controller {
	
	@Inject 
	private  Configuration configuration;
	String filepath;
	
	

    @Inject
    FormFactory formFactory;
    
    private final HttpExecutionContext ec;

    @Inject
    public RedoxController (final HttpExecutionContext ec, Configuration configuration)
    {
        this.ec = ec;
        this.configuration = configuration;
        filepath = configuration.getString("filepath");
    }
    
    
    @Restrict({@Group({"coach"})})
    public CompletionStage<Result> redox(int playernumber, String category) {
    	System.out.println("redox called");
    	
    	User user = User.findByEmail(session().get("connected"));
    	Category categoryFound = Category.findByName(category);
    	Player player;
    	List<Player> players;
    	
    	if(categoryFound != null){
    		//System.out.println("cat not null");
    		players = user.getPlayersCategorisedWith(categoryFound);
    		
    		// check for empty category for this user
    		if(players == null || players.isEmpty()){
    		//empty category, return 'no players'
    			//System.out.println("players is null, sending no players");
    			player = Player.findByPlayername("no players");
    			players.add(player);
    		} else {
    		// the category is not empty
    			//System.out.println("players not null");
    	    	if (playernumber == 0) {
    	    		// to handle initial call
    	    		//System.out.println("handle initial call, sending player 0");
    	    		player = players.get(0);
    	    	} else {
    	    		player = Player.findByNumber(playernumber);
    	    		//System.out.println("finding player by number");
    	    		
    	    		// check first if the player is null(out of range)
    	    		if(player == null) {
    	    			player = players.get(0);
    	    		} else {
    	    			// check if the player that the user had highlighted is in this category
        	    		if (!player.categories.contains(categoryFound)){
        	    			//System.out.println("player not in this category, sending player 0");
        	    			player = players.get(0);
        	    		}
        	    		
        	    		if(players.contains(player)){
        	    			System.out.println("player is in the list");
        	    		} else {
        	    			System.out.println("player not in the list");
        	    			player = players.get(0);
        	    		}
    	    		}
    	    		
    	    		 
    	    	}
    		}
    		
    	} else {
    		//System.out.println("cat is null");
    		players = user.getPlayersCategorisedWith(Category.findByName("All"));
    		player = players.get(0);
    		category = "All";
    	}
    	
    	List<Category> categories = Category.find.all();
    	int playerIndex = players.indexOf(player);
    	return CompletableFuture.completedFuture(ok(redox.render(user, "redox", player, playerIndex, players, category, categories)));
    	
    }
    
    
    
    
    
  public Result getRedoxCSV(Integer playernumber){
    	
    	System.out.println("getRedoxCSV called");
   	 
 	Player p = Player.findByNumber(playernumber);
 	List<Redox> rdx = Redox.findByPlayer(p);
 	Map<Long, String> timestamps = new TreeMap<Long, String>();
 	
 	for(Redox r : rdx) {
 		
 		Integer included = r.includeInCritDiff? 1: 0;
 		String aline = p.playername +","
 				+ r.id +","
 				+ r.rdxalertreport.result + ","
 				+ r.sportscientistComment+","
 				+ r.orrecoScientist+","
 				+ r.rdxalertreport.potentialoutcomes + ","
 				+ r.rdxalertreport.actions + ","
 				
 				+ r.rdxalertreport.sleepadvice + ","
 				+ r.rdxalertreport.trainingloadadvice + ","
 				+ r.rdxalertreport.dietaryadvice + ","
 				
 				+r.exercised+","+r.eaten+","
 				+r.exercises+","
 				+"exergym,"+"exertrain,"+"exergame,"+"exerrest,"+r.other+","
 				+r.energy.toString()+","+r.muscleSoreness.toString()+","
 				+r.fever+","+r.sorethroat+","+r.headache+","+r.jointmuscleache+","+r.diarrhea+","+","
 				+r.illness+","+r.injured+","
 				+r.additionalNotes+","
 				+r.date.getTime()+","+r.defence.toString()+","+included.toString()+","+r.defenceThreshold.toString()+","+r.stress.toString()+","+included.toString()+","+r.stressThreshold.toString();
 		
 		//System.out.println(aline);
 		
 		timestamps.put(r.date.getTime(), aline);
 	}
 	
 	
 	
 	String csvFileHeader = "playername,id,"
			+"ResultShow,NOTES,SS,POTOUT,ACTION,"
 			+"Sleepadvice,TrainingLoadAdvice,DietAdvice,"
			+"TrainedToday,AteToday,Exercises,ExerciseGym,ExerciseTraining,ExerciseGame,ExerciseRest,ExerciseOther,"
			+"EnergyLevel,MuscleSoreness,"
			+"Fever,SoreThroat,Headache,JointorMuscleAche,Diarrhea,Other,"
			+"Ill,Injured,"
			+ "testernotes,"
			+"TEST_TIME,DEFENCE,DEFENCE_INC,DEFENCE_CDT,STRESS,STRESS_INC,STRESS_CDT";
 	
 	
 	File playerfile = new File("data/attachments/"+p.playername+"_Rdx.csv");
 	
		if (!playerfile.exists()) {
			try {

				playerfile.createNewFile();
			} catch (IOException e1) {
				// TODO return an error message to the user
				e1.printStackTrace();
			}
		}
 	
		PrintWriter out1 = null;
		try {
			out1 = new PrintWriter(new BufferedWriter(new FileWriter(playerfile)));

			out1.println(csvFileHeader);
			
			for (Map.Entry<Long, String> entry : timestamps.entrySet()) {
				 //System.out.println(entry.getKey() + ": " + entry.getValue());
				  out1.println(entry.getValue());
				}


		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		} finally {
			out1.flush();
			out1.close();
		}
		
		
 	
 	File file = new File("/tmp/tmpCSV.csv");
 	
		if (!file.exists()) {
			try {

				file.createNewFile();
			} catch (IOException e1) {
				// TODO return an error message to the user
				e1.printStackTrace();
			}
		}
 	
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			out.println(csvFileHeader);
			
			for (Map.Entry<Long, String> entry : timestamps.entrySet()) {
				 //System.out.println(entry.getKey() + ": " + entry.getValue());
				  out.println(entry.getValue());
				}


		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
 	return ok(new java.io.File(file.getAbsolutePath()));
    }
  
  
  
  
  
  @Restrict({@Group({"coach"})})
  public CompletionStage<Result> redoxQ(int playernumber, String category) {
  	System.out.println("record called");
  	User user = User.findByEmail(session().get("connected"));
  	Category categoryFound = Category.findByName(category);
  	List<Player> players = user.getPlayersCategorisedWith(categoryFound);
  	
  	
  	
  	Player player = Player.findByNumber(playernumber);
  	if(player == null) {
  		player = Player.find.byId((long) 1);
  	}
  	
  	List<Player> players1 = new ArrayList<Player>();
  	players1.add(player);
  	
  	List<Category> categories = Category.find.all();
  	//System.out.println("player count = " + players.size());
  	int playerIndex = players.indexOf(player);
  	
  	return CompletableFuture.completedFuture(ok(redoxQuestionnaire.render(user, "redoxQ", player, playerIndex, players1, category, categories)));
  	
  }
  
    
    
    
    public CompletionStage<Result> saveRedox(int playernumber, String category) {
       	
       	System.out.println("saveRedox called");
       	
       	DynamicForm form = Form.form().bindFromRequest();
       	
       	DynamicForm requestData = formFactory.form().bindFromRequest();
       	
       	Map<String, String> mydata = requestData.data();
       	
       	System.out.println(mydata);
       	
       	Player player = Player.findByNumber(playernumber);
       	
       	String fever = "";
       	if(mydata.get("fevertoday") != null) {
       		fever += mydata.get("fevertoday").equalsIgnoreCase("on") ? "Today;": "";
       	}
       	
       	if(mydata.get("feverlastweek") != null) {
       		fever += mydata.get("feverlastweek").equalsIgnoreCase("on") ? "In the last week;": "";
       	}
       	
       	String coldsorethroat = "";
       	if(mydata.get("coldtoday") != null) {
       		coldsorethroat += mydata.get("coldtoday").equalsIgnoreCase("on") ? "Today;": "";
       	}
       	
       	if(mydata.get("coldlastweek") != null) {
       		coldsorethroat += mydata.get("coldlastweek").equalsIgnoreCase("on") ? "In the last week;": "";
       	}
       	
       	String headache = "";
       	if(mydata.get("headachetoday") != null) {
       		headache += mydata.get("headachetoday").equalsIgnoreCase("on") ? "Today;": "";
       	}
       	
       	if(mydata.get("headachelastweek") != null) {
       		headache += mydata.get("headachelastweek").equalsIgnoreCase("on") ? "In the last week;": "";
       	}
       	
       	String jointmuscleache = "";
       	if(mydata.get("muscachetoday") != null) {
       		jointmuscleache += mydata.get("muscachetoday").equalsIgnoreCase("on") ? "Today;": "";
       	}
       	
       	if(mydata.get("muscachelastweek") != null) {
       		jointmuscleache += mydata.get("muscachelastweek").equalsIgnoreCase("on") ? "In the last week;": "";
       	}
       	
       	String diarrhea = "";
       	if(mydata.get("sicktoday") != null) {
       		diarrhea += mydata.get("sicktoday").equalsIgnoreCase("on") ? "Today;": "";
       	}
       	
       	if(mydata.get("sicklastweek") != null) {
       		diarrhea += mydata.get("sicklastweek").equalsIgnoreCase("on") ? "In the last week;": "";
       	}
       	
       	String exercises = "";
       	if(mydata.get("gym") != null) {
       		exercises += mydata.get("gym").equalsIgnoreCase("on") ? "Gym weights;": "";
       	}
       	
    	if(mydata.get("practice") != null) {
    		exercises += mydata.get("practice").equalsIgnoreCase("on") ? "Practice;": "";
       	}
    	
    	if(mydata.get("game") != null) {
    		exercises += mydata.get("game").equalsIgnoreCase("on") ? "Game;": "";
       	}
    	
    	if(mydata.get("rest") != null) {
    		exercises += mydata.get("rest").equalsIgnoreCase("on") ? "Rest;": "";
       	}

    	if(mydata.get("otherexercises") != null || mydata.get("otherexercises") != "") {
    		exercises += mydata.get("otherexercises") +";";
       	}
    	
    	
    	
    	Double nrg = Double.parseDouble(mydata.get("nrglevel"));
    	if(nrg < 0.75) {
    		nrg = 0.0;
    	}
    	
    	System.out.println("nrg level is "+nrg);
    	
    	
    	Double musc = Double.parseDouble(mydata.get("musclevel"));
    	if(musc < 0.75) {
    		nrg = 0.0;
    	}
    	
    	System.out.println("nrg level is "+nrg);
    	
    	Double stress = 0.0;
    	if(mydata.get("stress") != null){
    		if(!mydata.get("stress").isEmpty()){
    			stress = Double.parseDouble(mydata.get("stress"));
    		}
    		
    	}
    	
    	Double defence = 0.0;
    	if(mydata.get("defence") != null){
    		if(!mydata.get("defence").isEmpty()){
    			defence = Double.parseDouble(mydata.get("defence"));
    		}
    	}
    	
    	
    	boolean include = stress > 0.01 ? true : false;
    	
    	
    	
    	String addnotes = "";
    	if(mydata.get("notes") != null || mydata.get("notes") != "") {
    		
    		// replace any commas with 'section symbol' to prevent corruption of csv format when returning 
    		// data to browser
    		String replacedcommas = mydata.get("notes").replace(",", "§");
    		
    		// replace any carriage returns(newlines) to be able to format it properly on returned page
    		// using semi-colon to separate lines 
    		String replacedcarriagereturns = replacedcommas.replaceAll("(\\r|\\n|\\r\\n)+", ";");
    		
    		addnotes += replacedcarriagereturns +";";
       	}
    	
       	
       	
       	
       	Redox rdx = new Redox(player, new Date());
       	rdx.setRedoxTestResult(mydata.get("eaten"), 
       			mydata.get("exercise"), 
       			fever, //fever, 
       			coldsorethroat,//sorethroat, 
       			headache,//headache, 
       			jointmuscleache,//jointmuscleache, 
       			diarrhea,//diarrhea, 
       			exercises,
       			"",//gymweights, 
       			"",//practicetraining, 
       			"",//game, 
       			"",//rest, 
       			"",//other, 
       			nrg,//energy, 
       			musc,//muscleSoreness, 
       			stress,//stress, 
       			defence,//defence, 
       			include,
       			mydata.get("illness"),
       			mydata.get("injury"),
       			addnotes
       			);//includeInCritDiff);
       	
       	
       	
       	return CompletableFuture.completedFuture(redirect(routes.RedoxController.redox(playernumber, category)));
       	
       }
    
    
    
    public CompletionStage<Result> saveComment(int playernumber, String category){
 	   System.out.println("saveComment called");
 	   
 	   	DynamicForm form = Form.form().bindFromRequest();
 	   	
 	   	DynamicForm requestData = formFactory.form().bindFromRequest();
 	   	
 	   	Map<String, String> mydata = requestData.data();
 	   	
 	   	System.out.println(mydata);
 	   	
 	   	Player player = Player.findByNumber(playernumber);
 	   	
 	   	String rdxid = "";
 	   	if(mydata.get("rdxid") != null) {
 	   		rdxid += mydata.get("rdxid");
 	   	}
 	   	
 	   	String comment = "";
 		if(mydata.get("comment") != null) {
 			String cleanComment = mydata.get("comment").replace(",", "§");
 			comment += cleanComment;
 	   	}
 		
 	   	String commentBy = "";
 		if(mydata.get("ssname") != null) {
 			commentBy += mydata.get("ssname");
 	   	}
 	   	
 	   	Long rid = Long.parseLong(rdxid);
 	   	Redox rdx = Redox.findById(rid);
 	   	rdx.addComment(comment, commentBy);
 	   	
 	   	
 	   	
    	
    	return CompletableFuture.completedFuture(redirect(routes.RedoxController.redox(playernumber, category)));
    }
    
    
    
    
    
   public CompletionStage<Result> updateRedoxToggleState(int playernumber, String category) {
    	
    	System.out.println("updateRedoxCSV called");
    	
    	DynamicForm form = Form.form().bindFromRequest();
    	String timekey = form.get("timekey");
    	
    	Date date = new Date(Long.parseLong(timekey));
    	Player player = Player.findByNumber(playernumber);
    	
    	Redox rdx = Redox.findByTimeKey(player, date);
    	rdx.toggleIncludedState();
    	
    	return CompletableFuture.completedFuture(redirect(routes.RedoxController.redox(playernumber, category)));
    	
    }
   
   
  
   
   
   
   public CompletionStage<Result> uploadRedoxCSV() {
   	
	   
		System.out.println("uploadRedoxCSV called");
	   	User user = User.findByEmail(session().get("connected"));
	   	List<User> allusers = User.find.all();
	   	
	   	List<FilePart<Object>> files = request().body().asMultipartFormData().getFiles();
	   	
	   	for(FilePart<Object> fp : files){
	   		
	           if (fp != null) {
	               
	               File file = (File) fp.getFile();
	               
	               RedoxUtilities rdxutil = new RedoxUtilities();
	               Map<String, ArrayList<String>> playerdatabyname = rdxutil.getDemoRedoxData(file.getAbsolutePath());
	               
	       		Iterator it = playerdatabyname.entrySet().iterator();
	       		while (it.hasNext()) {
	       			
	       			
	       			Map.Entry pair = (Map.Entry) it.next();
	       			
	       			
	       			String[] tokens = null;
	       			
	       			for(String s : (List<String>) pair.getValue()){
	       				
	       				tokens = s.split(",");
	       			}
	       			
	       			for(int i=0;i<tokens.length;++i){
	       				System.out.println(i +" : " + tokens[i]);
	       			}
	       			
	       			
	       			
	       			Player player = Player.findByNameOrAlias((String) pair.getKey());

	       			if(player.playername.equalsIgnoreCase("no players")){
	       				player =  Player.findByNumber(0);
	       			} else {
	       				
						Long timestamp = Long.parseLong(tokens[15]);
						Date actual = new Date(timestamp);
						System.out.println("actual date is "+actual);
						// check if already have a test for this player at this time
						Redox rdx = Redox.findByTimeKey(player, actual);//new Redox(player);
						if(rdx == null){
							System.out.println("rdx is null");
							rdx = new Redox(player, actual);
							rdx.setRedoxTestResult(tokens[2],//eaten,
									tokens[3],//exercised, 
									tokens[4],//fever, 
									tokens[5],//sorethroat, 
									tokens[6],//headache, 
									tokens[7],//jointmuscleache, 
									tokens[8],//diarrhea, 
									tokens[9],//exercises
									"",//gymweights, 
									"",//practicetraining, 
									"",//game, 
									"",//rest, 
									"",//other, 
									// remember to add illness, injury here
									Double.parseDouble(tokens[13]),//energy, 
									Double.parseDouble(tokens[14]),//muscleSoreness, 
									Double.parseDouble(tokens[20]),//stress, 
									Double.parseDouble(tokens[16]),//defence, 
									true,
									tokens[10],//illness
									tokens[11],//injured
									tokens[12]//addnotes
											);
									rdx.addComment("", "");
						}else{
							System.out.println("existing rdx result");
							rdx.setRedoxTestResult(tokens[2],//eaten,
									tokens[3],//exercised, 
									tokens[4],//fever, 
									tokens[5],//sorethroat, 
									tokens[6],//headache, 
									tokens[7],//jointmuscleache, 
									tokens[8],//diarrhea,
									tokens[9],//exercises
									"",//gymweights, 
									"",//practicetraining, 
									"",//game, 
									"",//rest, 
									"",//other, 
									// remember to add illness, injury here
									Double.parseDouble(tokens[13]),//energy, 
									Double.parseDouble(tokens[14]),//muscleSoreness, 
									Double.parseDouble(tokens[20]),//stress, 
									Double.parseDouble(tokens[16]),//defence, 
									true,
									tokens[10],//illness
									tokens[11],//injured
									tokens[12]//addnotes
											);
							rdx.addComment("", "");
						}
	           			player.save();
	       			}
	       		}
	   		
	           } else {
	           	System.out.println("fp is null");
	           }
	           
	   	}
	   	Player player = Player.findByNumber(1);
	   	return CompletableFuture.completedFuture(ok(users.render(user, "users", player, allusers)));

	    }
	   
   
   public Result getFullRedoxCSV(){
   	
   	System.out.println("getFullRedoxCSV called");
  	
  	 return ok(new java.io.File(filepath +"redox.csv"));
   }
	   

}
