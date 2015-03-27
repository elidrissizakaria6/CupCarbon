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

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;
import battery.Battery;

public class Router extends DeviceWithRadio {

private static String idFL = "R" ; // ID First Letter
	
	public Router(double x, double y, double radius, double radioRadius) {
		super(x, y, radius, radioRadius);		
		battery = new Battery(null) ;
		withRadio = true ;
	}
	
	public Router(String x, String y, String radius, String radioRadius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius) , Double.valueOf(radioRadius));		
		battery = new Battery(null) ;
		withRadio = true ;
	}
		
	@Override
	public void draw(Graphics g) {	
		if(visible) {
			initDraw(g) ;		
			int [] coord = MapCalc.geoToIntPixelMapXY(this.x,this.y) ;
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;	
			int rayon = MapCalc.radiusInPixels(getRadioRadius()) ;
			int rayon2 = MapCalc.radiusInPixels(this.radius) ;
			if(displayDetails) {
				g.setColor(UColor.GREEN);
				g.fillRect(x+rayon+10, y+rayon-(int)(battery.getCapacity()/1000000.), 5, (int)(battery.getCapacity()/1000000.));
				g.setColor(Color.gray);
				g.drawRect(x+rayon+10, y+rayon-(int)(battery.getCapacity()/1000000.), 5, (int)(battery.getCapacity()/1000000.));
				g.drawLine(x+rayon, y+rayon, x+rayon+25, y+rayon);
				g.drawString(""+battery.getCapacity()+" %", x+15+rayon, y+20+rayon);
			}
			
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
			if(inside) {			
				g.setColor(UColor.BLEUF_TRANSPARENT);
			}
			else
				g.setColor(UColor.BLEU_TRANSPARENT);
	
			switch(hide) {
			case 0 : {
				g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				g.setColor(UColor.BLEUF_TRANSPARENT);
				g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
			}
			case 1 : 
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawAugDimRadio(x,y,g);
			drawRadius(x, y, rayon, g);
			drawRadioRadius(x, y, rayon2, g);
			
			if(underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(UColor.RED);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}	
	
	@Override
	public double getCaptureUnitRadius() {
		return 0 ;
	}
	
	@Override
	public void run() {
		setRadioRadius(getRadioRadiusOri()) ;
		battery.init() ;
		selected = false ;
		underSimulation = true; 
		int err = 0 ;
		int rayonOri = (int) getRadioRadius() ;
		//for(int i=0;i<1000;i++) {
		while(!battery.empty()) { 
			battery.consume() ;
			err = (int)(getRadioRadius()*(porteeErr*random.nextGaussian())/100);
			setRadioRadius((rayonOri + err) - (rayonOri*(100-battery.getCapacity())/100)) ;
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		underSimulation=false;
		thread = null ;
		Layer.getMapViewer().repaint();
	}

	@Override
	public int getType() {
		return Device.BRIDGE;
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
	public void setScriptFileName(String comFileName) {
		
	}
	
	@Override
	public int getNextTime() { return 0 ;}
	
	@Override
	public void loadRouteFromFile() {}
	
	@Override
	public void moveToNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}
}