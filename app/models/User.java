package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import play.data.validation.*;

import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;

import org.mindrot.jbcrypt.BCrypt;

import play.data.format.*;

@Entity
public class User extends Model implements Subject{

	@Id
	public Long id;
  
	//@Constraints.Email
  	public String email;

    public String userName;
    
    public String password;

    
    
    @ManyToMany
    public List<SecurityRole> roles;
    
    @ManyToMany
    public List<UserPermission> permissions;
    
    @ManyToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
    public List<Player> players = new ArrayList<Player>();


    public static Finder<Long,User> find = new Finder<>(User.class);
    
    


//	public String getPassword() {
//		return password;
//	}
	
	public boolean checkPassword(String pw){
		return BCrypt.checkpw(pw, this.password);
	}


	public void setPassword(String password) {
		
		
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}


	@Override
	public List<? extends Role> getRoles() {

		return roles;
	}


	@Override
	public List<? extends Permission> getPermissions() {

		return permissions;
	}
	
	//@Override
	public List<? extends Player> getPlayers() {

		return players;
	}
	
	public List<Player> getPlayersCategorisedWith(Category category){
		List<Player> categoryPlayers = new ArrayList<Player>();
		// filtering this users players by category
		for(Player player : players) {
			if(player.getCategories().contains(category)){
				categoryPlayers.add(player);
			}
		}
		return categoryPlayers;
	}
	



	@Override
	public String getIdentifier() {

		return email;
	}
	
	
	
    public static User findByUserName(String userName)
    {
        return find.where()
                   .eq("userName",
                       userName)
                   .findUnique();
    }
    
    public static User findByEmail(String email)
    {
        return find.where()
                   .eq("email",
                       email)
                   .findUnique();
    }
	
	
}
