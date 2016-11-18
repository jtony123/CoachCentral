package models;

import java.io.File;
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

@Entity
public class Player extends Model {

	@Id
	public Long id;

	// @Required
	public String playername;

	// @Required
	public Integer playernumber;

	public Date dateadded;

	public Blob playerPhoto;

	@Column(columnDefinition = "VARBINARY")
	 public File file;
	
	 public String filename;

	// @Required
	@ManyToMany(cascade = CascadeType.ALL)
	public List<User> users;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public Set<Category> categories;

	 @ManyToMany(cascade=CascadeType.PERSIST)
	 public Set<Question> questions;
	
	 @OneToMany(mappedBy="player", cascade=CascadeType.ALL)
	 public List<Questionnaire> questionnaire;

	public Player(String playerName, Integer playerNumber, Blob playerPhoto, User user) {
		this.playername = playerName;
		this.playernumber = playerNumber;
		if (playerPhoto != null) {
			this.playerPhoto = playerPhoto;
		}
		this.users = new ArrayList<User>();
		//this.users.add(user);
		this.dateadded = new Date();

		// this.questionnaire = new ArrayList<Questionnaire>();
		this.categories = new TreeSet<Category>();
		// all players are automatically categorised in All
		this.categoriseItWith("All");

		// this.questions = new TreeSet<Question>();

	}

	public static Finder<Long, Player> find = new Finder<>(Player.class);

	public Set<Category> getCategories() {
		return categories;
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

	public Player updatePhoto(Blob photo) {

		this.playerPhoto = photo;
		this.save();
		return this;
	}

	// public Player previous() {
	//
	// Player player = Player.find("select distinct p from Player p where
	// p.coach=?1 AND p.playernumber < ?2 AND p.categories.size > ?3 order by
	// playernumber desc", this.coach, playernumber, 0).first();
	// if(player == null) {
	// player = Player.find("select distinct p from Player p where p.coach=?1
	// AND p.categories.size > ?2 order by playernumber desc", this.coach,
	// 0).first();
	// }
	// return player;
	// }

	// public Player next() {
	// Player player = Player.find("select distinct p from Player p where
	// p.coach=?1 AND p.playernumber > ?2 AND p.categories.size > ?3 order by
	// playernumber asc", this.coach, playernumber, 0).first();
	// if(player == null) {
	// player = Player.find("select distinct p from Player p where p.coach=?1
	// AND p.categories.size > ?2 order by playernumber asc", this.coach,
	// 0).first();
	// }
	//
	// return player;
	// }

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
