package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class NameAlias extends Model{

	
	@Id
	public Long id;
	
	public String alias;
	
	@ManyToOne
	public Player player;
	
	public static Finder<Long, NameAlias> find = new Finder<>(NameAlias.class);
	
}
