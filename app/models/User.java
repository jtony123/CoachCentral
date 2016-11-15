package models;

import java.util.List;

import javax.persistence.*;
import play.data.validation.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;

import play.data.format.*;

@Entity
public class User extends Model implements Subject{

  @Id
	public Long id;

    public String userName;
    
    public String password;

    public String email;
    
    @ManyToMany
    public List<SecurityRole> roles;
    
    @ManyToMany
    public List<UserPermission> permissions;


    public static Finder<Long,User> find = new Finder<>(User.class);


	@Override
	public List<? extends Role> getRoles() {

		return roles;
	}


	@Override
	public List<? extends Permission> getPermissions() {

		return permissions;
	}


	@Override
	public String getIdentifier() {

		return userName;
	}
	
    public static User findByUserName(String userName)
    {
        return find.where()
                   .eq("userName",
                       userName)
                   .findUnique();
    }
	
	
}
