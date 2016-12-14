
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

public class GameDataLoader {

	// header contains the original column headers in the gps data file being uploaded
	// it is used to get the index of the column that the data is read from
	String header;
	
	List<Double> playerloads = new ArrayList<Double>();
	Double sessionAverage = 0.0;
	
	// put each players session details into a map
	Map<String, String[]> playersessions = new HashMap<String, String[]>();
	
	// playerfilesbyname is a map of player names pointing to a list of new data records for each of the players
	Map<String, ArrayList<String>> playerfilesbyname = new HashMap<String, ArrayList<String>>();
	

	public Map<String, ArrayList<String>> getPlayerfilesbyname() {
		return playerfilesbyname;
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

			// get the index of each of the required columns of data
			int nameindex = headerstrings.indexOf("PLAYER");
			int gamedate = headerstrings.indexOf("GAME_DATE");
			int periodname = headerstrings.indexOf("PERIOD");
			int minutes = headerstrings.indexOf("MINUTES");
			int playerload = headerstrings.indexOf("PHYSIO_LOAD");
			int points = headerstrings.indexOf("POINTS");
			int rebounds = headerstrings.indexOf("REBOUNDS");
			int assists = headerstrings.indexOf("ASSISTS");
			int steals = headerstrings.indexOf("STEALS");
			int blocks = headerstrings.indexOf("BLOCKS");
			int fouls = headerstrings.indexOf("FOULS");
			

			// Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				// replace all NA values with 0
				line = line.replaceAll("NA", "0");
				newLine = "";

				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the players name from the relevant column
				String playername = tokens[nameindex];
				String gamestring = tokens[gamedate];
				String minsplayed = tokens[minutes];
				
				
				String playerpoints = tokens[points];
				String playerrebounds = tokens[rebounds];
				String playerassists = tokens[assists];
				String playersteals = tokens[steals];
				String playerblocks = tokens[blocks];
				String playerfouls = tokens[fouls];
				
				
				
				
				//String load = tokens[playerload];
				String period = tokens[periodname];


				// discarding floating point numbers from data
				//int decpoint = tokens[playerload].indexOf('.');
				double d = Double.parseDouble(tokens[playerload]);
				String load = String.valueOf((long)d);
				//System.out.println(load);
				
				//System.out.println("gamestring is "+gamestring);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gametime = null;
				try {
					gametime = sdf.parse(gamestring);
					//gametime = sdf.parse("31-AUG-16");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("before "+gametime);
				
				Long hour = (long) (3600*1000);
				Date actual = new Date(gametime.getTime() + (19*hour));
				//System.out.println("after "+actual);
				
				// generate a unix timestamp from this date
				Long starttimesecs = actual.getTime()/1000;
				Long endtimesecs = starttimesecs + (60 * 180);
				//System.out.println(starttimesecs + " - "+endtimesecs);
				//System.out.println();
				

				// if the map already contains this player name as a key
				if (playerfilesbyname.containsKey(playername)) {
					
					if(period.equalsIgnoreCase("0")){
						
						playerloads.add(Double.parseDouble(tokens[playerload]));
						
						String entry = playername + ",0," + starttimesecs + ","+ endtimesecs;
						for(int i = 0; i<25;++i){
							entry +=",0";
						}
						
							entry += load + "," + minsplayed + ","  + playerpoints + "," 
							+ playerrebounds + "," + playerassists + "," + playersteals + "," 
							+ playerblocks + "," + playerfouls + ","
							+ "0,0,0,0";
										
						
						
						playerfilesbyname.get(playername).add(entry);
						
					}



				} else {
					// new player encountered(not already in map)
					// instantiate a new list of strings for this player
					
					
					if(period.equalsIgnoreCase("0")){
						
						playerloads.add(Double.parseDouble(tokens[playerload]));
						
						
						String entry = playername + ",0," + starttimesecs + ","+ endtimesecs;
						for(int i = 0; i<25;++i){
							entry +=",0";
						}
						
							entry += load + "," + minsplayed + ","  + playerpoints + "," 
							+ playerrebounds + "," + playerassists + "," + playersteals + "," 
							+ playerblocks + "," + playerfouls + ","
							+ "0,0,0,0";
						
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(entry);
						
						playerfilesbyname.put(playername, temp);//.add(entry);
						
						
					}

					
				}

			} // end while loop reading files
			

			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		
		
			
	} //end loadCSVFile()
	
	
}

