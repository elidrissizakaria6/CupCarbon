package wisen_simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import project.Project;

public class SimLog {
	
	public static PrintStream logFile ;
	
	public static void init() {
		try {
			logFile = new PrintStream(new FileOutputStream(
					Project.getProjectResultsPath() + "/log" + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void add(String s) {
		logFile.println(s);
	}
	
	public static void close() {
		logFile.close();
	}

}
