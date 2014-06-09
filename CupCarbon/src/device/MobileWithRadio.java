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
import tracking_ws.TargetWS;
import utilities.MapCalc;
import utilities.UColor;

public class MobileWithRadio extends MobileGWR {

	private static String idFL = "W" ; // ID First Letter 
	
	public MobileWithRadio(double x, double y, double rayon, double radioRadius) {
		this(x, y, rayon, radioRadius,"");
		withRadio = true ;		
	}
	
	public MobileWithRadio(double x, double y, double rayon, double radioRadius, String gpsFileName) {
		super(x, y, rayon, radioRadius, gpsFileName);	
	}
	
	public MobileWithRadio(String x, String y, String rayon, String radioRadius, String gpsFileName) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(rayon), Double.valueOf(radioRadius), gpsFileName);	
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
			int rayon = MapCalc.radiusInPixels(getRadioRadius()) ;
			int rayon2 = MapCalc.radiusInPixels(this.radius) ;
					
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
	
			switch(hide) {
			case 0 : {
				g.setColor(UColor.NOIRF_TTTRANSPARENT);
				g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
			}
			case 1 : 
				g.setColor(UColor.WHITE_LTRANSPARENT);
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2); 
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			dessinAugDimRadio(x,y,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
				drawRadioRadius(x, y, rayon2, g);
			}
			
			if(underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(Color.BLUE);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}
	
	@Override
	public int getType() {
		return Device.MOBILE_WR;
	}
	
	@Override
	public String getIdFL() {
		return idFL ;
	}
	
	@Override
	public void keyTyped(KeyEvent key) {
		super.keyTyped(key);
		if(selected) {
			if(key.getKeyChar()==';') {
				radius+=10 ;
				Layer.getMapViewer().repaint();
			}
			if(key.getKeyChar()==',') {
				radius-=10 ;
				Layer.getMapViewer().repaint();
			}
		}
	}
	
	@Override
	public String getNodeIdName() {
		return getIdFL()+id;
	}
	
	@Override
	public boolean canMove() {
		return false;
	}
	
	/**
	 * KH
	 */
	@Override
	public void runSensorSimulation() {
		loadRouteFromFile();
		fixori();
		if (readyForSimulation) {
			underSimulation = true;
			routeIndex = 0;
			selected = false;
			long tmpTime = 0;
			long cTime = 0;
			long toWait = 0;
			do {
				cTime = routeTime.get(routeIndex);
				toWait = cTime - tmpTime;
				tmpTime = cTime;
				if (toWait < 0) {
					toWait = cTime;
				}
				x = routeX.get(routeIndex);
				y = routeY.get(routeIndex);
				// Le sensot enregistre sa position dans la base de donn�es 
			
				
				// Ecrire la coordonn�e de la cible
				//client.setLastPoint(2, new Point(x,y));
				TargetWS.setCoords(this.getNodeIdName(), x, y);
				Layer.getMapViewer().repaint();
				try {
					Thread.sleep(toWait * Device.moveSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				goToNext();
			} while (hasNext());
			routeIndex = 0;
			selected = false;
			toori();
			thread = null;
			underSimulation = false;
			Layer.getMapViewer().repaint();
		}
	}
	
	//TrackingServiceClient client = new TrackingServiceClient();
	
	
}