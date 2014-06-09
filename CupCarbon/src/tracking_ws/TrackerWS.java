package tracking_ws;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class TrackerWS {
	
private static BufferedReader br = null;
	
	public static void clean() {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbonws/clean.php");			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public static void create(String id, double la, double lo) {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbonws/add_tracker.php?id="+id+"&la="+la+"&lo="+lo);			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}
	}
	
	public static String getInfo(String id) {	
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbonws/get_tracker.php?id="+id);			
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
		Point p = new Point(0,0) ;
		String str = getInfo(id);
		String [] strT = str.split("\"");
		p.setLocation(Double.valueOf(strT[7]), Double.valueOf(strT[11]));
		return p;
	}
	
	public static void setCoords(String id, double la, double lo) {
		try {
			URL url = new URL("http://pagesperso.univ-brest.fr/~bounceur/cupcarbonws/update_tracker.php?id="+id+"&la="+la+"&lo="+lo);			
			url.openStream();
		} catch (IOException e) {
			e.printStackTrace();			
		}	
	}
}
