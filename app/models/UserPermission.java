package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import be.objectify.deadbolt.java.models.Permission;

@Entity
public class UserPermission extends Model implements Permission{

	@Id
	public Long id;

	@Column(name = "permission_value")
	public String value;

	public static final Model.Finder<Long, UserPermission> find = new Model.Finder<>(UserPermission.class);
	
	@Override
	public String getValue() {

		return value;
	}
	
    public static UserPermission findByValue(String value)
    {
        return find.where()
                   .eq("value",
                       value)
                   .findUnique();
    }

}
