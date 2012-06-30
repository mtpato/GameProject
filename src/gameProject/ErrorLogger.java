package gameProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

	String fileName;
	String className;
	
	public ErrorLogger(String fileName, String className) {
		this.fileName = fileName;
		this.className = className;
	}
	
	
	
	public void log(String error) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		try {
			FileWriter fstream = new FileWriter(fileName, true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write("Time: " + dateFormat.format(date) + " | Class: " + className + " | " + error + "\n");
			
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void log(String error, int lineNumber) {
		log("line: " + lineNumber + " | " + error);
	}
}
