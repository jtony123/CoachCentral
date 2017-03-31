package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class RedoxUtilities {
	
	static int fileVersion = 1;
	static String fileExtension = ".csv";
	// column order is important!
	String defaultheader = "playername,"
			+"ResultsShow,NOTES,POTOUT,ACTION,"
			+"TrainedToday,AteToday,ExerciseGym,ExerciseTraining,ExerciseGame,ExerciseRest,ExerciseOther,"
			+"EnergyLevel,MuscleSoreness,"
			+"Fever,SoreThroat,Headache,JointorMuscleAche,Diarrhea,Other,"
			+"TEST_TIME,DEFENCE,DEFENCE_INC,DEFENCE_MEAN,DEFENCE_CDT,STRESS,STRESS_INC,STRESS_MEAN,STRESS_CDT";
	
	
	
	Double defenceAdjustment = 0.238;
	Double stressAdjustment = 1.174;
	
	String header;
	
//	Map<String, ArrayList<String>> playerfilesbyname = new HashMap<String, ArrayList<String>>();
//	Map<String, ArrayList<String>> playerfiles = new HashMap<String, ArrayList<String>>();

//	public Map<String, ArrayList<String>> getPlayerfilesbyname() {
//		return playerfiles;
//	}
	
	public String createFile(String filepath, String filename) {

		File file = new File(filepath + filename + fileVersion + fileExtension);
		// find the next available filename
		while (file.exists()) {
			++fileVersion;
			file = new File(filepath + filename + fileVersion + fileExtension);
		}
		// create the new file
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO return an error message to the user
				e1.printStackTrace();
			}
		}

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

			out.println(defaultheader);


		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

		return filename + fileVersion + fileExtension;
	}
	
	public Map<String, ArrayList<String>> getDemoRedoxData(String fileName){
		
		
		
		Map<String, ArrayList<String>> playerdatabyname = new HashMap<String, ArrayList<String>>();
		
		BufferedReader fileReader = null;
		String line = "";
		//String newLine = "";
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// this next line should contain the column headings
			header = fileReader.readLine();

			// split the line into an array
			String[] headertokens = header.split(",");
			for(int i = 0; i<headertokens.length; ++i){
				headertokens[i] = headertokens[i].replaceAll(" ", "").trim();
				
			}

			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of each of the required columns of data
			int nameindex = headerstrings.indexOf("playername");
			int testtime = headerstrings.indexOf("test_date");
			int eatentoday = headerstrings.indexOf("Eaten");
			int defence = headerstrings.indexOf("FORD");
			int stress = headerstrings.indexOf("FORT");
			
			// Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				// replace all NA values with 0
				line = line.replaceAll("NA", "0");

				// split the line into string tokens
				String[] tokens = line.split(",");
				
				// check for blank lines
				if(tokens.length > 1){
					if(tokens.length > 17){
					if( tokens[0].length() > 3){
					
					String playername = tokens[nameindex];
					String timestring = tokens[testtime];
					String fordstring = tokens[defence];
					String fortstring = tokens[stress];
					
					//System.out.println("timestring is "+timestring);
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
					sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date utctime = null;
					try {
						utctime = sdf.parse(timestring);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Date actual = new Date(utctime.getTime());
					//System.out.println("date read is "+actual);
					// generate a unix timestamp from this date
					Long starttimesecs = actual.getTime();
	
					// if the map already contains this player name as a key
					if (playerdatabyname.containsKey(playername)) {
						
							String entry = playername + ",,";
							for(int i = eatentoday; i < defence; ++i){
								entry += tokens[i] + ",";
							}
							
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,1.1,"
							+ fortstring + ",1,0,2.5";
							
							playerdatabyname.get(playername).add(entry);
							
	
	
	
					} else {
						// new player encountered(not already in map)
						// instantiate a new list of strings for this player

							String entry = playername + ",,";
							for(int i = eatentoday; i < defence; ++i){
								entry += tokens[i] + ",";
							}
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,1.1,"
							+ fortstring + ",1,0,2.5";
							
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(entry);
							
							playerdatabyname.put(playername, temp);
							
	
						
					} // end check for blamk lines
				
				}}} // end if check for blank lines

			} // end while loop reading files
			
			
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		return playerdatabyname;
	}
	
	
	public Map<String, ArrayList<String>> getRedoxData(String fileName){
		
		Map<String, ArrayList<String>> playerdatabyname = new HashMap<String, ArrayList<String>>();
		BufferedReader fileReader = null;
		String line = "";
		String newLine = "";
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));


			// this next line should contain the column headings
			header = fileReader.readLine();

			// split the line into an array
			String[] headertokens = header.split(",");
			for(int i = 0; i<headertokens.length; ++i){
				headertokens[i] = headertokens[i].replaceAll(" ", "").trim();
				
			}

			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of each of the required columns of data
			int nameindex = headerstrings.indexOf("playername");
			int testtime = headerstrings.indexOf("test_date");
			int trainedtoday = headerstrings.indexOf("Trained Today");
			int defence = headerstrings.indexOf("FORD");
			int stress = headerstrings.indexOf("FORT");
			
			// Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				// replace all NA values with 0
				line = line.replaceAll("NA", "0");
				newLine = "";
//				System.out.println("line is : " + line);
//				System.out.println("before tokenizing");
				// split the line into string tokens
				String[] tokens = line.split(",");
