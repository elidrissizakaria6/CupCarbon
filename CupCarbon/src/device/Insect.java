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

import insects.Insect2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import utilities.MapCalc;
import utilities.UColor;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Insect extends MobileG {

	private static String idFL = "I" ; // ID First Letter
	private Vector<Insect2> insects;
	private int height = 100000000;
	private int width = 600000000;
	
	public Insect() {		
		insects = new Vector<Insect2>();
	}
	
	public Insect(double x, double y, double radius) {
		super(x,y,radius,"");
		insects = new Vector<Insect2>();
	}
	
	public Insect(double x, double y, double radius, int n) {
		super(x,y,radius,"");
		insects = new Vector<Insect2>();
		for (int i = 0; i < n; i++) {
			insects.add(new Insect2(x, y, true));
		}
	}
	
	public Insect(double x, double y, double radius, String gpsFileName) {
		super(x, y, radius, gpsFileName);
	}
	
	public Insect(String x, String y, String radius, String gpsFileName) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius), gpsFileName);
	}
	
	public Point closestLocation(double px, double py, double ox, double oy) {
		double dX = Math.abs(ox - px);
		double dY = Math.abs(oy - py);
		double x = ox;
		double y = oy;
		if (Math.abs(height - ox + px) < dX) {
			dX = height - ox + px;
			x = ox - height;
		}
		if (Math.abs(height - px + ox) < dX) {
			dX = height - px + ox;
			x = ox + height;
		}

		if (Math.abs(width - oy + py) < dY) {
			dY = width - oy + py;
			y = oy - width;
		}
		if (Math.abs(width - py + oy) < dY) {
			dY = width - py + oy;
			y = oy + width;
		}

		return new Point((int) x, (int) y);
	}

	public Point normalisePoint(Point p, double n) {
		if (sizeOfPoint(p) == 0.0) {
			return p;
		} else {
			double weight = n / sizeOfPoint(p);
			return new Point((int) (p.x * weight), (int) (p.y * weight));
		}
	}

	public double sizeOfPoint(Point p) {
		return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
	}

	public Point sumPoints(double p1x, double p1y, double w1, double p2x,
			double p2y, double w2) {
		return new Point((int) (w1 * p1x + w2 * p2x), (int) (w1 * p1y + w2
				* p2y));
	}

	@Override
	public void run() {
		for (Insect2 insect2 : insects)
			insect2.start();
	}

	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;			
			int x = MapCalc.geoToIntPixelMapX(this.x, this.y) ;
			int y = MapCalc.geoToIntPixelMapY(this.x, this.y) ;		
			int rayon = MapCalc.rayonEnPixel(this.radius) ;		
			
			for (Insect2 insect2 : insects) {
				insect2.setX(x);
				insect2.setY(y);
				insect2.draw(g);
			}
			
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
			}
			g.setColor(UColor.BLANC_TRANSPARENT);
			g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			drawMoveArrows(x,y,g) ;
			drawIncRedDimNode(x,y,g);
			drawRadius(x,y,rayon,g);
			
			if(displayDetails) {
				g.setColor(Color.RED);
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
	public void setCOMFileName(String comFileName) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		for (Insect2 insect2 : insects) {
			insect2.mouseMoved(e);
		}
	}
	
	@Override
	public void setGPSFileName(String gpsFileName) {
		for (Insect2 insect2 : insects) {
			insect2.setGPSFileName(gpsFileName);
		}		
	}
}
