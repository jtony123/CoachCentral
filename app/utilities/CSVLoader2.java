
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
 *	updates each individual player's CSV File.
 *	It also updates the acute, chronic and squad statistics for all the players
 *
 */

public class CSVLoader2 {

	String header;
	String newHeader;
	String additionalColumnHeaders = ",ACUTE_LOAD,CHRONIC_LOAD,SQUAD_LOAD,SQUAD_STAN_DEV";
	
	Map<Integer, ArrayList<String>> playerfiles = new HashMap<Integer, ArrayList<String>>();
	
	// using playerfilesbyname because mavs data does not include a player id with it
	Map<String, ArrayList<String>> playerfilesbyname = new HashMap<String, ArrayList<String>>();
	
	Map<String, ArrayList<Integer>> squadacuteloads = new HashMap<String, ArrayList<Integer>>();
	
	
	Map<Integer, CircularFifoBuffer> playeracuteloads = new HashMap<Integer, CircularFifoBuffer>();
	Map<Integer, CircularFifoBuffer> playerchronicloads = new HashMap<Integer, CircularFifoBuffer>();
	
	Map<String, CircularFifoBuffer> playeracuteloads2 = new HashMap<String, CircularFifoBuffer>();
	Map<String, CircularFifoBuffer> playerchronicloads2 = new HashMap<String, CircularFifoBuffer>();
	
	
	
	public Map<Integer, ArrayList<String>> getPlayerfiles() {
		return playerfiles;
	}
	
	public Map<String, ArrayList<String>> getPlayerfilesByName() {
		return playerfilesbyname;
	}

	public String getHeader() {
		return newHeader;
	}

	public void loadCSVFile(String fileName){
		BufferedReader fileReader = null;
		String line ="";
		String newLine = "";
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// must skip down to the 10th line before reading column headers
			int numlinestoskip = 9;
			
			for(int i = numlinestoskip; i > 0; i--){
				
				//System.out.println("line "+i+": "+fileReader.readLine());
				fileReader.readLine();
			}
			
			
			// this next line should contain the attribute labels
			header = fileReader.readLine();
			//header = header.replaceAll("\\.", "_");
			//header = header.replaceAll("Unix Start Time", "date");
			//header = header.replaceAll("Total Player Load", "T_PLAYER_LOAD");
			header+=additionalColumnHeaders;
			
			newHeader = "playername,date,T_PLAYER_LOAD,T_ACC_DEC_LOAD";
			newHeader+=additionalColumnHeaders;
			
			String[] headertokens = header.split(",");

			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of the ID column
			//int idindex = headerstrings.indexOf("ID");
			
			int nameindex = headerstrings.indexOf("Player Name");
			int periodname = headerstrings.indexOf("Period Name");
			
			
			int dateindex = headerstrings.indexOf("Unix Start Time");
			int playerloadindex = headerstrings.indexOf("Total Player Load");
			int playeraccelindex = headerstrings.indexOf("Acceleration Band 5 Duration %");
			
			//System.out.println("id index found"+idindex);
			System.out.println("playername index found"+nameindex);
			System.out.println("load index found"+playerloadindex);
			System.out.println("date index found"+dateindex);
			System.out.println("accel index found"+playeraccelindex);
			
			//Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				//replace all NA values with 0
				line = line.replaceAll("NA", "0");
				newLine = "";
				
				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the player id from the relevant column
				//Integer playerid = Integer.parseInt(tokens[idindex]);
				String playername = tokens[nameindex];
				String date = tokens[dateindex];
				String period = tokens[periodname];
				
				Integer load = (int)Double.parseDouble(tokens[playerloadindex]);
				String accel = tokens[playeraccelindex];
				
				int acutetotal = 0;
				int chronictotal = 0;
				
				// if period name equals session, otherwise ignore this line
				if(period.equalsIgnoreCase("Session")){
					
				
					// if a new player encountered
					if(playerfilesbyname.containsKey(playername)) {
						
						
						// add the acute load to the queue
						playeracuteloads2.get(playername).add(tokens[playerloadindex]);
						playerchronicloads2.get(playername).add(tokens[playerloadindex]);
						// calculate the acute load
						
						for(Object o : playeracuteloads2.get(playername).toArray()){
							if(((String)o).equals("")){
								
							} else {
								
							double d = Double.parseDouble((String)o);
							acutetotal += (int)d;//(String)o);
							}
						}
						acutetotal = acutetotal/7;
						
						
						
						for(Object o : playerchronicloads2.get(playername).toArray()){
							if(((String)o).equals("")){
								
							} else {
								double d = Double.parseDouble((String)o);
								chronictotal += (int)d;//Integer.parseInt((String)o);
							}
							
						}
						chronictotal = chronictotal/28;
						
						line += "," + acutetotal + "," + chronictotal;
						newLine += playername +"," + date +"," + load + "," + accel + "," + acutetotal + "," + chronictotal;
						//playerfilesbyname.get(playername).add(line);
						playerfilesbyname.get(playername).add(newLine);
						
						
					} else {
						newLine += playername +"," + date +"," + load + "," + accel + "," + "0" + "," + "0";
						line += ",0,0";
						ArrayList<String> datapoints = new ArrayList<String>();
						datapoints.add(newLine);
						//datapoints.add(line);
						playerfilesbyname.put(playername, datapoints);
						
						CircularFifoBuffer acuteload = new CircularFifoBuffer(7);
						acuteload.add(tokens[playerloadindex]);
						
						CircularFifoBuffer chronicload = new CircularFifoBuffer(28);
						chronicload.add(tokens[playerloadindex]);
						
						playeracuteloads2.put(playername, acuteload);
						playerchronicloads2.put(playername, chronicload);
					}
					
					if(squadacuteloads.containsKey(date)){
						squadacuteloads.get(date).add(acutetotal);
					} else {
						ArrayList<Integer> dateacuteloads = new ArrayList<Integer>();
						dateacuteloads.add(acutetotal);
						squadacuteloads.put(date, dateacuteloads);
					}
				
				
				} // end check period name

			} // end while loop
			
			// calculate the squad average for each date
			// append the squad average to each line of each players file
	        Map<String, ArrayList<String>> map = playerfilesbyname;
	        		for (Entry<String, ArrayList<String>> entry : map.entrySet())
	        		{
	        			ArrayList<String> newdata = new ArrayList<String>();
	        			
	        			for (String s : entry.getValue()){

	    					String[] tokens = s.split(",");
	    					// get the player id from the relevant column
	    					String date = tokens[1];
	    					
	    					// calculate the squad average for this date
	    					int squadtotalperdate = 0;
	    		        	for(Integer i : squadacuteloads.get(date)){
	    		        		squadtotalperdate += i;
	    		        	}
	    		        	int squadavg = squadtotalperdate/playerfilesbyname.size();
	    		        	
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




