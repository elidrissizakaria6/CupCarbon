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
import java.awt.event.MouseEvent;
import java.util.Random;

import map.Layer;
import mt_simulation.DeviceSimulator;
import utilities.MapCalc;
import utilities.UColor;
import battery.Battery;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public abstract class DeviceWithRadio extends DeviceWithWithoutRadio {

	protected Battery battery ;
	protected double porteeErr = .4 ;
	protected Random random = new Random() ;
	
	protected double radioRangeRadius = 0 ;
	protected double radioRangeRadiusOri = 0 ;
	//protected double signalPower = 1 ; // 1 = 100%
	protected boolean augmenterRadio = false ;
	protected boolean reduireRadio = false ;
	
	/**
	 * 
	 */
	public DeviceWithRadio() {
		this(0,0,0,0);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param radioRangeRadius
	 */
	public DeviceWithRadio(double x, double y, double radius, double radioRangeRadius) {
		super(x, y, radius);
		deviceSimulator = new DeviceSimulator(this);
		this.radioRangeRadius = radioRangeRadius ;
		radioRangeRadiusOri = radioRangeRadius ;
	}

//	@Override
//	public void setCOMFileName(String comFileName){
//		System.out.println("set COM File");
//		Scenariofile = comFileName;
//		simulator.scenariofile = comFileName;
//	}
//	
//	@Override
//	public void initSimulator(Simulation sim)
//	{
//		simulation = sim;
//		System.out.println("DS init 1");
//		simulator.init(simulation);
//		System.out.println("DS init 2");
//
//	}

	/* (non-Javadoc)
	 * @see device.Device#getRadioRadius()
	 */
	@Override
	public double getRadioRadius() {
		return radioRangeRadius ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#getBattery()
	 */
	@Override
	public Battery getBattery() {
		return battery;
	}
	
	public double getRadioRadiusOri() {
		return radioRangeRadiusOri ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#setRadioRadius(double)
	 */
	@Override
	public void setRadioRadius(double radioRadius) {
		this.radioRangeRadius = radioRadius ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#setCaptureRadius(double)
	 */
	@Override 
	public void setCaptureRadius(double captureRadio) {
		
	}
	
	/* (non-Javadoc)
	 * @see device.Device#getMaxRadius()
	 */
	@Override
	public double getMaxRadius() {
		return Math.max(radius, getRadioRadius()) ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#getGPSFileName()
	 */
	@Override
	public String getGPSFileName() {
		return "" ;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param g
	 */
	public void dessinAugDimRadio(int x, int y, Graphics g) {
		if(reduireRadio || augmenterRadio) {
			g.setColor(UColor.BLUE);
			g.drawLine(x-10, y-2, x+10, y-2);
			g.drawLine(x-10, y+2, x+10, y+2);
		}
		if(augmenterRadio) {
			g.drawLine(x-2, y-10, x-2, y+10);
			g.drawLine(x+2, y-10, x+2, y+10);
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		augmenterRadio = false ;
		reduireRadio = false ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		if (augmenterRadio) {			
			radioRangeRadius += 30;
			radioRangeRadiusOri += 30 ;
			Layer.getMapViewer().repaint();
		}
		if (reduireRadio) {
			if(radioRangeRadius>0) { 
				radioRangeRadius -= 30 ;
				radioRangeRadiusOri -= 30 ;
			}
			Layer.getMapViewer().repaint();
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent key) {
		super.keyTyped(key);
		
		if(selected) {
			if(key.getKeyChar()=='+') {
				move = false ;
				reduireRadio = false ;
				augmenterRadio = !augmenterRadio ;
				radioRangeRadius+=5 ;
				radioRangeRadiusOri+=5 ;
				Layer.getMapViewer().repaint();
			}
			if(key.getKeyChar()=='-') {
				move = false ;
				augmenterRadio = false ;
				reduireRadio = !reduireRadio ;
				if(radioRangeRadius>0) { 
					radioRangeRadius-=5 ;
					radioRangeRadiusOri-=5 ;
				}
				Layer.getMapViewer().repaint();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#selectInit()
	 */
	@Override
	public void initSelection() {		
		super.initSelection() ;
		augmenterRadio = false ;
		reduireRadio = false ;
	}
	
	/* (non-Javadoc)
	 * @see device.Device#drawRadius(int, int, int, java.awt.Graphics)
	 */
	public void drawRadius(int x, int y, int r1, Graphics g) {
		if(r1>0 && displayRadius) {
			g.setColor(UColor.WHITED_TRANSPARENT);
			int lr1 = (int) (r1*Math.cos(Math.PI/4.));
			g.drawLine(x,y,(int)(x+lr1),(int)(y-lr1));
			g.drawString(""+getRadioRadius(),x+(lr1/2),(int)(y-(lr1/4.)));
		}
	}	
	
	/**
	 * @param x
	 * @param y
	 * @param r2
	 * @param g
	 */
	public void drawRadioRadius(int x, int y, int r2, Graphics g) {
		if(r2>0 && displayRadius) {
			g.setColor(UColor.WHITED_TRANSPARENT);
			int lr2 = (int) (r2*Math.cos(Math.PI/4.));
			g.drawLine(x,y,x-lr2,y-lr2);
			g.drawString(""+radius,x-lr2,y-lr2);
		}
	}
	
	/* (non-Javadoc)
	 * @see device.Device#consume(double)
	 */
	@Override 
	public void consume(double v) {
		battery.consume(v);
	}
	
	@Override
	public void drawSelectedByAlgo(Graphics g) {		
		if(selectedByAlgo) {	
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(x, y) ;
			//int y = MapCalc.geoToIntPixelMapY(x, y) ;	
			int r = MapCalc.radiusInPixels(this.radioRangeRadius) ;
			g.setColor(Color.RED);
			if(hide==2) {
				g.setColor(Color.gray);
			}
			if(hide != 3) {
				g.drawOval(x-r-3, y-r-3, (r+3)*2, (r+3)*2);
				g.drawOval(x-r-4, y-r-4, (r+4)*2, (r+4)*2);
				g.setColor(UColor.VERTF_TRANSPARENT);
				g.fillOval(x-r-3, y-r-3, (r+3)*2, (r+3)*2);
			}
			g.setColor(Color.DARK_GRAY);
			g.drawOval(x-6, y-6, 12, 12);
			g.drawOval(x-7, y-7, 14, 14);
		}
	}
	
}
