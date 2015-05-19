package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import utilities.MapCalc;
import cupcarbon.CupCarbonMap;
import device.Device;
import device.DeviceList;
import device.Marker;
import device.MarkerList;
import device.SensorNode;

public class RandomDevices {

	public static boolean WithMarkers;
	public static void addRandomSensors(int n,int x,int y){
		
		WithMarkers=true;
		try{
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		}catch(IndexOutOfBoundsException IOBE){WithMarkers=false;}
		
		if(WithMarkers){
			addRandomSensorsWithMarkersInGrid(n,x,y);
		}
		else{
			addRandomSensorsWithoutMarkers(n,x,y);
		}
	}
	public static void addRandomSensorsWithoutMarkers(int n,int x,int y) {
	
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		Point2D p3=Layer.mapViewer.convertGeoPositionToPoint(CupCarbonMap.map.getCenterPosition());
		Point p1=new Point((int)(p3.getX()-(p3.getX()*95/100)),(int)(p3.getY()-(p3.getY()*95/100)));
		Point p2=new Point((int)(p3.getX()+(p3.getX()*95/100)),(int)(p3.getY()+(p3.getY()*95/100)));
//		Point p1=new Point(0,0);
//		Point p2=new Point((int)(p3.getX()+(p3.getX())),(int)(p3.getY()+(p3.getY())));
		System.out.println(p3.getX()+" "+p3.getY());
		
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();
		System.out.println(tab1[0]+" "+tab1[1]+" "+tab2[0]+" "+tab2[1]);
		ArrayList<Integer> tabX=new ArrayList<Integer>();
		ArrayList<Integer> tabY=new ArrayList<Integer>();
		for(int i=tab1[0];i<tab2[0];i+=((tab2[0]-tab1[0])/x+1)){
			tabX.add(i);
		}
		for(int i=tab1[1];i<tab2[1];i+=((tab2[1]-tab1[1])/y+1)){
			tabY.add(i);
		}
		int r1 ;
		int r2 ;
		int i=0;
		GeneratorAlea GA1=new GeneratorAlea();
		GeneratorAlea GA2=new GeneratorAlea();
		while(i < n) {
//			r1 = tabX.get((int)(Math.random()*tabX.size()));
//			r2 = tabY.get((int)(Math.random()*tabY.size()));
			r1=tabX.get(GA1.Random(36, 22,12, tabX.size()));
			r2=tabY.get(GA2.Random(7, 40,31, tabY.size()));
			System.out.println(r1);
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
	
	public static void addRandomSensorsWithMarkersInGrid(int n,int x, int y) {
	
		ArrayList<Integer> tabX=new ArrayList<Integer>();
		ArrayList<Integer> tabY=new ArrayList<Integer>();
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		System.out.println(marker1.getY()+" "+marker1.getX());
		Point2D p1=Layer.mapViewer.convertGeoPositionToPoint(new GeoPosition(marker1.getX(), marker1.getY()));
		Point2D p2=Layer.mapViewer.convertGeoPositionToPoint(new GeoPosition(marker2.getX(), marker2.getY()));
//		tab1=MapCalc.geoToIntPixelMapXY(marker1.getY(), marker1.getX());
//		tab2=MapCalc.geoToIntPixelMapXY(marker1.getY(), marker1.getX());
		
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();
//		System.out.println(tab1[0]+" "+tab1[1]+" "+tab2[0]+" "+tab2[1]);

		for(int i=tab1[0];i<tab2[0];i+=((tab2[0]-tab1[0])/x+1)){
			tabX.add(i);
		}
		for(int i=tab2[1];i<tab1[1];i+=((tab1[1]-tab2[1])/y+1)){
			tabY.add(i);
		}
		int i=0;
		int r1 ;
		int r2 ;
		
		while(i < n) {
			r1 = tabX.get((int)(Math.random()*tabX.size()));
			r2 = tabY.get((int)(Math.random()*tabY.size()));	
			GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point(r1,r2));
			System.out.println(gp1.getLatitude()+" "+gp1.getLongitude());
			SensorNode s=new SensorNode(gp1.getLatitude(), gp1.getLongitude(),0, 100, 20);
			if(s.isInDeviceList()==false){
			DeviceList.add(s);
			Layer.getMapViewer().repaint();
			i++;
			}
			else{
				Device.number--;
			}
		}
	}

	
}