//				System.out.println("token size is "+tokens.length);
//				for(String s : tokens){
//					System.out.println(s);
//				}
				// get the players name from the relevant column
				
				// check for blank lines
				if(tokens.length > 1){
					if(tokens.length > 22){
					if( tokens[0].length() > 3){
					
					String playername = tokens[nameindex];
					String timestring = tokens[testtime];
					
				
					String fordstring = tokens[defence];
					String fortstring = tokens[stress];
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date utctime = null;
					try {
						utctime = sdf.parse(timestring);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Date actual = new Date(utctime.getTime());
					// generate a unix timestamp from this date
					Long starttimesecs = actual.getTime()/1000;
	
					// if the map already contains this player name as a key
					if (playerdatabyname.containsKey(playername)) {
						
							String entry = playername + ",,";
							for(int i = trainedtoday; i < defence; ++i){
								entry += tokens[i] + ",";
							}
							
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,1.1,"
							+ fortstring + ",1,0,2.5";
							
							playerdatabyname.get(playername).add(entry);
							
	
	
	
					} else {
						// new player encountered(not already in map)
						// instantiate a new list of strings for this player
						
							String entry = playername + ",,";
							for(int i = trainedtoday; i < defence; ++i){
								entry += tokens[i] + ",";
							}
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,1.1,"
							+ fortstring + ",1,0,2.5";
							
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(entry);
							
							playerdatabyname.put(playername, temp);
							
	
						
					} // end check for blamk lines
				
				}}} // end if check for blank lines

			} // end while loop reading files
			
			
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		return playerdatabyname;
	}

	
	public String updateFile(String filepath, List<String> datapoints, boolean append){
		
		File file = new File(filepath);
		
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
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
			
			for(String datapoint : datapoints){		

				out.println(datapoint);
			}
			
		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		}	finally {
			out.flush();
			out.close();
		}
		
		return filepath;
	}
	
	
	public void toggleStateCSVFile(String fileName, String timekeytochange){
		
		String header;
		String playername = "";
		
		Map<Long, String> dataEntries = new TreeMap<Long, String>();
		Map<Long, String> timestamps = new TreeMap<Long, String>();
		Map<Long, String> timestampnotes = new TreeMap<Long, String>();
		Map<Long, Double[]> timestamps1 = new TreeMap<Long, Double[]>();
		BufferedReader fileReader = null;
		String line ="";
		
		//int player_name = 0;
		int timekey = 0;
		int notes = 0;
		int defencereadings = 0;
		int defence_inc = 0;
		int defence_mean = 0;
		int defence_cdt = 0;
		
		int stress_readings = 0;
		int stress_inc = 0;
		int stress_mean = 0;
		int stress_cdt = 0;
		
		
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
			 defencereadings = headerstrings.indexOf("DEFENCE");
			 defence_inc = headerstrings.indexOf("DEFENCE_INC");
			 defence_mean = headerstrings.indexOf("DEFENCE_MEAN");
			 defence_cdt = headerstrings.indexOf("DEFENCE_CDT");
			 
			 stress_readings = headerstrings.indexOf("STRESS");
			 stress_inc = headerstrings.indexOf("STRESS_INC");
			 stress_mean = headerstrings.indexOf("STRESS_MEAN");
			 stress_cdt = headerstrings.indexOf("STRESS_CDT");
			 
			 
			
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
						try{
							datapoints[i] = Double.parseDouble(tokens[i]);
						} catch (Exception e) {
							datapoints[i] = 0.0;
						}
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
		
		
		// find the corresponding key and toggle the included state for defence and stress
		for (Map.Entry<Long, Double[]> entry : timestamps1.entrySet()) {
			Long sourcekey = Long.parseLong(timekeytochange);
			//System.out.println("looking for " + sourcekey);
			if(sourcekey.equals(entry.getKey())){

				// toggle that state of the FORD_INC and FORT_INC values
				if(entry.getValue()[defence_inc].equals(1.0)){
					entry.getValue()[defence_inc] = 0.0;
					entry.getValue()[stress_inc] = 0.0;
				} else {
					entry.getValue()[defence_inc] = 1.0;
					entry.getValue()[stress_inc] = 1.0;
				}
			}
		}
		
		
		// calculating the averages and critical differences for stress and defence
		
		FIFOQueue defencequeue = new FIFOQueue(7);
		FIFOQueue stressqueue = new FIFOQueue(7);
		
		boolean firstentry = true;
		
		for (Map.Entry<Long, Double[]> entry : timestamps1.entrySet()) {
			
		// ignore the first entry
			if(firstentry){
				
				defencequeue.add(entry.getValue()[defencereadings]);
				stressqueue.add(entry.getValue()[stress_readings]);
				
				
				firstentry = false;
			} else {
				
				
				entry.getValue()[defence_mean] = defencequeue.getAverage();
				entry.getValue()[defence_cdt] = defencequeue.getAverage() - (defencequeue.getAverage() * defenceAdjustment);
				
				entry.getValue()[stress_mean] = stressqueue.getAverage();
				entry.getValue()[stress_cdt] = (stressqueue.getAverage() * stressAdjustment);
				
				// only add the defence and stress readings if it is to be included in the mean calculation
				if(entry.getValue()[defence_inc] > 0.0){
					defencequeue.add(entry.getValue()[defencereadings]);
					stressqueue.add(entry.getValue()[stress_readings]);
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
	
	
	
	
	public void addNoteCSVFile(String fileName, String timekeytochange, String additionalNote){
		Map<Long, String> dataEntries = new TreeMap<Long, String>();
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
	
	

}
