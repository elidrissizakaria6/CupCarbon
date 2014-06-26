package tracking;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import map.Layer;
import device.DeviceList;
import device.Sensor;

public class TrackerReader extends Thread {
	
	private double la ;
	private double lo ;
	private Sensor sensor ;
	private String id ;
	private URL url = null ;		
	private BufferedReader br = null ;
	private String [] strT ;
	
	public TrackerReader() {
		try {
			sensor = (Sensor) DeviceList.getNodes().get(0);
			id = sensor.getNodeIdName();
			la = sensor.getX();
			lo = sensor.getY();
			FileInputStream fis = new FileInputStream("tracking_sw.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			Service.url = br.readLine() ;
			br.close();
			fis.close();
			URL url = null;
			url = new URL(Service.url+"add_tracker.php?id="+id+"&la="+la+"&lo="+lo);
			url.openStream();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {		
		la = sensor.getX();
		lo = sensor.getY();
		while(true) {
			try {
				url = new URL(Service.url+"get_tracker.php?id="+id+"&la="+la+"&lo="+lo);				
				br = new BufferedReader(new InputStreamReader(url.openStream()));
				strT = br.readLine().split("\"");
				sensor.setX(Double.valueOf(strT[7]));
				sensor.setY(Double.valueOf(strT[11]));
				Layer.getMapViewer().repaint();
				br.close();
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
}
