package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import cupcarbon.CupCarbonMap;
import device.DeviceList;
import device.Marker;
import device.MarkerList;
import device.SensorNode;

public class RandomDevices {

	public static boolean WithMarkers;
	public static void addRandomSensors(int n){
		WithMarkers=true;
		try{
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		}catch(IndexOutOfBoundsException IOBE){WithMarkers=false;}
		
		if(WithMarkers){
			addRandomSensorsWithMarkers(n);
		}
		else{
			addRandomSensorsWithoutMarkers(n);
		}
	}
	public static void addRandomSensorsWithoutMarkers(int n) {
	
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		Point2D p3=Layer.mapViewer.convertGeoPositionToPoint(CupCarbonMap.map.getCenterPosition());
		Point p1=new Point((int)p3.getX()-663,(int)p3.getY()+293);
		Point p2=new Point((int)p3.getX()+663,(int)p3.getY()-293);
		
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();

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
		while(i < n) {
			r1 = tabX.get((int)(Math.random()*tabX.size()));
			r2 = tabY.get((int)(Math.random()*tabY.size()));	
			GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point(r1,r2));
			SensorNode s=new SensorNode(gp1.getLatitude(), gp1.getLongitude(),0, 100, 20);
			if(s.isInDeviceList()==false){
			DeviceList.add(s);
			Layer.getMapViewer().repaint();
			i++;
			}
		}
	}
	
	public static void addRandomSensorsWithMarkers(int n) {
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		double m1x = marker1.getX();
		double m1y = marker1.getY();
		double m2x = marker2.getX();
		double m2y = marker2.getY();

		double r1 ;
		double r2 ;
		double x ;
		double y ;
		
		for (int i = 0; i < n; i++) {
			r1 = Math.random();
			r2 = Math.random();
			x = ((m2x-m1x)*r1)+m1x;
			y = ((m2y-m1y)*r2)+m1y;				
			DeviceList.add(new SensorNode(x, y,0, 100, 20));
			Layer.getMapViewer().repaint();
		}
	}

	
}
