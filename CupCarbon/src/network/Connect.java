package network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connect {

	public static void main(String [] args) throws Exception {
		Socket s = new Socket("172.25.49.90", 5000);
		System.out.println("Connexion effectu�e !");
		InputStream is = s.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String str = br.readLine();
		System.out.println("Premi�re ligne : ");
		System.out.println(str);
	}
	
}
