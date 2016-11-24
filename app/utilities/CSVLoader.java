package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Anthony Jackson
 *
 *	CSVLoader parses the csv file that contains all the players gps data and
 *	generates an individual file for each player with the acute, chronic and squad statistics added
 *
 */

public class CSVLoader {

	String header;
	String additionalColumnHeaders = ",ACUTE_LOAD,CHRONIC_LOAD,SQUAD_LOAD,SQUAD_STAN_DEV";
	
	Map<Integer, ArrayList<String>> playerfiles = new HashMap<Integer, ArrayList<String>>();
	Map<String, ArrayList<Integer>> squadacuteloads = new HashMap<String, ArrayList<Integer>>();
	
	
	Map<Integer, CircularFifoBuffer> playeracuteloads = new HashMap<Integer, CircularFifoBuffer>();
	Map<Integer, CircularFifoBuffer> playerchronicloads = new HashMap<Integer, CircularFifoBuffer>();
	
	
	
	public Map<Integer, ArrayList<String>> getPlayerfiles() {
		return playerfiles;
	}

	public String getHeader() {
		return header;
	}

	public void loadCSVFile(String fileName){
		BufferedReader fileReader = null;
		String line ="";
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// this first line should contain the attribute labels
			header = fileReader.readLine();
			header = header.replaceAll("\\.", "_");
			header = header.replaceAll("Dates", "date");
			header+=additionalColumnHeaders;
			String[] headertokens = header.split(",");

			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of the ID column
			int idindex = headerstrings.indexOf("ID");
			int playerloadindex = headerstrings.indexOf("T_PLAYER_LOAD");
			int dateindex = headerstrings.indexOf("date");
			
//			System.out.println("id index found"+idindex);
//			System.out.println("load index found"+playerloadindex);
//			System.out.println("date index found"+dateindex);
			
			//Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				//replace all NA values with 0
				line = line.replaceAll("NA", "0");
				
				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the player id from the thrid column
				Integer playerid = Integer.parseInt(tokens[idindex]);
				String date = tokens[dateindex];
				
				int acutetotal = 0;
				int chronictotal = 0;
				// if a new player encountered
				if(playerfiles.containsKey(playerid)) {
					// add the acute load to the queue
					playeracuteloads.get(playerid).add(tokens[playerloadindex]);
					playerchronicloads.get(playerid).add(tokens[playerloadindex]);
					// calculate the acute load
					
					for(Object o : playeracuteloads.get(playerid).toArray()){
						if(((String)o).equals("")){
							
						} else {
						acutetotal += Integer.parseInt((String)o);
						}
					}
					acutetotal = acutetotal/7;
					
					
					
					for(Object o : playerchronicloads.get(playerid).toArray()){
						if(((String)o).equals("")){
							
						} else {
							chronictotal += Integer.parseInt((String)o);
						}
						
					}
					chronictotal = chronictotal/28;
					
					line += "," + acutetotal + "," + chronictotal;
					playerfiles.get(playerid).add(line);
					
					
					
				} else {
					line += ",0,0";
					ArrayList<String> datapoints = new ArrayList<String>();
					datapoints.add(line);
					playerfiles.put(playerid, datapoints);
					
					CircularFifoBuffer acuteload = new CircularFifoBuffer(7);
					acuteload.add(tokens[playerloadindex]);
					
					CircularFifoBuffer chronicload = new CircularFifoBuffer(28);
					chronicload.add(tokens[playerloadindex]);
					
					playeracuteloads.put(playerid, acuteload);
					playerchronicloads.put(playerid, chronicload);
				}
				
				if(squadacuteloads.containsKey(date)){
					squadacuteloads.get(date).add(acutetotal);
				} else {
					ArrayList<Integer> dateacuteloads = new ArrayList<Integer>();
					dateacuteloads.add(acutetotal);
					squadacuteloads.put(date, dateacuteloads);
				}

			} // end while loop
			
			// calculate the squad average for each date
			// append the squad average to each line of each players file
	        Map<Integer, ArrayList<String>> map = playerfiles;
	        		for (Entry<Integer, ArrayList<String>> entry : map.entrySet())
	        		{
	        			ArrayList<String> newdata = new ArrayList<String>();
	        			
	        			for (String s : entry.getValue()){

	    					String[] tokens = s.split(",");
	    					// get the player id from the relevant column
	    					String date = tokens[dateindex];
	    					
	    					// calculate the squad average for this date
	    					int squadtotalperdate = 0;
	    		        	for(Integer i : squadacuteloads.get(date)){
	    		        		squadtotalperdate += i;
	    		        	}
	    		        	int squadavg = squadtotalperdate/playerfiles.size();
	    		        	
	    		        	// calculate the standard deviations
	    		        	List<Double> deviations = new ArrayList<Double>();
	    		        	for(Integer i : squadacuteloads.get(date)){
	    		        		deviations.add(Math.pow((i-squadavg), 2));
	    		        	}
	    		        	
	    		        	// calculate the variance
	    		        	double variance = 0;
	    		        	for(Double d : deviations){
	    		        		variance+=d;
	    		        	}
	    		        	variance = variance/deviations.size();
	    		        	
	    		        	int standardDeviation = (int) Math.sqrt(variance);
	    		        	//System.out.println("sd = " + standardDeviation);
	    		        	
	    		        	
	    		        	// append the squad average to this data line for this player
	    		        	s = s + ","+squadavg+","+standardDeviation;
	    		        	
	    		        	// add the new line to the new list
	    		        	newdata.add(s);
	        			}
	        			// replace the old list with the new list
	        			entry.setValue(newdata);
	        		    
	        		}
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		} 
	}
	
}



