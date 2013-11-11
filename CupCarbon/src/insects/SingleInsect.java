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

package insects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import map.Layer;
import utilities.MapCalc;
import device.Device;
import device.MobileG;

/**
 * @author Ahcene Bounceur
 * @author Mahamadou Traore
 */
public class SingleInsect extends MobileG implements Runnable {

	protected double x = 0;
	protected double y = 0;
	protected double xc = 0;
	protected double yc = 0;
	private double direction;
	// Insect parameters
	private double speedOnX = .00001;
	private double speedOnY = .00001;
	private double rotationAngle = .3;
	private double dispersion = 100.;

	// ------------------------------------

	public SingleInsect(double x, double y, int theta) {
		this.x = x;
		this.y = y;
		this.direction = theta;
	}

	public SingleInsect(double x, double y, boolean dispersion) {
		generate(x, y, dispersion);
	}

	public void generate(double x, double y, boolean dispersion) {
		double d1 = 0;
		double d2 = 0;
		if (dispersion) {
			d1 = Math.random() / this.dispersion;
			d2 = Math.random() / this.dispersion;
		}
		this.x = x + d1;
		this.y = y + d2;
		direction = (int) (Math.random() * 360);
	}

	// ------------------------------------

	/**
	 * Move the insect to the new direction
	 * 
	 * @param newDirection
	 *            Angle in degree of the new direction
	 */
	public void move(double newDirection) {
		double left = (newDirection - direction + 360) % 360;
		double right = (direction - newDirection + 360) % 360;
		double thetaChange = 0;
		if (left < right) {
			thetaChange = Math.min(rotationAngle, left);
		} else {
			thetaChange = -Math.min(rotationAngle, right);
		}
		direction = (direction + thetaChange + 360) % 360;
		x += (speedOnX * Math.cos(direction * Math.PI / 180));
		y -= (speedOnY * Math.sin(direction * Math.PI / 180));
	}

	/**
	 * Draw the insect
	 * 
	 * @param gg
	 *            Graphics
	 */
	@Override
	public void draw(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
		int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
		int v = 5;
		g.setColor(Color.BLACK);
		g.fillArc((int) x - v, (int) y - v, v * 2, v * 2,
				(int) direction - 90 - 20, 40);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDirection() {
		return direction;
	}

	@Override
	public void run() {
		double distance;
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x1, y1, x2, y2;
		try {
			Thread.sleep((int) (1000 * Math.random()));
			if (!gpsFileName.equals("")) {
				fis = new FileInputStream(gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				b.readLine();
				b.readLine();
				b.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		long tmpTime = -3600000;
		long cTime = 0;
		long toWait = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		while (true) {
			if (b == null)
				break;
			try {
				if (((s = b.readLine()) != null)) {
					x2 = x;
					y2 = y;
					ts = s.split(" ");
					cTime = simpleDateFormat.parse(ts[0]).getTime();
					toWait = cTime - tmpTime;
					tmpTime = cTime;
					x1 = Double.parseDouble(ts[1]) + Math.random() / dispersion;
					y1 = Double.parseDouble(ts[2]) + Math.random() / dispersion;
					xc = MapCalc.geoToIntPixelMapX(x1, y1);
					yc = MapCalc.geoToIntPixelMapY(x1, y1);
					if (firstTime) {
						x = x1;
						y = y1;
						firstTime = false;
					} else {
						distance = 1.35 * MapCalc.distance(x1, y1, x2, y2);
						int d = 1;
						for (int i = 0; i < distance; i++) {
							move(getAngle(x1, y1, x2, y2));
							Layer.getMapViewer().repaint();
							Thread.sleep(d);
						}
					}
				} else {
					break;
				}
				try {
					Thread.sleep(toWait / 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Layer.getMapViewer().repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (b != null)
				b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculate the angle between two points (x1,y1) and (x2,y2) in degree
	 * 
	 * @param x1
	 *            x of the first point
	 * @param y1
	 *            y of the second point
	 * @param x2
	 *            x of the first point
	 * @param y2
	 *            y of the secong point
	 * @return the angle in degree
	 */
	public double getAngle(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double alpha = Math.atan(dy / dx);
		alpha = 180 * alpha / Math.PI;
		if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
			return (180 - (int) alpha);
		else
			return (-(int) alpha);
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	@Override
	public int getType() {
		return Device.INSECT;
	}

	@Override
	public void setRadioRadius(double radiuRadius) {
	}

	@Override
	public void setCaptureRadius(double captureRadius) {
	}

	@Override
	public String getIdFL() {
		return "I";
	}

	public void setXYFromMouse(int xm, int ym) {
		Point p = new Point(xm, ym);
		GeoPosition gp = Layer.getMapViewer().convertPointToGeoPosition(p);
		x = gp.getLatitude();
		y = gp.getLongitude();
	}

}
