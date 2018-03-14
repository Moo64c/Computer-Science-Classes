import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CollegeAdmission{

	public static void main(String[] args) {
		// Gets a file name args[0] with names and grade
		try {
			checkCandidates(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkCandidates(String filename) throws Exception {
		BufferedReader inputFile = null;
		try {
			inputFile = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			throw e1;
			// Can't keep going.
		}
		
		String currentLine;
		
		
		try {
			while ((currentLine = inputFile.readLine())!= null){
				if (!currentLine.matches("(([a-z]|[A-Z])+.?\\s*)+;[0-9]{1,3}")) {
					throw new BadInputFormatException("line that doesn't follow the requested format " + currentLine);
				}
				
				String[] candidateDetails = currentLine.split(";");
				System.out.println(candidateDetails[0] 
						+ ":" + candidateDetails[1] +"\t=> "
						+ (Integer.parseInt(candidateDetails[1])>= 400 ?
								"ACCEPTED":"DENIED"));
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
