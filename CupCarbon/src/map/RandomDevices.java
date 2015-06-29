package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import cupcarbon.CupCarbonMap;
import device.Device;
import device.DeviceList;
import device.Marker;
import device.MarkerList;
import device.SensorNode;

public class RandomDevices {

	public static boolean WithMarkers;
	public static long racine1;
	public static long racine2;
	public static Random random2;
	public static Random random1;

	public static void addRandomSensorsWithMarkers(int n) {
		WithMarkers=true;
		if(racine1==0&&racine2==00){
			racine1=Double.doubleToLongBits(Math.random());
			racine2=Double.doubleToLongBits(Math.random());
			random1=new Random(racine1);
			random2=new Random(racine2);
			}
		
		try{
			Marker marker1 = MarkerList.getMarkers().get(0);
			Marker marker2 = MarkerList.getMarkers().get(1);
			}catch(IndexOutOfBoundsException IOBE){WithMarkers=false;}
		if(!WithMarkers){
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Veuillez Poser 2 Markers Sur La Carte Puis Résseyez");
		}
		else{
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
			// suppression des marqueurs apres la génération des noeudds
			MarkerList.getMarkers().clear();
			for (int i = 0; i < n; i++) {
				r1=random1.nextDouble();
				r2=random2.nextDouble();
				x = ((m2x-m1x)*r1)+m1x;
				y = ((m2y-m1y)*r2)+m1y;
				DeviceList.add(new SensorNode(x, y,0, 100, 20));
				Layer.getMapViewer().repaint();
			}
		}
	}
	public static void addRandomSensorsWithoutMarkers(int n) {
		if(racine1==0&&racine2==00){
			racine1=Double.doubleToLongBits(Math.random());
			racine2=Double.doubleToLongBits(Math.random());
			random1=new Random(racine1);
			random2=new Random(racine2);
		}
//		else{
//			random1=new Random(racine1);
//			random2=new Random(racine2);
//		}
		Point2D p3=Layer.mapViewer.convertGeoPositionToPoint(CupCarbonMap.map.getCenterPosition());
		Point p1=new Point((int)(p3.getX()-(p3.getX()*95/100)),(int)(p3.getY()-(p3.getY()*95/100)));
		Point p2=new Point((int)(p3.getX()+(p3.getX()*95/100)),(int)(p3.getY()+(p3.getY()*95/100)));		
		double m1x=(int) p1.getX();
		double m1y=(int) p1.getY();
		double m2x=(int) p2.getX();
		double m2y=(int) p2.getY();

		double r1 ;
		double r2 ;
		int x ;
		int y ;
		for (int i = 0; i < n; i++) {
			r1=random1.nextDouble();
			r2=random2.nextDouble();
			x = (int) (((m2x-m1x)*r1)+m1x);
			y = (int) (((m2y-m1y)*r2)+m1y);
			GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point(x,y));
			SensorNode s=new SensorNode(gp1.getLatitude(), gp1.getLongitude(),0, 100, 20);
				DeviceList.add(s);
				Layer.getMapViewer().repaint();
		}
	}
	
	public static void addRandomSensorsWithoutMarkersInGrid(int n,int x,int y) {
		if(racine1==0&&racine2==00){
			racine1=Double.doubleToLongBits(Math.random());
			racine2=Double.doubleToLongBits(Math.random());
			random1=new Random(racine1);
			random2=new Random(racine2);
		}
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		Point2D p3=Layer.mapViewer.convertGeoPositionToPoint(CupCarbonMap.map.getCenterPosition());
		Point p1=new Point((int)(p3.getX()-(p3.getX()*95/100)),(int)(p3.getY()-(p3.getY()*95/100)));
		Point p2=new Point((int)(p3.getX()+(p3.getX()*95/100)),(int)(p3.getY()+(p3.getY()*95/100)));		
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();
		ArrayList<Integer> tabX=new ArrayList<Integer>();
		ArrayList<Integer> tabY=new ArrayList<Integer>();
		for(int i=tab1[0];i<tab2[0];i+=((tab2[0]-tab1[0])/x+1)){
			tabX.add(i);
		}
		for(int i=tab1[1];i<tab2[1];i+=((tab2[1]-tab1[1])/y+1)){
			tabY.add(i);
		}
		int r1 = 0 ;
		int r2 = 0 ;
		int i=0;
		while(i < n){ 
			r1=tabX.get(Math.abs(random1.nextInt())%tabX.size());
			r2=tabY.get(Math.abs(random2.nextInt())%tabY.size());
			GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point(r1,r2));
			
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
	
	public static void addRandomSensorsWithMarkersInGrid(int n,int x, int y) {
		WithMarkers=true;
		if(racine1==0&&racine2==00){
			racine1=Double.doubleToLongBits(Math.random());
			racine2=Double.doubleToLongBits(Math.random());
			random1=new Random(racine1);
			random2=new Random(racine2);
			}
		
		try{
			Marker marker1 = MarkerList.getMarkers().get(0);
			Marker marker2 = MarkerList.getMarkers().get(1);
			}catch(IndexOutOfBoundsException IOBE){WithMarkers=false;}
		if(!WithMarkers){
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Veuillez Poser 2 Markers Sur La Carte Puis résseyez");
		}
		else{
			
		ArrayList<Integer> tabX=new ArrayList<Integer>();
		ArrayList<Integer> tabY=new ArrayList<Integer>();
		Marker marker1 = MarkerList.getMarkers().get(0);
		Marker marker2 = MarkerList.getMarkers().get(1);
		int tab1[]=new int[2];
		int tab2[]=new int[2];
		System.out.println(marker1.getY()+" "+marker1.getX());
		Point2D p1=Layer.mapViewer.convertGeoPositionToPoint(new GeoPosition(marker1.getX(), marker1.getY()));
		Point2D p2=Layer.mapViewer.convertGeoPositionToPoint(new GeoPosition(marker2.getX(), marker2.getY()));
		tab1[0]=(int) p1.getX();
		tab1[1]=(int) p1.getY();
		tab2[0]=(int) p2.getX();
		tab2[1]=(int) p2.getY();

		for(int i=Math.min(tab1[0], tab2[0]);i<Math.max(tab2[0],tab1[0]);i+=Math.abs(((tab2[0]-tab1[0])/x+1))){
			tabX.add(i);
		}
		for(int i=Math.min(tab1[1], tab2[1]);i<Math.max(tab2[1],tab1[1]);i+=Math.abs(((tab2[1]-tab1[1])/y+1))){
			tabY.add(i);
		}
		int i=0;
		int r1 ;
		int r2 ;
		// suppression des marqueurs apres la génération des noeudds
		MarkerList.getMarkers().clear();
		while(i < n) {	
			r1=tabX.get(Math.abs(random1.nextInt())%tabX.size());
			r2=tabY.get(Math.abs(random2.nextInt())%tabY.size());
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
	
}
