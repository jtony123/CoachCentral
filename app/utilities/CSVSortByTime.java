package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVSortByTime {
	
	
	
	// read the header and write out as is
	// get the index of the column with the header 'SESS_START'
	// read the file line by line
	//	for each line, get the session start time and put it as a map key with the line as a value
	//	sort the map according to key
	
	// iterate thru the map, and write out the new file line by line
	
	String header;
	
	Map<Long, String> data = new TreeMap<Long, String>();
	
	

	public String getHeader() {
		return header;
	}
	
	
	public void sortCSVFile(String fileName){
		BufferedReader fileReader = null;
		String line ="";
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
			
			// this first line should contain the attribute labels
			header = fileReader.readLine();
			// leave header alone
			// put it in the map with the key = 0 ( will always be the first entry)
			data.put((long) 0, header);
			
			String[] headertokens = header.split(",");
			List<String> headerstrings = Arrays.asList(headertokens);
			
			// get the index of the ID column
			int sess_start_index = headerstrings.indexOf("SESS_START");
			
			//Read the file line by line

			while ((line = fileReader.readLine()) != null) {
				
				// split the line into string tokens
				String[] tokens = line.split(",");
				// get the sess start from the relevant column
				
				Long key = Long.parseLong(tokens[sess_start_index]);
				data.put(key, line);
					

			} // end while loop
			
			
			fileReader.close();
		} catch (IOException e) {
			// TODO return an error to the user
			e.printStackTrace();
		}
		
		// now write out the file again
		
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
				  System.out.println(entry.getKey() + ": " + entry.getValue());
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
