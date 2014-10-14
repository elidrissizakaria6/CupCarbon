package tracking.ws;

import geometry.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TrackerWS {
	
private static BufferedReader br = null;
	
	public static void clean() {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbon_ws/clean.php");			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public static void create(String id, double la, double lo) {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbon_ws/add_tracker.php?id="+id+"&la="+la+"&lo="+lo);			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public static String getInfo(String id) {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbon_ws/get_tracker.php?id="+id);			
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			String s = br.readLine();
			br.close();
			return s;
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return null;
	}
		
	public static void displayCoords(String id) {
		String str = getInfo(id);
		String [] strT = str.split("\"");
		System.out.println(strT[3]);
		System.out.println(strT[7]);
		System.out.println(strT[11]);
	}
	
	public static Point getCoords(String id) {
		String str = getInfo(id);
		String [] strT = str.split("\"");
		if(strT.length >=12){
			return new Point(Double.valueOf(strT[7]), Double.valueOf(strT[11]));
		}
		else return null;
	}
	
	public static void setCoords(String id, double la, double lo) {
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbon_ws/update_tracker.php?id="+id+"&la="+la+"&lo="+lo);			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}	
	}
}