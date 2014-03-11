/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

package device;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

public class Marker extends Device {

	private static String idFL = "M" ; // ID First Letter
	
	public Marker(double x, double y, double radius) {
		super(x, y, radius);
	}
	
	public Marker(String x, String y, String radius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius));
	}
		
	@Override
	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.radius) ;
					
			if (inside || selected) {
				g.setColor(UColor.NOIR_TRANSPARENT);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);			
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			
			if(hide==0) {
				g.setColor(Color.RED);
				g.fillOval(x-3, y-3, 6, 6);
			}
		}
	}	
	
	public static Marker getCentre(Marker marker1, Marker marker2, boolean b) {
		// b = true : the created node will be selected
		double x1 = marker1.getX();
		double y1 = marker1.getY();
		double x2 = marker2.getX();
		double y2 = marker2.getY();
		double x = x1+((x2-x1)/2.0);
		double y = y1+((y2-y1)/2.0);
		Marker marker = new Marker(x, y, 25) ; 
		if(b) marker.setSelection(true);
		return marker;
	}
	
	@Override
	public void run() {
		selected = false ;
	}

	@Override
	public int getType() {
		return Device.MARKER;
	}
	
	@Override
	public String getIdFL() {
		return idFL ;
	}
	
	@Override
	public String getNodeIdName() {
		return getIdFL()+id;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);
		if(selected) {
			if(e.getKeyChar()=='u') {
				//System.out.println(this.getNodeIdName());
				int ix = MarkerList.getIndex(this)+1 ;
				if(ix<MarkerList.size()) {
					Layer.addMarker(ix,getCentre(this,MarkerList.get(ix),true));
				}
			
			}
			if(e.getKeyChar()=='t') {
				DeviceList.add(new Sensor(x, y, 0, 100, 20));
			}
		}
	}

	@Override
	public void setRadioRadius(double radiuRadius) {}

	@Override
	public void setCaptureRadius(double captureRadius) {}

	@Override
	public void setGPSFileName(String gpsFileName) {}
	
	@Override
	public String getGPSFileName() {
		return "" ;
	}

	@Override
	public void setScriptFileName(String comFileName) {
		
	}
	
	@Override
	public int getNextTime() { return 0 ;}
	
	@Override
	public void loadRouteFromFile() {}
	
	@Override
	public void exeNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}
}