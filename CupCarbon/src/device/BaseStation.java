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
public class BaseStation extends DeviceWithRadio {

	private static String idFL = "B"; // ID First Letter

	protected double porteeErr = 1;
	protected Random random = new Random();
	protected boolean mort = false;

	public BaseStation(double x, double y, double rayon, double radioRadius) {
		super(x, y, rayon, radioRadius);
		withRadio = true;
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param radioRadius
	 */
	public BaseStation(String x, String y, String radius, String radioRadius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius),
				Double.valueOf(radioRadius));
		withRadio = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#dessiner(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (visible) {
			initDraw(g);
			int [] coord = MapCalc.geoToIntPixelMapXY(this.x,this.y) ;
			int x = coord[0];
			int y = coord[1];
			// int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
			// int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
			int rayon = MapCalc.radiusInPixels(getRadioRadius());
			int rayon2 = MapCalc.radiusInPixels(this.radius);

			if (inside || selected) {
				g.setColor(UColor.NOIR_TRANSPARENT);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon + 2, y
						- rayon - 3);
				g.drawLine(x - rayon - 3, y - rayon - 3, x - rayon - 3, y
						- rayon + 2);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon + 2, y
						+ rayon + 3);
				g.drawLine(x - rayon - 3, y + rayon + 3, x - rayon - 3, y
						+ rayon - 2);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon - 2, y
						- rayon - 3);
				g.drawLine(x + rayon + 3, y - rayon - 3, x + rayon + 3, y
						- rayon + 2);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon - 2, y
						+ rayon + 3);
				g.drawLine(x + rayon + 3, y + rayon + 3, x + rayon + 3, y
						+ rayon - 2);
			}
			if (inside) {
				g.setColor(UColor.JAUNEF_TRANSPARENT);
			} else
				g.setColor(UColor.JAUNE_TRANSPARENT);

			switch (hide) {
			case 0: {
				g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				g.setColor(UColor.ORANGE_TRANSPARENT);
				g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
				// g.fillOval(x-5, y-25, 10, 10);
				/*
				 * g.setColor(UColor.NOIR_TRANSPARENT); g.drawOval(x-5, y-25,
				 * 10, 10); g.drawLine(x-5, y-20, x-20, y+20); g.drawLine(x-5,
				 * y-20, x-16, y+20); g.drawLine(x+5, y-20, x+20, y+20);
				 * g.drawLine(x+5, y-20, x+16, y+20);
				 */

				// g.drawLine(x, y-20, x-5, y-5);
				// g.drawLine(x, y-20, x+5, y-5);
				// g.drawLine(x-5, y-5, x+5, y-5);
			}
			case 1:
				g.setColor(UColor.NOIR_TTRANSPARENT);
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			}

			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
						(rayon + 4) * 2);
			}

			drawMoveArrows(x, y, g);
			drawIncRedDimNode(x, y, g);
			dessinAugDimRadio(x, y, g);
			if (displayRadius) {
				drawRadius(x, y, rayon, g);
				drawRadioRadius(x, y, rayon2, g);
			}

			if (underSimulation) {
				g.setColor(UColor.VERT);
				g.fillOval(x - 3, y - 3, 6, 6);
			} else {
				g.setColor(UColor.ROUGE);
				g.fillOval(x - 3, y - 3, 6, 6);
			}
			drawId(x, y, g);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		setRadioRadius(getRadioRadiusOri());
		selected = false;
		underSimulation = true;
		int err = 0;
		int rayonOri = (int) getRadioRadius();
		for (int i = 0; i < 10000; i++) {
			err = (int) (getRadioRadius() * (porteeErr * random.nextGaussian()) / 100);
			setRadioRadius(rayonOri + err);
			Layer.getMapViewer().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		underSimulation = false;
		thread = null;
		Layer.getMapViewer().repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#getType()
	 */
	@Override
	public int getType() {
		return Device.BASE_STATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#getIdFL()
	 */
	@Override
	public String getIdFL() {
		return idFL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#getNodeIdName()
	 */
	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#setGPSFileName(java.lang.String)
	 */
	@Override
	public void setGPSFileName(String gpsFileName) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see device.Device#setCOMFileName(java.lang.String)
	 */
	@Override
	public void setScriptFileName(String scriptFileName) {
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