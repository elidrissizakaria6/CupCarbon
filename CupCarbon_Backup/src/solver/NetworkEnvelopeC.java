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

import java.util.LinkedList;

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
public class NetworkEnvelopeC extends Thread {
	
	private LinkedList<Integer> envelope = null ;
	
	@Override
	public void run() {
		
		DeviceList.envelope = new LinkedList<Integer>();		
		envelope = DeviceList.envelope;
		
		double angle ;
		double min = 10000000;		
		int imin = 0;
		int n ;
		
		double px1 ;
		double py1 ;
		double px2 ;
		double py2 ;
		double px3 ;
		double py3 ;
		
		boolean r ;
		
		
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
		envelope.add(imin);		
		//System.out.println(envelope);
		Layer.getMapViewer().repaint();
		
		double x0,y0;		
		x0 = DeviceList.getNodes().get(imin).getY();
		y0 = DeviceList.getNodes().get(imin).getX();		
		
		delay();		
				
		Device node;
		min = 10000000;
		imin = -1;
		n = DeviceList.getNodes().size()-1;
		
		double x,y;
		
		for(int j=0; j<DeviceList.getNodes().size(); j++) {
			node = DeviceList.getNodes().get(j);			
			if((j!=envelope.getFirst()) && (!node.isVisited())) {
				x = node.getY();
				y = node.getX();
				angle = getAngle(x0,y0,x,y);					
				if(angle<min) {
					min = angle;
					imin = j;
				}
			}
		}
		DeviceList.getNodes().get(imin).setMarked(true);
		DeviceList.getNodes().get(imin).setVisited(true);
		envelope.add(imin);
		//System.out.println(envelope);
		Layer.getMapViewer().repaint();
		
		delay();		
			
		for(int i=0; i<DeviceList.getNodes().size()-2; i++) {
			min = 10000000;
			imin = -1;
			for(int j=0; j<DeviceList.getNodes().size(); j++) {
				node = DeviceList.getNodes().get(j);
				if((j!=envelope.getLast()) && (!node.isVisited())) {
					x = node.getY();
					y = node.getX();
					angle = getAngle(x0,y0,x,y);					
					if(angle<min) {
						min = angle;
						imin = j;
					}
				}
			}
			DeviceList.getNodes().get(imin).setMarked(true);
			DeviceList.getNodes().get(imin).setVisited(true);			
			Layer.getMapViewer().repaint();
			envelope.add(imin);
			//System.out.println(envelope);
			n = envelope.size()-1;
			px1 = DeviceList.getNodes().get(envelope.get(n-2)).getY();
			py1 = DeviceList.getNodes().get(envelope.get(n-2)).getX();
			px2 = DeviceList.getNodes().get(envelope.get(n-1)).getY();
			py2 = DeviceList.getNodes().get(envelope.get(n-1)).getX();
			px3 = DeviceList.getNodes().get(envelope.get(n)).getY();
			py3 = DeviceList.getNodes().get(envelope.get(n)).getX();
			//System.out.println(px1+" "+py1+" "+px2+" "+py2+" "+px3+" "+py3);
			r = right(px1-px2,py1-py2,px3-px2,py3-py2);			
			//System.out.println(r?"DROITE":"GAUCHE");
			
			while(!r) {
				DeviceList.getNodes().get(envelope.get(n-1)).setMarked(false);				
				Layer.getMapViewer().repaint();
				envelope.remove(n-1);
				n--;
				if(n>0) {
					px1 = DeviceList.getNodes().get(envelope.get(n-2)).getY();
					py1 = DeviceList.getNodes().get(envelope.get(n-2)).getX();
					px2 = DeviceList.getNodes().get(envelope.get(n-1)).getY();
					py2 = DeviceList.getNodes().get(envelope.get(n-1)).getX();
					px3 = DeviceList.getNodes().get(envelope.get(n)).getY();
					py3 = DeviceList.getNodes().get(envelope.get(n)).getX();
					r = right(px1-px2,py1-py2,px3-px2,py3-py2);
				}
				else r=true;
				delay();
			}
			delay();
			//System.out.println(envelope);
			//System.out.println("----------");
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
	
	public void delay() {
		try {
			sleep(10);
		} catch (InterruptedException e) {}		
	}
}
