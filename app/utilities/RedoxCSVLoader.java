
package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;

public class RedoxCSVLoader {
	
	Double fordAdjustment = 0.238;
	Double fortAdjustment = 1.174;

	// header contains the original column headers in the gps data file being uploaded
	// it is used to get the index of the column that the data is read from
	String header;
	

	
	// playerfilesbyname is a map of player names pointing to a list of new data records for each of the players
	
	Map<String, ArrayList<String>> playerfilesbyname = new HashMap<String, ArrayList<String>>();
	Map<String, ArrayList<String>> playerfiles = new HashMap<String, ArrayList<String>>();

	public Map<String, ArrayList<String>> getPlayerfilesbyname() {
		return playerfiles;
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
							+ fordstring + ",1,0,0,"
							+ fortstring + ",1,0,0";
							
							playerdatabyname.get(playername).add(entry);
							
	
	
	
					} else {
						// new player encountered(not already in map)
						// instantiate a new list of strings for this player
						
							String entry = playername + ",,";
							for(int i = trainedtoday; i < ford; ++i){
								entry += tokens[i] + ",";
							}
							
							entry+= starttimesecs + ","
							+ fordstring + ",1,0,0,"
							+ fortstring + ",1,0,0";
							
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
	
	
	
	
	void calculateAverages(){
		
		Iterator it = playerfilesbyname.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			
			List<String> entries = (List<String>) pair.getValue();
			List<String> replacements = new ArrayList<String>();
			
			// iterate thru this list of strings
			
			List<Double> fordreadings = new ArrayList<Double>();
			List<Double> fortreadings = new ArrayList<Double>();
			
			Double runningfordavg = 0.0;
			Double runningfortavg = 0.0;
			
			for(String s : entries){
				String rep = "";
				
				String[] tokens = s.split(",");
				Double fordreading = Double.parseDouble(tokens[3]);
				Double fortreading = Double.parseDouble(tokens[7]);
				
				if(!fordreadings.isEmpty()){
					System.out.println("not empty");
					Double newfordavg = 0.0;
					for(Double d : fordreadings){
						System.out.println("ford is "+d);
						newfordavg += d;
					}
					runningfordavg = newfordavg/fordreadings.size();
					System.out.println("runing ford avg  is "+runningfordavg);
					Double fordadjusted = runningfordavg - (runningfordavg * fordAdjustment);
					
					
					Double newfortavg = 0.0;
					for(Double d : fortreadings){
						newfortavg += d;
					}
					runningfortavg = newfortavg/fortreadings.size();
					Double fortadjusted = runningfortavg * fortAdjustment;
					
					rep = s.replace("fordinc", "1");
					rep = rep.replace("fortinc", "1");
					rep = rep.replace("ford_avg", runningfordavg.toString());
					rep = rep.replace("fort_avg", runningfortavg.toString());
					rep = rep.replace("fordavgadj", fordadjusted.toString());
					rep = rep.replace("fortavgadj", fortadjusted.toString());
				
				} else {
					//System.out.println("empty");
					// must not include first values by default
					rep = s.replace("fordinc", "1.0");
					rep = rep.replace("fortinc", "1.0");
					rep = rep.replace("ford_avg", "0.0");
					rep = rep.replace("fort_avg", "0.0");
					rep = rep.replace("fordavgadj", "0.0");
					rep = rep.replace("fortavgadj", "0.0");
				}
				
				fordreadings.add(fordreading);
				fortreadings.add(fortreading);
				replacements.add(rep);
				
			}
			playerfiles.put((String)pair.getKey(), (ArrayList<String>) replacements);
			
		}
		
	}
	
