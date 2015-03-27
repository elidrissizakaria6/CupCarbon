package g;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket s = new Socket("localhost", 2004);
		//BufferedReader is = new s.getInputStream();
		PrintStream ps = new PrintStream(s.getOutputStream());
		ps.println("node add s2 \"/Users/bounceur/Desktop/Gautrais/TestG/TestG/test/an2.scr\"");
		Thread.sleep(2000);
		ps.println("step 3");
		Thread.sleep(2000);
		ps.println("exit");
		ps.close();
		s.close();
		
	}

}
