var acutetooltip = d3.select("body").append("div")   
	.attr("class", "acutetooltip")               
	.style("opacity", 0)
	.style("z-index", 999999);
var rdxtip = d3.select("body").append("div")   
	.attr("class", "rdxtip")
	.attr("id", "rdxtip")
	.style("font", "8px")
	.style("opacity", 1)
	.style("z-index", 999999);
var rdxnotes = function(d){
	
	var notesarray = d.NOTES.split(':');
	
	var text = "<p><font face='verdana' size='3' color='black'>Date : " + d.TEST_TIME.toString().substring(0, 15) + "</p>"
			+ "<p><font face='verdana' size='3' color='purple'>Anti-Oxidant : " + d.FORT + "</p>"
			+ "<p><font face='verdana' size='3' color='blue'>Pro-Oxidant : " + d.FORD  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Energy Level : " + d.EnergyLevel  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>MuscleSoreness : " + d.MuscleSoreness  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Trained Today : " + d.TrainedToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Ate Today : " + d.AteToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Exercised Gym Yesterday     : " + d.ExerciseGymYesterday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Exercise Training Yesterday : " + d.ExerciseTrainingYesterday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Exercise Game Yesterday     : " + d.ExerciseGameYesterday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Exercise None Yesterday     : " + d.ExerciseNoneYesterday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Exercise Other Yesterday    : " + d.ExerciseOtherYesterday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Fever Today : " + d.SymptomFeverToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Fever Previously : " + d.SymptomFeverPreviously  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Sore Throat Today : " + d.SymptomSoreThroatToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Sore Throat Previously : " + d.SymptomSoreThroatPreviously  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Headache Today : " + d.SymptomHeadacheToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Headache Previously : " + d.SymptomHeadachePreviously  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Joint or Muscle Ache Today : " + d.SymptomJointorMuscleAcheToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Joint or Muscle Ache Previously : " + d.SymptomJointorMuscleAchePreviously  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Diarrhea Today : " + d.SymptomDiarrheaToday  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Diarrhea Previously : " + d.SymptomDiarrheaPreviously  + "</p>"
			+ "<p><font face='verdana' size='2' color='blue'>Symptom Other : " + d.SymptomOther  + "</p>";
	
			for(var i=0; i<notesarray.length; i++){
				text += "<p>" + notesarray[i].trim() + "</p>";
			}
			
			//@restrict(roles = allOfGroup("redoxadmin")) {
				text += "<a title='add notes' href='#' onclick='addRdxNote("+d.TIME_KEY+ "); return false;'>" + "Add note" +"</a>";
				
			//}
			
  		return text;
}
var gametiptable = function(d){
	
	var test = d.ACUTE > (d.CHRONIC * acuteloadupperthreshold) | d.ACUTE < (d.CHRONIC * acuteloadlowerthreshold);
	
	var header = "<table id='"+"gametiptable"+"'><tr><th>Data</th><th>Value</th></tr>";
	var topline = test ? "<tr><td>Acute Load</td><td class='acutealert'>"+ (parseInt(d.ACUTE))+" : "+ (Math.round((d.ACUTE/d.CHRONIC)*100)) +"%</td></tr>"
			: "<tr><td>Acute Load</td><td>"+ (parseInt(d.ACUTE))+" : "+ (Math.round((d.ACUTE/d.CHRONIC)*100)) +"%</td></tr>"
	var text = header.concat(topline);
	text = text.concat(
	"<tr><td>Chronic Load</td><td>"+ (parseInt(d.CHRONIC)) +"</td></tr>" +
	"<tr><td>Game Load </td><td>"+ (parseInt(d.GAME_LOAD)) +" (~"+(parseInt(d.SQUAD_AVG))+")"+"</td></tr>" +
	"<tr><td>Minutes</td><td>"+ (parseInt(d.GAME_MINS)) +"</td></tr>" +
	"<tr><td>Points</td><td>"+ (parseInt(d.POINTS)) +"</td></tr>" +
	"<tr><td>Rebounds</td><td>"+ (parseInt(d.REBOUNDS)) +"</td></tr>" +
	"<tr><td>Assists</td><td>"+ (parseInt(d.ASSISTS)) +"</td></tr>" +
	"<tr><td>Steals</td><td>"+ (parseInt(d.STEALS)) +"</td></tr>" +
	"<tr><td>Blocks</td><td>"+ (parseInt(d.BLOCKS)) +"</td></tr>" +
	"<tr><td>Fouls</td><td>"+ (parseInt(d.FOULS)) +"</td></tr>" +
	"<tr><td>Notes</td><td><a title='this shows the full notes' href='#' onclick='moreNotes(); return false;'>"+ "click for notes" +"</a></td></tr>" +
	"</table>");
  		return text;
}
//used for form links when clicking in redox reading
var linktip = d3.select("body").append("div")   
	.attr("class", "tooltip")               
	.style("opacity", 0)
	.style("z-index", 999999);
	
