package models;

import javax.persistence.*;
import play.db.ebean.*;
import play.data.validation.*;

import play.data.format.*;

@Entity
public class Person extends com.avaje.ebean.Model {

  @Id
	public Long id;

    public String name;

    public String email;


    public static Finder<Long,Person> find = new Finder<Long,Person>(
    	    Long.class, Person.class
    	  );
}
