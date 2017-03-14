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
			+"NOTES,"
			+"TrainedToday,AteToday,ExerciseGymYesterday,ExerciseTrainingYesterday,ExerciseGameYesterday,"
			+"ExerciseNoneYesterday,ExerciseOtherYesterday,EnergyLevel,MuscleSoreness,SymptomFeverToday,"
			+"SymptomFeverPreviously,SymptomSoreThroatToday,SymptomSoreThroatPreviously,SymptomHeadacheToday,"
			+"SymptomHeadachePreviously,SymptomJointorMuscleAcheToday,SymptomJointorMuscleAchePreviously,"
			+"SymptomDiarrheaToday,SymptomDiarrheaPreviously,SymptomOther,"
			+"TEST_TIME,FORD,FORD_INC,FORD_MEAN,FORD_ADJ,FORT,FORT_INC,FORT_MEAN,FORT_ADJ,"
			+"FORD_MEAN2,FORD_ADJ2,FORT_MEAN2,FORT_ADJ2";
	
	
	
	
	
	Double fordAdjustment = 0.238;
	Double fortAdjustment = 1.174;
	
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
			int trainedtoday = headerstrings.indexOf("TrainedToday");
			int ford = headerstrings.indexOf("FORD");
			int fort = headerstrings.indexOf("FORT");
			
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
					
				
					String fordstring = tokens[ford];
					String fortstring = tokens[fort];
					
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
							for(int i = trainedtoday; i < ford; ++i){
								entry += tokens[i] + ",";
							}
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,-1.1,"
							+ fortstring + ",1,0,-1.1"
							+",0,-1.1,0,-1.1";
							
							playerdatabyname.get(playername).add(entry);
							
	
	
	
					} else {
						// new player encountered(not already in map)
						// instantiate a new list of strings for this player
						
							String entry = playername + ",,";
							for(int i = trainedtoday; i < ford; ++i){
								entry += tokens[i] + ",";
							}
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,-1.1,"
							+ fortstring + ",1,0,-1.1"
							+",0,-1.1,0,-1.1";
							
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
		int fordreadings = 0;
		int fordinc = 0;
		int fordmean = 0;
		int fordadj = 0;
		int fortreadings = 0;
		int fortinc = 0;
		int fortmean = 0;
		int fortadj = 0;
		
		int fordmean2 = 0;
		int fordadj2 = 0;
		int fortmean2 = 0;
		int fortadj2 = 0;
		
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
			 
			 fordmean2 = headerstrings.indexOf("FORD_MEAN2");
			 fordadj2 = headerstrings.indexOf("FORD_ADJ2");
			 fortmean2 = headerstrings.indexOf("FORT_MEAN2");
			 fortadj2 = headerstrings.indexOf("FORT_ADJ2");
			 
			
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
		
		CircularDoubleBuffer fordscdb = new CircularDoubleBuffer(7);
		CircularDoubleBuffer fortscdb = new CircularDoubleBuffer(7);
		
		for (int i = 0; i<7;++i){
			fordscdb.add(1.1);
			fortscdb.add(2.5);
		}
		
		CircularDoubleBuffer fordsqueue = new CircularDoubleBuffer(7);
		CircularDoubleBuffer fortsqueue = new CircularDoubleBuffer(7);
		
//		 fordmean2 = headerstrings.indexOf("FORD_MEAN2");
//		 fordadj2 = headerstrings.indexOf("FORD_ADJ2");
//		 fortmean2 = headerstrings.indexOf("FORT_MEAN2");
//		 fortadj2 = headerstrings.indexOf("FORT_ADJ2");
		
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
				
				fordsqueue.add(entry.getValue()[fordreadings]);
				fortsqueue.add(entry.getValue()[fortreadings]);
				
				fordscdb.add(entry.getValue()[fordreadings]);
				fortscdb.add(entry.getValue()[fortreadings]);
				
				firstentry = false;
			} else {
				
				//updating the ford values
				Double fordtotal = 0.0;
				for(Double d : fords){
					fordtotal += d;
				  }
				runningfordavg = fordtotal/fords.size();
				//entry.getValue()[fordmean] = runningfordavg;
				
				fordadjusted = runningfordavg - (runningfordavg * fordAdjustment);
				//entry.getValue()[fordadj] = fordadjusted;
				
				entry.getValue()[fordmean] = fordsqueue.getAverage();
				entry.getValue()[fordadj] = fordsqueue.getAverage() - (fordsqueue.getAverage() * fordAdjustment);
				
				entry.getValue()[fordmean2] = fordscdb.getAverage();
				entry.getValue()[fordadj2] = fordscdb.getAverage() - (fordscdb.getAverage() * fordAdjustment);
				
				//updating the fort values
				Double forttotal = 0.0;
				for(Double d : forts){
					forttotal += d;
				  }
				runningfortavg = forttotal/forts.size();
				//entry.getValue()[fortmean] = runningfortavg;
				
				fortadjusted = runningfortavg * fortAdjustment;
				//entry.getValue()[fortadj] = fortadjusted;
				
				entry.getValue()[fortmean] = fortsqueue.getAverage();
				entry.getValue()[fortadj] = (fortsqueue.getAverage() * fortAdjustment);
				
				
				entry.getValue()[fortmean2] = fortscdb.getAverage();
				entry.getValue()[fortadj2] = (fortscdb.getAverage() * fortAdjustment);
				
				
				// only add the ford and fort readings if it is to be included in the mean calculation
				if(entry.getValue()[fordinc] > 0.0){
					fords.add(entry.getValue()[fordreadings]);
					forts.add(entry.getValue()[fortreadings]);
					
					fordscdb.add(entry.getValue()[fordreadings]);
					fortscdb.add(entry.getValue()[fortreadings]);
					fordsqueue.add(entry.getValue()[fordreadings]);
					fortsqueue.add(entry.getValue()[fortreadings]);
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
