
package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CSVRedoxGenerator {


	static int fileVersion = 1;
	static String fileExtension = ".csv";
	// column order is important!
	String header = "playername,"
			+"NOTES,"
			+"TrainedToday,AteToday,ExerciseGymYesterday,ExerciseTrainingYesterday,ExerciseGameYesterday,"
			+"ExerciseNoneYesterday,ExerciseOtherYesterday,EnergyLevel,MuscleSoreness,SymptomFeverToday,"
			+"SymptomFeverPreviously,SymptomSoreThroatToday,SymptomSoreThroatPreviously,SymptomHeadacheToday,"
			+"SymptomHeadachePreviously,SymptomJointorMuscleAcheToday,SymptomJointorMuscleAchePreviously,"
			+"SymptomDiarrheaToday,SymptomDiarrheaPreviously,SymptomOther,"
			+"TEST_TIME,FORD,FORD_INC,FORD_MEAN,FORD_ADJ,FORT,FORT_INC,FORT_MEAN,FORT_ADJ";
	

	
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

