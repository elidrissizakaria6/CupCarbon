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
import java.util.Random;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class SimpleInsect extends MobileG {
	
	private int duree = 5000 ;
	
	public SimpleInsect() {}
	
	public SimpleInsect(double x, double y, double radius) {
		super(x,y,radius,"");
	}
	
	public SimpleInsect(double x, double y, double radius, String gpsFileName) {
		super(x, y, radius, gpsFileName);
	}
	
	public SimpleInsect(String x, String y, String radius, String gpsFileName) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius), gpsFileName);
	}

	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x, this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x, this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.radius) ;		
			
			if (inside || selected) {
				g.setColor(UColor.ORANGE);
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
				//if(deplacement) {
				//	g.setColor(Color.LIGHT_GRAY);
				//	g.drawOval(mx-dx - rayon, my-dy - rayon, rayon * 2, rayon * 2);
				//	g.drawLine(mx-dx, my-dy, x, y);
				//}
			}	
			//g.setColor(Color.orange);		
			//g.fillOval(x-6, y-6, 12, 12);
			
	
			int fxo = x+MapCalc.radiusInPixels(this.radius) ;
			int fyo = y ;
			int fx = fxo ;
			int fy = fyo ;		
			double rayon2 ;
			double rayon3 ;
			Random rnd = new Random();
			double v ;
			double v2 ;
			double v3 ;
						
			int r = (int) ((this.radius)/1.1) ;
			for(double j=1; j<8; j+=.2) {
				r = (int) ((this.radius)/j) ;
				for(double i=0; i<6.28; i+=.4) {
					v = MapCalc.radiusInPixels(r)*(rnd.nextInt(20)-10)/100.;
					v2 = rnd.nextInt(30)-15;
					v3 = rnd.nextInt(30)-15;
					rayon2 = MapCalc.radiusInPixels(r*Math.cos(i))+v ;
					rayon3 = MapCalc.radiusInPixels(r*Math.sin(i))+v ;
					g.setColor(UColor.NOIR_TRANSPARENT) ;
					if(hide==0)
						g.fillOval((int)(x+rayon2+v2), (int)(y+rayon3+v3), (int)MapCalc.radiusInPixels(4*Layer.getMapViewer().getZoom()), 
							(int)MapCalc.radiusInPixels(4*Layer.getMapViewer().getZoom()));
					if(j==1) {
						g.setColor(UColor.WHITE_TRANSPARENT) ;
						g.drawLine(fx, fy, (int)(x+rayon2), (int)(y+rayon3));
						fx = (int)(x+rayon2) ;
						fy = (int)(y+rayon3) ;
					}
				}
				if(j==1) g.drawLine(fx, fy, fxo, fyo);
			}
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			//if(displayRadius) {
			//	drawRadius(x, y, rayon, g);
			//}
			
			if(displayDetails) {
				g.setColor(Color.RED);
				g.drawString(""+duree+" mn", x+15, y+20);
			}
			
			if(underSimulation) {
				g.setColor(UColor.VERT);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(Color.BLUE);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}
	
	/*
	@Override
	public void run() {
		selected = false ;
		Random rnd = new Random() ;
		int d ;
		double ro = radius ;
		seSimule = true ;
		while(duree>0) {
			d = rnd.nextInt(30)-15;
			radius = ro+(ro*d/100.);
			x += .00002 ;
			y -= .00002 ;
			duree-- ;
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		Layer.getMapViewer().repaint();
		seSimule = false ;
		thread = null ;		
	}
	*/

	@Override
	public int getType() {
		return Device.INSECT;
	}

	@Override
	public double getCaptureUnitRadius() {
		return radius ;
	}
	
	@Override
	public String getIdFL() {
		return "SI" ;
	}
	
	@Override
	public String getNodeIdName() {
		return getIdFL()+id;
	}

	@Override
	public void setRadioRadius(double radiuRadius) {}

	@Override
	public void setCaptureRadius(double captureRadius) {}

	@Override
	public void setCOMFileName(String comFileName) {
		
	}
}
