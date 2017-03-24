package models;


import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//import com.github.jasminb.jsonapi.annotations.Id;
//import com.github.jasminb.jsonapi.annotations.Links;
//import com.github.jasminb.jsonapi.annotations.Meta;
//import com.github.jasminb.jsonapi.annotations.Relationship;
//import com.github.jasminb.jsonapi.annotations.Type;


//@com.github.jasminb.jsonapi.annotations.Type("alert")
@Entity
public class RdxAlertReport extends Model {
	

	@Id
    public Long Id;
	
	  
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rdxalertreport")
    public List<Redox> redoxes = new ArrayList<Redox>();
	

	
	public String stressStatus;
	
	public String defenceStatus;
    
    public String result;
    
    public String potentialoutcomes;
    
    public String actions;
    
    @Column(columnDefinition = "varchar(1024)")
    public String trainingloadadvice;
    
    @Column(columnDefinition = "varchar(1024)")
    public String dietaryadvice;
    
    @Column(columnDefinition = "varchar(1024)")
    public String sleepadvice;
    
    
    
    public RdxAlertReport(){
    }
    
  
    
    public void setAlert(String stressStatus, String defenceStatus, 
    		String result, String potentialoutcomes, String actions,
    		String trainingloadadvice, String dietaryadvice, String sleepadvice){
    	
    	this.stressStatus = stressStatus;
    	this.defenceStatus = defenceStatus;
    	this.result = result;
    	this.potentialoutcomes = potentialoutcomes;
    	this.actions = actions;
    	this.trainingloadadvice = trainingloadadvice;
    	this.dietaryadvice = dietaryadvice;
    	this.sleepadvice = sleepadvice;
    	this.save();
    }
    
    public static RdxAlertReport findByStatus(String stressStatus, String defenceStatus) {
        Finder<Long, RdxAlertReport> finder = new Finder<>(RdxAlertReport.class);
        RdxAlertReport alert = finder.where()
        		.eq("defenceStatus", defenceStatus)
        		.where()
        		.eq("stressStatus", stressStatus)
        		.findUnique();
        //System.out.println("alert found : " + alert.alertText);
        
        return alert;
    }
    
    
    public static Finder<Long, RdxAlertReport> find = new Finder<>(RdxAlertReport.class);
    
    
    
}
