package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

import utilities.FIFOQueue;



@Entity
public class Redox extends Model {
	
	@Id
	public Long id;
	

    @ManyToOne
    public Player player;
    
    @ManyToOne
    public RdxAlertReport rdxalertreport;
    
    public Date date;
    
    public String eaten;
    
    public String exercised;
    
    //
    public String fever;
    
    public String sorethroat;
    
    public String headache;
    
    public String jointmuscleache;
    
    public String diarrhea;
    
    //
    public String gymweights;
    
    public String practicetraining;
    
    public String game;
    
    public String rest;
    
    public String other;
    
    
    
    public Double energy;
    
    public Double muscleSoreness;
    
    
    
    public String orrecoScientist;
    
    public Double stress;
    
    public Double defence;
    
    
    
    public Boolean includeInCritDiff;
    
    public Double stressThreshold;
    
    public Double defenceThreshold;
    
    public String stressStatus;
    
    public String defenceStatus;
    
    
    public Redox(Player player) {
        this.player = player;
        this.player.save();
    }
    
    public Redox(Player player, Date date) {
        this.player = player;
        this.player.save();
        this.date = date;
    }
    
    public void setTestDate(Date date){
    	this.date = date;
    }
    
    
    
    public void setRedoxTestResult(String eaten, String exercised, 
    	    String fever,String sorethroat,String headache, String jointmuscleache, String diarrhea,
    	    String gymweights, String practicetraining, String game, String rest, String other,
    		Double energy, Double muscleSoreness,
    		Double stress, Double defence, Boolean includeInCritDiff) {
    	this.eaten = eaten;
    	this.exercised = exercised;
    	this.fever = fever;
    	this.sorethroat = sorethroat;
    	this.headache = headache;
    	this.jointmuscleache = jointmuscleache;
    	this.diarrhea = diarrhea;
    	
    	this.gymweights = gymweights;
    	this.practicetraining = practicetraining;
    	this.game = game;
    	this.rest = rest;
    	this.other = other;
    	

    	this.energy = energy;
    	this.muscleSoreness = muscleSoreness;
    	this.stress = stress;
    	this.defence = defence;
    	this.includeInCritDiff = includeInCritDiff;
    	
    	this.save();
    	calculateThreshold();
    	this.save();
    	
    	determineStatus();
    	
    	this.save();
    }
    
    private void determineStatus() {
    	
    	
    	List<Redox> redoxReadings = Redox.findByPlayer(player);
    	
    	for(Redox r : redoxReadings){
        	double diff = Math.abs(r.stressThreshold - r.defenceThreshold);
        	double tenpercent = diff*0.1;
        	
        	if(r.stress >= r.stressThreshold){
        		r.stressStatus = "RED";
        	} else if (r.stress >= (r.stressThreshold - tenpercent)){
        		r.stressStatus = "AMBER";
        	} else {
        		r.stressStatus = "GREEN";
        	}
        	
        	
        	if(r.defence <= r.defenceThreshold){
        		r.defenceStatus = "RED";
        	} else if (r.defence <= (r.defenceThreshold + tenpercent)){
        		r.defenceStatus = "AMBER";
        	} else {
        		r.defenceStatus = "GREEN";
        	}
        	r.determineReport(r);
        	r.save();
    	}
    
    	
    }
    
    private void determineReport(Redox r) {
    	r.rdxalertreport = RdxAlertReport.findByStatus(r.stressStatus, r.defenceStatus);
    }
    
    public void calculateThreshold(){
    	
    	this.refresh();
    	
    	Double defenceAdjustment = 0.238;
    	Double stressAdjustment = 1.174;
    	
    	List<Redox> redoxReadings = Redox.findByPlayer(player);
    	
    	

    	
    	FIFOQueue stressqueue = new FIFOQueue(7);
		FIFOQueue defencequeue = new FIFOQueue(7);
		
    	
    	boolean firstreading = true;
    	for(Redox r : redoxReadings){

    		if(firstreading){
    			r.defenceThreshold = 1.1;
    			r.stressThreshold = 2.5;
    			
    			if(r.includeInCritDiff){
    				stressqueue.add(r.stress);
    				defencequeue.add(r.defence);
    			} else {
    				// in the event that the first value is excluded
    				// we have to put the default reverse calculated value in the queue
    				//Double defenceAdjustment = 0.238;
    				//Double stressAdjustment = 1.174;
    				// for stress => 2.129
    				// for defence = > 1.44357 
    				stressqueue.add(2.129);
    				defencequeue.add(1.44357);
    			}
    			
    			firstreading = false;
    		} else {
    			
    			r.defenceThreshold = defencequeue.getAverage() - (defencequeue.getAverage() * defenceAdjustment);
    			r.stressThreshold =  stressqueue.getAverage() * stressAdjustment;
    			
    			if(r.includeInCritDiff){
    				
    				stressqueue.add(r.stress);
    				defencequeue.add(r.defence);
    			} 
    			
    		}
    		r.save();
    	}
    	
    }
    
    
    
    public void toggleIncludedState(){
    	this.includeInCritDiff = ! this.includeInCritDiff;
    	this.save();
    	calculateThreshold();
    	determineStatus();
    	this.save();
    }
    
    
    
    public static List<Redox> findByPlayer(Player player) {
        Finder<Long, Redox> finder = new Finder<>(Redox.class);
        return finder.where().eq("player", player).orderBy("date asc").findList();
    }
    
    public static Redox findByTimeKey(Player player, Date date){
    	Finder<Long, Redox> finder = new Finder<>(Redox.class);
    	List<Redox> rdxs = finder.where().eq("player", player).eq("date", date).findList();
    	System.out.println("size of list is "+rdxs.size());
        if(rdxs == null || rdxs.isEmpty()){
        	return null;
        }
        return rdxs.get(0);
    }
    
}
