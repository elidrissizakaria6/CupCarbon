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
	private int [] polyX = new int[62];
	private int [] polyY = new int[62];
	private double [] power = new double[62];
	
	private int duration = 300 ;
	
	{
		for(int i=0; i<62; i++) {
			power[i]=1;
		}
	}
	
	public Gas(double x, double y, double radius) {
		super(x, y, radius);
	}
	
	public Gas(String x, String y, String radius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius));
	}

	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(x, y) ;
			//int y = MapCalc.geoToIntPixelMapY(x, y) ;		
			//int mx = getPixelMX() ;
			//int my = getPixelMY() ;
			//int dx = getPixelDX() ;
			//int dy = getPixelDY() ;
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
			
			double rayon2=0;
			double rayon3=0;

			Random rnd = new Random();
			double v ;
			int fxo = x+MapCalc.radiusInPixels(this.radius) ;
			int fyo = y ;
			polyX[0]=fxo;
			polyY[0]=fyo;
			int k=1;			
			for(double i=0.2; i<6.28; i+=.1) {				
				v = MapCalc.radiusInPixels(this.radius)*(rnd.nextInt(12)-10)/100.;
				rayon2 = MapCalc.radiusInPixels(this.radius*power[k]*Math.cos(i)+v/1.) ;
				v = MapCalc.radiusInPixels(this.radius)*(rnd.nextInt(12)-10)/100.;
				rayon3 = MapCalc.radiusInPixels(this.radius*power[k]*Math.sin(i)+v/1.) ;
				polyX[k]=(int)(x+rayon2);
				polyY[k]=(int)(y+rayon3);
				k++;
			}
			if(hide!=2) {
				g.setColor(UColor.ORANGE) ;
				g.drawPolygon(polyX, polyY, 62);
			}
			if(hide==0) {
				g.setColor(UColor.ORANGE_TRANSPARENT);
				g.fillPolygon(polyX, polyY, 62);				
			}
			
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			if(displayRadius) {
				drawRadius(x, y, rayon, g);
			}
			
			if(displayDetails) {
				g.setColor(Color.RED);
				g.drawString(""+duration+" mn", x+15, y+20);
			}
			
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
	public void run() {
		selected = false ;
		underSimulation = true ;
		fixori();
		while(duration>0) {
			radius++ ;
//			power[10]+=.0024;
//			power[11]+=.0026;
//			power[12]+=.0028;
//			power[13]+=.0024;
//			power[14]+=.0022;
//			power[15]+=.002;
//			
//			power[20]+=.0001;
//			power[21]+=.0003;
//			power[22]+=.0004;
//			power[23]+=.0005;
//			power[24]+=.0002;
//			power[25]+=.0001;
//			
//			
//			power[30]+=.0001;
//			power[31]+=.0003;
//			power[32]+=.0002;
//			power[33]+=.0004;
//			power[34]+=.0003;
//			power[35]+=.0001;
//			
//			
//			power[50]+=.0012;
//			power[51]+=.0013;
//			power[52]+=.0014;
//			power[53]+=.0012;
//			power[54]+=.0011;
//			power[55]+=.001;
//			power[56]+=.0011;
			
			duration-- ;
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		toori();
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
	public void moveToNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}
	
}