//called whenever mouse hovers over a data point
var tooltip = d3.select("body").append("div")   
	.attr("class", "tooltip")               
	.style("opacity", 0)
	.style("z-index", 999999);
	
var gametip = d3.select("body").append("div")   
.attr("class", "gametip")               
.style("opacity", 0)
.style("z-index", 999999);
var datatip = d3.select("body").append("div")   
.attr("class", "datatip")               
.style("opacity", 0)
.style("z-index", 999999);
var datatiptable = function(d){
	
	var test = d.ACUTE > (d.CHRONIC * acuteloadupperthreshold) | d.ACUTE < (d.CHRONIC * acuteloadlowerthreshold);
	var sessiontimediffms = d.SESS_END - d.SESS_START;
	var sessionhours = ("0" + Math.floor((sessiontimediffms % 86400000) / 3600000)).slice(-2);
	var sessionmins = ("0" + Math.floor(((sessiontimediffms % 86400000) % 3600000) / 60000)).slice(-2);
	
	var pretimediffms = d.PRE_END - d.PRE_START;
	var prehours = ("0" + Math.floor((pretimediffms % 86400000) / 3600000)).slice(-2);
	var premins = ("0" + Math.floor(((pretimediffms % 86400000) % 3600000) / 60000)).slice(-2);
	
	var practtimediffms = d.PRACT_END - d.PRACT_START;
	var practhours = ("0" + Math.floor((practtimediffms % 86400000) / 3600000)).slice(-2);
	var practmins = ("0" + Math.floor(((practtimediffms % 86400000) % 3600000) / 60000)).slice(-2);
	
	var posttimediffms = d.POST_END - d.POST_START;
	var posthours = ("0" + Math.floor((posttimediffms % 86400000) / 3600000)).slice(-2);
	var postmins = ("0" + Math.floor(((posttimediffms % 86400000) % 3600000) / 60000)).slice(-2);
	
	var othertimediffms = d.OTHER_END - d.OTHER_START;
	var otherhours = ("0" + Math.floor((othertimediffms % 86400000) / 3600000)).slice(-2);
	var othermins = ("0" + Math.floor(((othertimediffms % 86400000) % 3600000) / 60000)).slice(-2);
	
	var header = "<table id='"+"datatiptable"+"'><tr><th>Data</th><th>Value</th><th>Duration</th></tr>";
	var topline = test ? "<tr><td>Acute Load</td><td class='acutealert'>"+ (parseInt(d.ACUTE))+" : "+ (Math.round((d.ACUTE/d.CHRONIC)*100)) +"%</td><td>.</td></tr>"
			: "<tr><td>Acute Load</td><td>"+ (parseInt(d.ACUTE))+" : "+ (Math.round((d.ACUTE/d.CHRONIC)*100)) +"%</td><td>.</td></tr>"
	var text = header.concat(topline);
	text = text.concat(
	"<tr><td>Chronic Load</td><td>"+ (parseInt(d.CHRONIC)) +"</td><td>.</td></tr>" +
	"<tr><td>Session Load </td><td>"+ (parseInt(d.SESS_LOAD)) +"</td><td>"+ sessionhours +":"+ sessionmins +"</td></tr>" +
	"<tr><td>Pre Load</td><td>"+ (parseInt(d.PRE_LOAD)) +"</td><td>"+ prehours +":"+ premins +"</td></tr>" +
	"<tr><td>Practice Load</td><td>"+ (parseInt(d.PRACT_LOAD)) +"</td><td>"+ practhours +":"+ practmins +"</td></tr>" +
	"<tr><td>Post Load</td><td>"+ (parseInt(d.POST_LOAD)) +"</td><td>"+ posthours +":"+ postmins +"</td></tr>" +
	"<tr><td>Other Load</td><td>"+ (parseInt(d.OTHER_LOAD)) +"</td><td>"+ otherhours +":"+ othermins +"</td></tr>" +
	"<tr><td>Notes</td><td><a title='this shows the full notes' href='#' onclick='moreNotes(); return false;'>"+ "click for notes" +"</a></td></tr>" +
	"</table>");
  		return text;
}