package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CSVLoader3 {

	// header contains the original column headers in the gps data file being uploaded
	// it is used to get the index of the column that the data is read from
	String header;
	
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

			// must skip down to the 10th line before reading column headers
			int numlinestoskip = 9;

			for (int i = numlinestoskip; i > 0; i--) {

				fileReader.readLine();
			}

			// this next line should contain the column headings
			header = fileReader.readLine();

			// split the line into an array
			String[] headertokens = header.split(",");

			List<String> headerstrings = Arrays.asList(headertokens);

			// get the index of each of the required columns of data
			int nameindex = headerstrings.indexOf("Player Name");
			int periodname = headerstrings.indexOf("Period Name");
			int startindex = headerstrings.indexOf("Unix Start Time");
			int endindex = headerstrings.indexOf("Unix End Time");
			int playerloadindex = headerstrings.indexOf("Total Player Load");

			// getting the accel/decel values
			int playeraccelindex1 = headerstrings.indexOf("IMA Accel Low");
			int playeraccelindex2 = headerstrings.indexOf("IMA Accel Medium");
			int playeraccelindex3 = headerstrings.indexOf("IMA Accel High");
			int playerdecelindex1 = headerstrings.indexOf("IMA Decel Low");
			int playerdecelindex2 = headerstrings.indexOf("IMA Decel Medium");
			int playerdecelindex3 = headerstrings.indexOf("IMA Decel High");
			
			
			int playercodleftlow = headerstrings.indexOf("IMA CoD Left Low");
			int playercodleftmed = headerstrings.indexOf("IMA CoD Left Medium"); 
			int playercodlefthigh = headerstrings.indexOf("IMA CoD Left High"); 
			int playercodrightlow = headerstrings.indexOf("IMA CoD Right Low"); 
			int playercodrightmed = headerstrings.indexOf("IMA CoD Right Medium"); 
			int playercodrighthigh = headerstrings.indexOf("IMA CoD Right High"); 
			

			// System.out.println("id index found"+idindex);
			System.out.println("playername index found" + nameindex);
			System.out.println("period name found" + periodname);
			System.out.println("startindex found" + startindex);
			System.out.println("endindex  found" + endindex);
			System.out.println("load index found" + playerloadindex);

			// Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				// replace all NA values with 0
				line = line.replaceAll("NA", "0");
				newLine = "";

				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the players name from the relevant column
				String playername = tokens[nameindex];
				String starttime = tokens[startindex];
				String endtime = tokens[endindex];
				String period = tokens[periodname];

				Integer acc = Integer.parseInt(tokens[playeraccelindex1])
						+ Integer.parseInt(tokens[playeraccelindex2])
						+ Integer.parseInt(tokens[playeraccelindex3]);

				Integer dec = Integer.parseInt(tokens[playerdecelindex1])
						+ Integer.parseInt(tokens[playerdecelindex2])
						+ Integer.parseInt(tokens[playerdecelindex3]);



				String accel = acc.toString();
				String decel = dec.toString();
				
				String codleftlow = tokens[playercodleftlow];
				String codleftmed = tokens[playercodleftmed]; 
				String codlefthigh = tokens[playercodlefthigh]; 
				String codrightlow = tokens[playercodrightlow]; 
				String codrightmed = tokens[playercodrightmed];
				String codrighthigh = tokens[playercodrighthigh];



				// discarding floating point numbers from data
				int decpoint = tokens[playerloadindex].indexOf('.');
				String load = tokens[playerloadindex].substring(0, decpoint);

				// if the map already contains this player name as a key
				if (playersessions.containsKey(playername)) {
					
					String[] datapoints = playersessions.get(playername);
					// test which column to add the data t0
					// note adding default zeros for acute load and chronic load
					if(period.equalsIgnoreCase("Session")){
						
						datapoints[1] = load + ",";
						datapoints[2] = starttime + ",";
						datapoints[3] = endtime + ",";
						datapoints[16] = accel + ",";
						datapoints[17] = decel + ",";
						
						datapoints[18] = codleftlow + ",";
						datapoints[19] = codleftmed + ",";
						datapoints[20] = codlefthigh + ",";
						datapoints[21] = codrightlow + ",";
						datapoints[22] = codrightmed + ",";
						datapoints[23] = codrighthigh + ",";
						
					
					} else if(period.equalsIgnoreCase("Pre Practice")){
						
						datapoints[4] = load + ",";
						datapoints[5] = starttime + ",";
						datapoints[6] = endtime + ",";
						
					} else if(period.equalsIgnoreCase("Practice")){
						
						datapoints[7] = load + ",";
						datapoints[8] = starttime + ",";
						datapoints[9] = endtime + ",";
						
					} else if(period.equalsIgnoreCase("Post Practice")){
						
						datapoints[10] = load + ",";
						datapoints[11] = starttime + ",";
						datapoints[12] = endtime + ",";
						
					} else {
						
						datapoints[13] = load + ",";
						datapoints[14] = starttime + ",";
						datapoints[15] = endtime + ",";
						
					}





					

				} else {
					// new player encountered(not already in map)
					// instantiate a new list of strings for this player
					String[] datapoints = new String[28];
					// populate the arraylist with zeros
					for(int i = 0; i<datapoints.length; i++){
						datapoints[i] = "0,";
					}
					
					datapoints[0] = playername + ",";
					
					if(period.equalsIgnoreCase("Session")){
						
						datapoints[1] = load + ",";
						datapoints[2] = starttime + ",";
						datapoints[3] = endtime + ",";
						datapoints[16] = accel + ",";
						datapoints[17] = decel + ",";
						
						datapoints[18] = codleftlow + ",";
						datapoints[19] = codleftmed + ",";
						datapoints[20] = codlefthigh + ",";
						datapoints[21] = codrightlow + ",";
						datapoints[22] = codrightmed + ",";
						datapoints[23] = codrighthigh + ",";
					
					} else if(period.equalsIgnoreCase("Pre Practice")){
						
						datapoints[4] = load + ",";
						datapoints[5] = starttime + ",";
						datapoints[6] = endtime + ",";
						
					} else if(period.equalsIgnoreCase("Practice")){
						
						datapoints[7] = load + ",";
						datapoints[8] = starttime + ",";
						datapoints[9] = endtime + ",";
						
					} else if(period.equalsIgnoreCase("Post Practice")){
						
						datapoints[10] = load + ",";
						datapoints[11] = starttime + ",";
						datapoints[12] = endtime + ",";
						
					} else {
						
						datapoints[13] = load + ",";
						datapoints[14] = starttime + ",";
						datapoints[15] = endtime + ",";
						
					}



					// add this new player to the map
					playersessions.put(playername, datapoints);
					
				}

			} // end while loop reading files


			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		for (Map.Entry<String, String[]> entry : playersessions.entrySet()) {
		    String key = entry.getKey();
		    String aline = "";
		    for(String s : entry.getValue()){
		    	aline += s;
		    }
		    ArrayList<String> templist = new ArrayList<String>();
		    templist.add(aline);
		    playerfilesbyname.put(key, templist);
		}
		
		
			
	} //end loadCSVFile()
	
	
}
