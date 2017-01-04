package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVTemplateGenerator {


	static int fileVersion = 1;
	static String fileExtension = ".csv";
	// column order is important!
	String header = "playername,SESS_LOAD,SESS_START,SESS_END,"
			+ "PRE_LOAD,PRE_START,PRE_END,"
			+ "PRACT_LOAD,PRACT_START,PRACT_END,"
			+ "POST_LOAD,POST_START,POST_END,"
			+ "OTHER_LOAD,OTHER_START,OTHER_END,"
			+ "ACCEL_TOTAL,DECEL_TOTAL,"
			+ "COD_L_LO,COD_L_MED,COD_L_HI,COD_R_LO,COD_R_MED,COD_R_HI,"
			+ "JUMP_LO,JUMP_MED,JUMP_HI,"
			+ "HR_EXERT,"
			+ "AWAY_TEAM,HOME_TEAM,WIN_LOSS,SCORE_DIFF,"
			+ "GAME_LOAD,GAME_MINS,POINTS,REBOUNDS,ASSISTS,STEALS,BLOCKS,FOULS,"
			+ "ACUTE,CHRONIC,SQUAD_AVG,SQUAD_DEV";
	
	String defaultFirstLine = "blank,0,1474981900,1474998200,"
			+ "0,1474981900,1474998200,"
			+ "0,1474981900,1474998200,"
			+ "0,1474981900,1474998200,"
			+ "0,1474981900,1474998200,"
			+ "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";

	
	/**
	 * Creates a new data file template for a player
	 * @param filepath
	 * @param filename
	 * @return
	 */
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

			out.println(header);
			out.println(defaultFirstLine);


		} catch (Exception e) {
			// TODO return an error message to the user
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

		return filename + fileVersion + fileExtension;
	}
}
