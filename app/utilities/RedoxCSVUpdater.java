
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;

public class RedoxCSVUpdater {
	
	Double fordAdjustment = 0.238;
	Double fortAdjustment = 1.174;
	
	// read the header and write out as is
	// get the index of the column with the header 'SESS_START'
	// read the file line by line
	//	for each line, get the session start time and put it as a map key with the line as a value
	//	sort the map according to key
	
	// iterate thru the map, and write out the new file line by line
	
	String header;
	String playername = "";
	
	Map<Long, String> dataEntries = new TreeMap<Long, String>();
	Map<Long, String> timestamps = new TreeMap<Long, String>();
	Map<Long, String> timestampnotes = new TreeMap<Long, String>();
	Map<Long, Double[]> timestamps1 = new TreeMap<Long, Double[]>();
	//Map<Long, Integer> loads = new HashMap<Long, Integer>();
	
//	public Map<Long, Integer> getLoads(){
//		return loads;
//	}

	public String getHeader() {
		return header;
	}
	
	
	public void addNoteCSVFile(String fileName, String timekeytochange, String additionalNote){
		BufferedReader fileReader = null;
		String line ="";
		
		//int player_name = 0;
		int timekey = 0;
		int notes = 0;
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// this first line should contain the attribute labels
			header = fileReader.readLine();
			// leave header alone
			// put it in the map with the key = 0 ( will always be the first entry)
			dataEntries.put((long) 0, header);
			
			String[] headertokens = header.split(",");
			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of the ID column
			 //player_name = headerstrings.indexOf("playername");
			 timekey = headerstrings.indexOf("TEST_TIME");
			 notes = headerstrings.indexOf("NOTES");
			 
			 
			 long targetkey = Long.parseLong(timekeytochange);
			
			//Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				
				// split the line into string tokens
				String[] tokens = line.split(",");
				String originalNote = "";
				String newNote = "";
			
				long sourcekey = Long.parseLong(tokens[timekey]);
				
				if(sourcekey == targetkey) {

					// get the notes already associated and append the new notes
					originalNote = tokens[notes];
					newNote = originalNote + ":" + additionalNote;
					tokens[notes] = newNote;
					
					String replacementLine = "";
					for(String s : tokens){
						replacementLine += s + ",";
					}
					dataEntries.put(sourcekey, replacementLine);
					
				} else {
					
					dataEntries.put(sourcekey, line);
				}
				

			} // end while loop
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		// now write out the file again
		writeOutFile(fileName, dataEntries);

		
	}
	
	

	
	
	public void toggleStateCSVFile(String fileName, String timekeytochange){
		BufferedReader fileReader = null;
		String line ="";
		
		//int player_name = 0;
		int timekey = 0;
		int notes = 0;
		int fordreadings = 0;
		int fordinc = 0;
		int fordmean = 0;
		int fordadj = 0;
		int fortreadings = 0;
		int fortinc = 0;
		int fortmean = 0;
		int fortadj = 0;
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// this first line should contain the attribute labels
			header = fileReader.readLine();
			// leave header alone
			// put it in the map with the key = 0 ( will always be the first entry)
			timestamps.put((long) 0, header);
			
			String[] headertokens = header.split(",");
			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of the ID column
			 //player_name = headerstrings.indexOf("playername");
			 timekey = headerstrings.indexOf("TEST_TIME");
			 notes = headerstrings.indexOf("NOTES");
			 fordreadings = headerstrings.indexOf("FORD");
			 fordinc = headerstrings.indexOf("FORD_INC");
			 fordmean = headerstrings.indexOf("FORD_MEAN");
			 fordadj = headerstrings.indexOf("FORD_ADJ");
			 fortreadings = headerstrings.indexOf("FORT");
			 fortinc = headerstrings.indexOf("FORT_INC");
			 fortmean = headerstrings.indexOf("FORT_MEAN");
			 fortadj = headerstrings.indexOf("FORT_ADJ");
			 
			
			//Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				
				// split the line into string tokens
				String[] tokens = line.split(",");
				playername = tokens[0];
				
				Double[] datapoints = new Double[tokens.length];
				for(int i = timekey; i < tokens.length; ++i){
					if(tokens[i].equalsIgnoreCase("0") || tokens[i].equalsIgnoreCase("")){
						datapoints[i] = 0.0;
					}else {
						datapoints[i] = Double.parseDouble(tokens[i]);
					}
				}
				
				Long key = Long.parseLong(tokens[timekey]);
				timestamps1.put(key, datapoints);
				
				String otherData = "";
				for(int i = 1; i < timekey; ++i){
					otherData += tokens[i] +",";
				}
				
				
				timestampnotes.put(key, otherData);
				
					

			} // end while loop
			
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		
		// find the corresponding key and toggle the included state for ford and fort
		for (Map.Entry<Long, Double[]> entry : timestamps1.entrySet()) {
			Long sourcekey = Long.parseLong(timekeytochange);
			//System.out.println("looking for " + sourcekey);
			if(sourcekey.equals(entry.getKey())){

				// toggle that state of the FORD_INC and FORT_INC values
				if(entry.getValue()[fordinc].equals(1.0)){
					entry.getValue()[fordinc] = 0.0;
					entry.getValue()[fortinc] = 0.0;
				} else {
					entry.getValue()[fordinc] = 1.0;
					entry.getValue()[fortinc] = 1.0;
				}
			}
		}
		
		
		// now recalculate the means and adjusted
		List<Double> fords = new ArrayList<Double>();
		List<Double> forts = new ArrayList<Double>();
		
		Double runningfordavg = 0.0;
		Double runningfortavg = 0.0;
		
		Double fordadjusted = 0.0;
		Double fortadjusted = 0.0;
		
		boolean firstentry = true;
		
		for (Map.Entry<Long, Double[]> entry : timestamps1.entrySet()) {
			
		// ignore the first entry
			if(firstentry){
				fords.add(entry.getValue()[fordreadings]);
				forts.add(entry.getValue()[fortreadings]);
				firstentry = false;
			} else {
				
				//updating the ford values
				Double fordtotal = 0.0;
				for(Double d : fords){
					fordtotal += d;
				  }
				runningfordavg = fordtotal/fords.size();
				entry.getValue()[fordmean] = runningfordavg;
				
				fordadjusted = runningfordavg - (runningfordavg * fordAdjustment);
				entry.getValue()[fordadj] = fordadjusted;
				
				//updating the fort values
				Double forttotal = 0.0;
				for(Double d : forts){
					forttotal += d;
				  }
				runningfortavg = forttotal/forts.size();
				entry.getValue()[fortmean] = runningfortavg;
				
				fortadjusted = runningfortavg * fortAdjustment;
				entry.getValue()[fortadj] = fortadjusted;
				
				
				// only add the ford and fort readings if it is to be included in the mean calculation
				if(entry.getValue()[fordinc] > 0.0){
					fords.add(entry.getValue()[fordreadings]);
					forts.add(entry.getValue()[fortreadings]);
				}
				
				
			}
		  
		}

		
		// convert array back into string and add it to the timestamps map
		
		for (Map.Entry<Long, Double[]> entry : timestamps1.entrySet()) {
			
			  String dataline = "";
			  if(timestampnotes.containsKey(entry.getKey())){
				  dataline += playername + "," + timestampnotes.get(entry.getKey());
			  } else {
				  dataline += playername + ",";
			  }
			  
			  for(int i = 0; i < entry.getValue().length; ++i){
					
				  // skipping the first 2 null array positions
				  if(entry.getValue()[i] != null){
					  if(i == timekey){
						  dataline += entry.getKey() + ",";
					  } else {
						  dataline += entry.getValue()[i] + ",";
					  }
					  
				  }
			  }
			  //System.out.println(dataline);
			  timestamps.put(entry.getKey(), dataline);
			  
			}
		
		// now write out the file again
		writeOutFile(fileName, timestamps);
	}
	
	
	
	void writeOutFile(String fileName, Map<Long, String> data){
		
		File file = new File(fileName);
		// find the next available filename
		
		// create the new file
		if ( !file.exists() ){
			try {
				
				file.createNewFile();
			} catch (IOException e1) {
				// TODO return an error message to the user
				e1.printStackTrace();
			}
	      }	
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(file, "UTF-8");
			
			for (Map.Entry<Long, String> entry : data.entrySet()) {
				 // System.out.println(entry.getKey() + ": " + entry.getValue());
				  out.println(entry.getValue());
				}

			
		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		}	finally {
			out.flush();
			out.close();
		}
	}

}

