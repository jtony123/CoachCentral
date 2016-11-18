package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class Answer extends Model {

	@Id
	public Long id;
	
	//@Required
	@ManyToOne
	public Question question;
	
	
	@ManyToOne
	public Questionnaire questionnaire;
	
	
	//@Required
	public String answer;
	
	//@Required
	public int answerValue;
	
	public boolean raiseFlag;
	
	public Answer (Question question, String answer, int answerValue){
		
		this.question = question;	
		this.answer = answer;
		this.answerValue=answerValue;
		
	}
}
