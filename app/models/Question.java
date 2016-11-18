package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;


@Entity
public class Question extends Model{
	
	@Id
	public Long id;
	
	//@Required
	public String question;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<QuestionCategory> questioncategories;
	
	@OneToMany(mappedBy="question", cascade=CascadeType.ALL)
	public List<Answer> answers;
	
	public int flagThreshold;
	
	
	public Question (String question, int flagThreshold){
			
		this.question = question;
		this.answers = new ArrayList<Answer>();

		this.questioncategories = new TreeSet<QuestionCategory>();
		
		this.flagThreshold = flagThreshold;
		
	}
	
	public String toString(){
		return question;
	}
	
//	public static List<Question> findCategorisedWith(String questioncategory) {
//		
//	    return Question.find(
//	        "select distinct p from Question p join p.questioncategories as t where t.name = ?", questioncategory
//	    ).fetch();
//	}
	

	

	
//	public static List<Map> getCloud(){
//		List<Map> result=Question.find("select new map(t.question as question, count(p.id) as pound) from Player p join p.questions as t group by t.question order by t.question").fetch();
//		return result;
//	}
	

}
