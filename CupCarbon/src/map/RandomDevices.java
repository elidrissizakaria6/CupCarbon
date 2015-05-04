package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import cupcarbon.CupCarbonMap;
import device.DeviceList;
import device.SensorNode;

public class RandomDevices {

	public static void addRandomSensors(int n) {
	
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		Point2D p3=Layer.mapViewer.convertGeoPositionToPoint(CupCarbonMap.map.getCenterPosition());
		Point p1=new Point((int)p3.getX()-663,(int)p3.getY()+293);
		Point p2=new Point((int)p3.getX()+663,(int)p3.getY()-293);
		
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();
		System.out.println(tab1[0]);
		System.out.println(tab2[0]);

		ArrayList<Integer> tabX=new ArrayList<Integer>();
		ArrayList<Integer> tabY=new ArrayList<Integer>();
		for(int i=tab1[0];i<tab2[0];i+=40){
			tabX.add(i);
		}
		for(int i=tab2[1];i<tab1[1];i+=40){
			tabY.add(i);
		}
		int r1 ;
		int r2 ;
		int i=0;
		int j=0;
		while(i < n) {
			r1 = tabX.get((int)(Math.random()*tabX.size()));
			r2 = tabY.get((int)(Math.random()*tabY.size()));	
			System.out.println(tabX.size() + " dfd "+tabY.size());
			GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point(r1,r2));
			SensorNode s=new SensorNode(gp1.getLatitude(), gp1.getLongitude(),0, 100, 20);
			if(s.isInDeviceList()==false){
			DeviceList.add(s);
			Layer.getMapViewer().repaint();
			i++;
			}
		}
	}

	
}
