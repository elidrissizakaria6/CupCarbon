/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package solver;

import map.Layer;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Reinhardt Euler
 * @author Marc Sevaux
 * @author Bernard Pottier 
 * @author Ali Benzerbadj
 * @author Farid Lalem
 * @author Massinissa Saoudi
 * @version 1.0
 */
public class NetworkEnvelopeC3 extends Thread {
	
	//private LinkedList<Integer> border = null ;
	
	@Override
	public void run() {
		
		DeviceList.initAll();
		DeviceList.addEnvelope();				
		
		int p0=0;
		double px0 = 0;
		double py0 = 0;
		int p1;
		double px1 = 0;
		double py1 = 0;
		int p2;
		double px2 = 0;
		double py2 = 0;
		int p3;
		double px3 = 0;
		double py3 = 0;
		//int p4;
		//double px4 = 0;
		//double py4 = 0;
		
		double min = 10000000;
		
		int imin = 0;
		
		for (int i = 0; i < DeviceList.getNodes().size(); i++) {
			DeviceList.getNodes().get(i).setValue(0);
			DeviceList.getNodes().get(i).setVisited(false);
			DeviceList.getNodes().get(i).setMarked(false);
			if(min>DeviceList.getNodes().get(i).getY()) {
				min = DeviceList.getNodes().get(i).getY();
				imin = i;
			}
		}
		DeviceList.getNodes().get(imin).setMarked(true);
		DeviceList.getNodes().get(imin).setVisited(true);
		DeviceList.addToLastEnvelope(imin);
		Layer.getMapViewer().repaint();		
		
		try {
			sleep(100);
		} catch (InterruptedException e) {}		
		//DeviceList.getNodes().get(imin).setAlgoSelect(false);
		
		int cur = imin;		
		Device n1 = DeviceList.getNodes().get(cur);
		Device n2 ;
		double x1 = n1.getY();
		double y1 = n1.getX();
		double x2 = 0;
		double y2 = 0;
		double angle ;
		
		System.out.println(cur);
		p1 = cur ;
		px1 = x1 ;
		py1 = y1 ;

			min = 10000000;
			imin = -1;
			for(int j=0; j<DeviceList.getNodes().size(); j++) {
				n2 = DeviceList.getNodes().get(j);
				if((j!=cur) && (!n2.isVisited())) {
					x2 = n2.getY();
					y2 = n2.getX();
					angle = getAngle(x1,y1,x2,y2);					
					System.out.println("    "+j+" : "+Math.toDegrees(angle));
					if(angle<min) {
						min = angle;
						imin = j;
					}
				}
			}
			cur = imin;
			System.out.println("  > "+cur);
			DeviceList.getNodes().get(cur).setMarked(true);
			DeviceList.getNodes().get(cur).setVisited(true);
			DeviceList.addToLastEnvelope(imin);
			Layer.getMapViewer().repaint();
			x2 = DeviceList.getNodes().get(cur).getY();
			y2 = DeviceList.getNodes().get(cur).getX();
			p2 = cur ;
			px2 = x2;
			py2 = y2;
			n1 = DeviceList.getNodes().get(cur);
			
			try {
				sleep(100);
			} catch (InterruptedException e) {}		
			//DeviceList.getNodes().get(imin).setAlgoSelect(false);
			
		for(int i=0; i<DeviceList.getNodes().size()-2; i++) {
			System.out.println(cur);
			min = 10000000;
			imin = -1;
			for(int j=0; j<DeviceList.getNodes().size(); j++) {
				n2 = DeviceList.getNodes().get(j);
				if((j!=cur) && (!n2.isVisited())) {
					x2 = n2.getY();
					y2 = n2.getX();
					angle = getAngle(x1,y1,x2,y2);					
					System.out.println("    "+j+" : "+Math.toDegrees(angle));
					if(angle<min) {
						min = angle;
						imin = j;
					}
				}
			}
			cur = imin;
			System.out.println("  > "+cur);
			DeviceList.getNodes().get(cur).setMarked(true);
			DeviceList.getNodes().get(cur).setVisited(true);
			DeviceList.addToLastEnvelope(imin);
			Layer.getMapViewer().repaint();
			x2 = DeviceList.getNodes().get(cur).getY();
			y2 = DeviceList.getNodes().get(cur).getX();
			p3 = cur ;
			px3 = x2 ;
			py3 = y2 ;
			
			System.out.println("---> "+p1+" "+p2+" "+p3);
			System.out.println(px1+" "+py1);
			System.out.println(px2+" "+py2);
			System.out.println(px3+" "+py3);
			
			boolean r = right(px1-px2,py1-py2,px3-px2,py3-py2);
			System.out.println(r?"DROITE":"GAUCHE");
			
			if(!r) {
				DeviceList.getNodes().get(p2).setMarked(false);				
				Layer.getMapViewer().repaint();
				int tmp = p2;
				double tmpx = px2;
				double tmpy = py2;
				p2 = p1;
				p1 = p0;	
				p0 = tmp;
				px2 = px1 ;
				py2 = py1 ;
				px1 = px0 ;
				py1 = py0 ;
				px0 = tmpx;
				py0=tmpy;
				
				System.out.println("-1-> "+p1+" "+p2+" "+p3);
			}
			else {
				p0 = p1;
				p1 = p2 ;
				p2 = p3 ;
				px0 = px1;
				py0 = py1;
				px1 = px2 ;
				py1 = py2 ;
				px2 = px3 ;
				py2 = py3 ;
				System.out.println("-2-> "+p1+" "+p2+" "+p3);
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {}	
		}
		
		int v ;
		for(int i=0; i<DeviceList.getNodes().size(); i++) {
			v = DeviceList.getLastEnvelope().get(i);					
			if(DeviceList.getNodes().get(v).isSelected())
				DeviceList.addToLastEnvelope(v);
		}
		Layer.getMapViewer().repaint();
		System.out.println("---------------------------");
		System.out.println(" F I N I S H");
		System.out.println("---------------------------");
		
	}

	public double getAngle(double x1, double y1, double x2, double y2) {
		x2 = x2 - x1 ;
		y2 = y2 - y1 ;		
		double a = Math.atan2(x2, y2);
		if (a<0) a = (2*Math.PI)+a;		
		return a;
	}
	
	public boolean right2(double x1, double y1, double x2, double y2) {
		double b = (x1*y2)-(x2*y1);
		return (b>0);
	}
	
	public boolean right(double x1, double y1, double x2, double y2) {
		double a = Math.atan2(x1, y1);
		if (a<0) a = (2*Math.PI)+a;		
		double b = Math.atan2(x2, y2);
		if (b<0) b = (2*Math.PI)+b;
		b = b - a;
		if (b<0) b = (2*Math.PI)+b; 
		return (Math.toDegrees(b)>180);
	}
	
	public double getAngle2(double x1, double y1, double x2, double y2) {
		double a = Math.atan2(x1, y1);
		if (a<0) a = (2*Math.PI)+a;		
		double b = Math.atan2(x2, y2);
		if (b<0) b = (2*Math.PI)+b;
		b = b - a;
		if (b<0) b = (2*Math.PI)+b; 
		return b;
	}
	
	public static boolean intersect(double p0_x, double p0_y, double p1_x, double p1_y, 
	    double p2_x, double p2_y, double p3_x, double p3_y) {
	    double s1_x, s1_y, s2_x, s2_y;
	    s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
	    s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;
	
	    double s, t;
	    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);
	
	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
	    {
	        // Collision detected
	        return true;
	    }
	
	    return false; // No collision
	}
	
}