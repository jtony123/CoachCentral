package models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class Questionnaire extends Model {
	
	@Id
	public Long id;
	
	//@Required
	@ManyToOne
	public Player player;
	
	//@Required
	public Date datetime;
	
	public String notes;
	
	public String injurylog;
	
	@OneToMany(mappedBy="questionnaire", cascade=CascadeType.ALL)
	public List<Answer> answers;
	
	
	public Questionnaire (Player player, Date datetime){
		
		this.player = player;	
		this.datetime = datetime;
		answers= new ArrayList<Answer>();
		
	}
	
	public void addPlayerQuestionnaireNote(String note) {
		this.notes = note;
		this.save();
	}
	
	public void addPlayerQuestionnaireInjuryLog(String injurylog) {
		this.injurylog = injurylog;
		this.save();
	}
	
	public void addPlayerQuestionnaireAnswer(Answer answer){
		answers.add(answer);
		
//		if(answer.answerValue > answer.question.flagThreshold){
//			this.outOfRange = true;
//		} else {
//			this.outOfRange = false;
//		}
		
		this.save();
	}

}
