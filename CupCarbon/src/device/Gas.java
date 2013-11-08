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
public class Gas extends DeviceWithoutRadio {

	private static String idFL = "G" ; // ID First Letter
	
	private int duree = 1000 ;
	
	public Gas(double x, double y, double radius) {
		super(x, y, radius);
	}
	
	public Gas(String x, String y, String radius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius));
	}

	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			//int mx = getPixelMX() ;
			//int my = getPixelMY() ;
			//int dx = getPixelDX() ;
			//int dy = getPixelDY() ;
			int rayon = MapCalc.rayonEnPixel(this.radius) ;		
			
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
			
			/*
			if(inside) {			
				g.setColor(Couleur.ROUGEF_TRANSPARENT);			
			}
			else
				g.setColor(Couleur.ROUGE_TRANSPARENT);
	
			switch(hide) {
			case 0 : g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			case 1 : g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2); 
			}
			*/
					
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
				//if(deplacement) {	
					//int mx = (int) this.mx ;
					//int my = (int) this.my ;
					//int dx = (int) this.dx ;
					//int dy = (int) this.dy ;
				//	g.setColor(Color.LIGHT_GRAY);
				//	g.drawOval(mx-dx - rayon, my-dy - rayon, rayon * 2, rayon * 2);
				//	g.drawLine(mx-dx, my-dy, x, y);
				//}
			}	
			g.setColor(Color.orange);		
			g.fillOval(x-6, y-6, 12, 12);
			
			double rayon2 ;
			double rayon3 ;
			Random rnd = new Random();
			double v ;
			g.setColor(UColor.ORANGE) ;		
			int fxo = x+MapCalc.rayonEnPixel(this.radius) ;
			int fyo = y ;
			int fx = fxo ;
			int fy = fyo ;
			
			for(double i=0.2; i<6.28; i+=.2) {
				v = MapCalc.rayonEnPixel(this.radius)*(rnd.nextInt(20)-10)/100.;
				rayon2 = MapCalc.rayonEnPixel(this.radius*Math.cos(i))+v ;
				rayon3 = MapCalc.rayonEnPixel(this.radius*Math.sin(i))+v ;					
				if(hide!=2)
					g.drawLine(fx, fy, (int)(x+rayon2), (int)(y+rayon3));
				if(hide==0)
					g.drawLine(x, y, (int)(x+rayon2), (int)(y+rayon3));
				fx = (int)(x+rayon2) ;
				fy = (int)(y+rayon3) ;
			}
			if(hide!=2) g.drawLine(fx, fy, fxo, fyo);
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
			}
			
			if(displayDetails) {
				g.setColor(Color.RED);
				g.drawString(""+duree+" mn", x+15, y+20);
			}
			
			if(underSimulation) {
				g.setColor(UColor.VERT);
				g.fillOval(x-3, y-3, 6, 6);
			}
			else {
				g.setColor(UColor.ROUGE);
				g.fillOval(x-3, y-3, 6, 6);
			}
			drawId(x,y,g);
		}
	}
	
	@Override
	public void run() {
		selected = false ;
		underSimulation = true ;
		while(duree>0) {
			radius++ ;
			duree-- ;
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		underSimulation = false ;
		thread = null ;
		Layer.getMapViewer().repaint();
	}

	@Override
	public int getType() {
		return Device.GAS;
	}
	
	@Override
	public double getCaptureUnitRadius() {
		return radius ;
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
	public void setCOMFileName(String comFileName) {
		// TODO Auto-generated method stub
		
	}
}