	public void loadCSVFile(String fileName) {
		
		
		
		BufferedReader fileReader = null;
		String line = "";
		String newLine = "";

		try {
			fileReader = new BufferedReader(new FileReader(fileName));


			// this next line should contain the column headings
			header = fileReader.readLine();

			// split the line into an array
			String[] headertokens = header.split(",");

			List<String> headerstrings = Arrays.asList(headertokens);
				for(String s : headerstrings){
					System.out.print(s + ":");
				}
			
			// get the index of each of the required columns of data
			int nameindex = headerstrings.indexOf("playername");
			int testtime = headerstrings.indexOf("test_date");
			
			int ford = headerstrings.indexOf("FORD");
			int fort = headerstrings.indexOf("FORT");
			

			// Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				// replace all NA values with 0
				line = line.replaceAll("NA", "0");
				newLine = "";

				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the players name from the relevant column
				
				// check for blank lines
				if(tokens[0].length() > 3){
					
				String playername = tokens[nameindex];
				String timestring = tokens[testtime];
				
				String fordstring = tokens[ford];
				String fortstring = tokens[fort];
				String fordavg = "ford_avg";
				String fortavg = "fort_avg";
				String fordavgadj = "fordavgadj";
				String fortavgadj = "fortavgadj";
				

				
				//System.out.println("line is : " + line);

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date utctime = null;
				try {
					
					utctime = sdf.parse(timestring);
					//gametime = sdf.parse("31-AUG-16");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("time parsed is "+utctime);
				
				Date actual = new Date(utctime.getTime());
				//System.out.println("after "+actual);
				
				// generate a unix timestamp from this date
				Long starttimesecs = actual.getTime()/1000;

				//System.out.println();
				

				// if the map already contains this player name as a key
				if (playerfilesbyname.containsKey(playername)) {
					
						
						String entry = playername + ",," + starttimesecs + ","
						+ fordstring + ",fordinc,"+fordavg+","+fordavgadj+","
						+ fortstring + ",fortinc,"+fortavg+","+fortavgadj;
						
						playerfilesbyname.get(playername).add(entry);
						



				} else {
					// new player encountered(not already in map)
					// instantiate a new list of strings for this player
					
						
						
					String entry = playername + ",," + starttimesecs + ","
							+ fordstring + ",fordinc,"+fordavg+","+fordavgadj+","
							+ fortstring + ",fortinc,"+fortavg+","+fortavgadj;
						
						
						
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(entry);
						
						playerfilesbyname.put(playername, temp);
						

					
				}
				
				} // end if check for blank lines

			} // end while loop reading files
			

			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		// now calculate the averages and the adjustments
		// by iterating thru' the map
		
		
		Iterator it = playerfilesbyname.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			
			List<String> entries = (List<String>) pair.getValue();
			List<String> replacements = new ArrayList<String>();
			
			// iterate thru this list of strings
			
			List<Double> fordreadings = new ArrayList<Double>();
			List<Double> fortreadings = new ArrayList<Double>();
			
			Double runningfordavg = 0.0;
			Double runningfortavg = 0.0;
			
			for(String s : entries){
				String rep = "";
				
				String[] tokens = s.split(",");
				Double fordreading = Double.parseDouble(tokens[3]);
				Double fortreading = Double.parseDouble(tokens[7]);
				
				if(!fordreadings.isEmpty()){
					System.out.println("not empty");
					Double newfordavg = 0.0;
					for(Double d : fordreadings){
						System.out.println("ford is "+d);
						newfordavg += d;
					}
					runningfordavg = newfordavg/fordreadings.size();
					System.out.println("runing ford avg  is "+runningfordavg);
					Double fordadjusted = runningfordavg - (runningfordavg * fordAdjustment);
					
					
					Double newfortavg = 0.0;
					for(Double d : fortreadings){
						newfortavg += d;
					}
					runningfortavg = newfortavg/fortreadings.size();
					Double fortadjusted = runningfortavg * fortAdjustment;
					
					rep = s.replace("fordinc", "1");
					rep = rep.replace("fortinc", "1");
					rep = rep.replace("ford_avg", runningfordavg.toString());
					rep = rep.replace("fort_avg", runningfortavg.toString());
					rep = rep.replace("fordavgadj", fordadjusted.toString());
					rep = rep.replace("fortavgadj", fortadjusted.toString());
				
				} else {
					//System.out.println("empty");
					// must not include first values by default
					rep = s.replace("fordinc", "1.0");
					rep = rep.replace("fortinc", "1.0");
					rep = rep.replace("ford_avg", "0.0");
					rep = rep.replace("fort_avg", "0.0");
					rep = rep.replace("fordavgadj", "0.0");
					rep = rep.replace("fortavgadj", "0.0");
				}
				
				fordreadings.add(fordreading);
				fortreadings.add(fortreading);
				replacements.add(rep);
				
			}
			playerfiles.put((String)pair.getKey(), (ArrayList<String>) replacements);
			
		}
		

		
		
			
	} //end loadCSVFile()
	
	
}



