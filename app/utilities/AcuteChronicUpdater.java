package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;


	/**
	 * @author Anthony Jackson
	 *
	 *
	 */

	public class AcuteChronicUpdater {

		String header;
		
		ArrayList<String> playerfiledata = new ArrayList<String>();
		//Map<String, ArrayList<Integer>> squadacuteloads = new HashMap<String, ArrayList<Integer>>();
		
		
		 CircularIntBuffer playeracuteloads = new CircularIntBuffer(7);
		 CircularIntBuffer playerchronicloads = new CircularIntBuffer(28);
		
		
		
		public ArrayList<String> getPlayerfileData() {
			return playerfiledata;
		}

		public String getHeader() {
			return header;
		}

		public void loadCSVFile(String fileName, Map<Long, Integer> gameavgloads){
			BufferedReader fileReader = null;
			String line ="";
			
			try {
				fileReader = new BufferedReader(new FileReader(fileName));
				
				// this first line should contain the attribute labels
				header = fileReader.readLine();
				// leave header alone
				playerfiledata.add(header);
				//System.out.println(header);
				
				
				String[] headertokens = header.split(",");

				List<String> headerstrings = Arrays.asList(headertokens);
				
				// get the index of the ID column
				int playernameindex = headerstrings.indexOf("playername");
				int sesstime = headerstrings.indexOf("SESS_START");
				int playerloadindex = headerstrings.indexOf("SESS_LOAD");
				int acuteindex = headerstrings.indexOf("ACUTE");
				int chronicindex = headerstrings.indexOf("CHRONIC");
				int gameloadindex = headerstrings.indexOf("GAME_LOAD");
				int sqdavg = headerstrings.indexOf("SQUAD_AVG");
				
				
				//Read the file line by line

				Integer acutetotal = 0;
				Integer chronictotal = 0;
				
				
				while ((line = fileReader.readLine()) != null) {
					
					// split the line into string tokens
					String[] tokens = line.split(",");
					// get the player name from the relevant column
					//String playername = (tokens[playernameindex]);
					//String date = tokens[dateindex];
					
					
					// if a new player encountered
					
						// check which data value is to be used
					//System.out.println(tokens[playernameindex]);
					
					if(!tokens[playerloadindex].equalsIgnoreCase("0")) {
						//System.out.println("equals 0");
						playeracuteloads.add(tokens[playerloadindex]);
						playerchronicloads.add(tokens[playerloadindex]);
					} else {
						//System.out.println("not equals 0");
						playeracuteloads.add(tokens[gameloadindex]);
						playerchronicloads.add(tokens[gameloadindex]);
					}
						// add the acute load to the queue
						
						// calculate the acute load
						
						for(Object o : playeracuteloads.toArray()){
							if(((String)o).equals("")){
								
							} else {
							acutetotal += Integer.parseInt((String)o);
							}
						}
						acutetotal = acutetotal/7;
						
						
						
						for(Object o : playerchronicloads.toArray()){
							if(((String)o).equals("")){
								
							} else {
								chronictotal += Integer.parseInt((String)o);
							}
							
						}
						chronictotal = chronictotal/28;
						
						tokens[acuteindex] = acutetotal.toString();
						tokens[chronicindex] = chronictotal.toString();
						
						if(gameavgloads != null){
							Long key = Long.parseLong(tokens[sesstime]);
							if(gameavgloads.containsKey(key)){
								tokens[sqdavg] = gameavgloads.get(key).toString();
							}
						}
						
						
						
						line ="";
						for(String s : tokens){
							line += s+",";
						}
						
						//line += "," + acutetotal + "," + chronictotal;
						playerfiledata.add(line);
						
					
					
//					if(squadacuteloads.containsKey(date)){
//						squadacuteloads.get(date).add(acutetotal);
//					} else {
//						ArrayList<Integer> dateacuteloads = new ArrayList<Integer>();
//						dateacuteloads.add(acutetotal);
//						squadacuteloads.put(date, dateacuteloads);
//					}

				} // end while loop
				
				// calculate the squad average for each date
				// append the squad average to each line of each players file
//		        Map<Integer, ArrayList<String>> map = playerfiles;
//		        		for (Entry<Integer, ArrayList<String>> entry : map.entrySet())
//		        		{
//		        			ArrayList<String> newdata = new ArrayList<String>();
//		        			
//		        			for (String s : entry.getValue()){
//
//		    					String[] tokens = s.split(",");
//		    					// get the player id from the relevant column
//		    					String date = tokens[dateindex];
//		    					
//		    					// calculate the squad average for this date
//		    					int squadtotalperdate = 0;
//		    		        	for(Integer i : squadacuteloads.get(date)){
//		    		        		squadtotalperdate += i;
//		    		        	}
//		    		        	int squadavg = squadtotalperdate/playerfiles.size();
//		    		        	
//		    		        	// calculate the standard deviations
//		    		        	List<Double> deviations = new ArrayList<Double>();
//		    		        	for(Integer i : squadacuteloads.get(date)){
//		    		        		deviations.add(Math.pow((i-squadavg), 2));
//		    		        	}
//		    		        	
//		    		        	// calculate the variance
//		    		        	double variance = 0;
//		    		        	for(Double d : deviations){
//		    		        		variance+=d;
//		    		        	}
//		    		        	variance = variance/deviations.size();
//		    		        	
//		    		        	int standardDeviation = (int) Math.sqrt(variance);
//		    		        	//System.out.println("sd = " + standardDeviation);
//		    		        	
//		    		        	
//		    		        	// append the squad average to this data line for this player
//		    		        	s = s + ","+squadavg+","+standardDeviation;
//		    		        	
//		    		        	// add the new line to the new list
//		    		        	newdata.add(s);
//		        			}
//		        			// replace the old list with the new list
//		        			entry.setValue(newdata);
//		        		    
//		        		}
				
				fileReader.close();
			} catch (IOException e) {
				// TODO return an error to the user
				e.printStackTrace();
			} 
		}
		
	}
