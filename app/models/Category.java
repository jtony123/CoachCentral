package models;

//import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
//import com.avaje.ebean.Model.Finder;

//import play.data.validation.Required;
//import play.db.jpa.*;

@Entity
public class Category extends Model implements Comparable<Category> {

	@Id
	public Long id;
	
	// @Required
	public String name;

	public Category(String name) {
		this.name = name;
	}

	public static Finder<Long, Category> find = new Finder<>(Category.class);

	public String toString() {
		return name;
	}

	public int compareTo(Category otherCategory) {
		return name.compareTo(otherCategory.name);
	}
	
    public static Category findByName(String catName)
    {
        return find.where()
                   .eq("name",
                       catName)
                   .findUnique();
    }

	public static Category findOrCreateByName(String name) {
		Category category = Category.findByName(name);
		if (category == null) {
			category = new Category(name);
			category.save();
		}
		return category;
	}
//
//	public static List<Map> getCloud() {
//		List<Map> result = Category
//				.find("select new map(t.name as category, count(p.id) as pound) from Player p join p.categories as t group by t.name order by t.name")
//				.fetch();
//		return result;
//	}

}
