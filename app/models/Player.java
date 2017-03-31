package models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.config.PropertyMap;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebeaninternal.server.type.ScalarTypeJsonMap.Blob;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

@Entity
public class Player extends Model {

	@Id
	public Long id;

	// @Required
	public String playername;
	
	@OneToMany(mappedBy="player", cascade=CascadeType.ALL)
	public List<NameAlias> aliases;
	
	public String height;
	
	public String weight;
	
	public String dob;
	
	public String position;

	// @Required
	public Integer playernumber;

	public Date dateadded;

	public String playerPhotofilename;
	
	@Lob
	public byte[] playerPhoto;

	@Column(columnDefinition = "VARBINARY")
	 public File file;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	 public List<Redox> redox = new ArrayList<Redox>();
	
	 public String filename;
	 
	 public String redoxFilename;

	// @Required
	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<User> users;

	@ManyToMany(cascade = CascadeType.MERGE)
	public Set<Category> categories;

	 @ManyToMany(cascade=CascadeType.PERSIST)
	 public Set<Question> questions;
	
	 @OneToMany(mappedBy="player", cascade=CascadeType.ALL)
	 public List<Questionnaire> questionnaire;

	public Player(String playerName, Integer playerNumber, Blob playerPhoto, User user) {
		this.playername = playerName;
		this.playernumber = playerNumber;
//		if (playerPhoto != null) {
//			this.playerPhoto = playerPhoto;
//		}
		this.users = new ArrayList<User>();
		this.dateadded = new Date();

		// this.questionnaire = new ArrayList<Questionnaire>();
		this.categories = new TreeSet<Category>();
		// all players are automatically categorised in All except for the 'no players'
		
		if(playerName.equalsIgnoreCase("no players")){
			// dont categorise the 'no players'
		} else {
			this.categoriseItWith("All");
		}
		this.aliases = new ArrayList<NameAlias>();

		// this.questions = new TreeSet<Question>();

	}

	public static Finder<Long, Player> find = new Finder<>(Player.class);

	public Set<Category> getCategories() {
		return categories;
	}
	
	public void addToCategory(String cat){
		this.categories.add(Category.findByName(cat));
		this.save();
	}
	
	public void removeFromCategory(String cat){
		this.categories.remove(Category.findByName(cat));
		this.save();
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addAlias(String alias){
		NameAlias nameAlias = new NameAlias();
		nameAlias.alias = alias;
		this.aliases.add(nameAlias);
	}

//	public Player updatePhoto(Blob photo) {
//
//		this.playerPhoto = photo;
//		this.save();
//		return this;
//	}
	
	public void setPlayerPhoto(){
		
		File afile = new java.io.File("data/demodataimages/"+this.playername+".png");
	    //FilePart<File> filename = afile;
	    
	    if (afile != null) {
	    	System.out.println("filename not null");
	        String fileName = afile.getName();
	        
	        System.out.println("size = "+afile.length());
	        if(afile.length() > 0){
	        	 this.playerPhotofilename = fileName;
		            this.playerPhoto = new byte[(int)afile.length()];
		            
		            InputStream inStream = null;
		            try {
		                inStream = new BufferedInputStream(new FileInputStream(afile));
		                inStream.read(this.playerPhoto);
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
	    this.save();
	}

	
	public static Player findByNumber(Integer playernumber){
		
		Player player = Player.find.where().eq("playernumber", playernumber).findUnique();
		// TODO: catch exception for no player found
		return player;
		
	}
	
	public static Player findByPlayername(String playername){
		Player player = Player.find.where().eq("playername", playername).findUnique();
		return player;
	}
	
	public static Player findByNameOrAlias(String name){
		Player player = Player.find.where().eq("playername", name).findUnique();
		if(player == null){
			NameAlias namealias = NameAlias.find.where().eq("alias", name).findUnique();
			if(namealias != null) {
				player = Player.find.byId(namealias.player.id);
			} else {
				player = Player.findByPlayername("no players");
			}
			
		}
		return player;
	}



	/**
	 * Categorising a player
	 * 
	 * @param name
	 * @return
	 */
	public Player categoriseItWith(String name) {
		categories.add(Category.findOrCreateByName(name));
		return this;
	}

	/**
	 * Get players in a specific category
	 * 
	 * @param name
	 * @return
	 */
	public static List<Player> findCategorisedWith(User user, String category) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", category);
		map.put("email", user.email);
		
		
		
		List<Player> players = Player.find
				.fetch("categories")
				//.fetch("users")
				.fetch("users", new FetchConfig().query())
				.where().allEq(map)
				
				//.where().eq("email", user.email)
				
				//.where().eq("user.email", user.email)
				// .in(user.email, Player.find.fetch("users")
				.findList();
		
		for(Player p : players){
			System.out.print("got this : "+ p.playername);
			for(User u : p.users){
				System.out.print(" with these users : " + u.email);
			}
			System.out.println();
		}

		return players;

	}

	/**
	 * Get a users players in a specific category
	 * 
	 * @param name
	 * @return
	 */
	// public static List<Player> findUsersPlayersCategorisedWith(String
	// category) {
	//
	// return find.where()
	// .eq("user",
	// user)
	// .eq("category", category)
	// .findList();
	//
	// }

	// public static List<QuestionCategory> findPlayerQuestionsCategories(int
	// playernumber){
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append("SELECT * FROM QuestionCategory ");
	// sb.append("WHERE id IN ");
	// sb.append("(SELECT questioncategories_id FROM Question_QuestionCategory
	// qqc JOIN Question q ON qqc.Question_id = q.id ");
	// sb.append("WHERE Question_id IN ");
	// sb.append("(SELECT questions_id FROM Player_Question pq INNER JOIN Player
	// p ON pq.Player_id = p.id where playernumber = '");
	// sb.append(playernumber);
	// sb.append("'));");
	//
	// String queryString = sb.toString();
	// //System.out.println(queryString);
	// Query query = JPA.em().createNativeQuery(queryString,
	// QuestionCategory.class);//.createNativeQuery(queryString);
	// List<QuestionCategory> result = query.getResultList();
	// return result;
	// }

	// public static List<Question> findPlayerQuestionsCategorisedWith(String
	// questioncategory, int playernumber){
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append("SELECT * FROM Question ");
	// sb.append("WHERE id IN ");
	// sb.append("(SELECT questions_id FROM Player_Question pq INNER JOIN Player
	// p ON pq.Player_id = p.id where playernumber = '");
	// sb.append(playernumber);
	// sb.append("') ");
	// sb.append("AND id IN ");
	// sb.append("(SELECT Question_id FROM Question_QuestionCategory qqc INNER
	// JOIN QuestionCategory qc ON qqc.questioncategories_id = qc.id where name
	// = '");
	// sb.append(questioncategory);
	// sb.append("');");// ');");
	// String queryString = sb.toString();
	// Query query = JPA.em().createNativeQuery(queryString,
	// Question.class);//.createNativeQuery(queryString);
	// List<Question> result = query.getResultList();
	// return result;
	//
	// }

	/**
	 * Get players belonging to several categories
	 * 
	 * @param name
	 * @return
	 */
	// public static List<Player> findCategorisededWith(String... categories) {
	// return Player.find(
	// "select distinct p from Player p join p.categories as t where t.name in
	// (:categories) group by p.id, p.playername, p.coach having count(t.id) =
	// :size"
	// ).bind("categories", categories).bind("size", categories.length).fetch();
	// }

	public String toString() {
		return playername;
	}

}
