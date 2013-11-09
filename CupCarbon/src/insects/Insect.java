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
import java.util.Random;

import map.Layer;
import utilities.MapCalc;

public class Insect extends Thread {

	protected double x = 0;
	protected double y = 0;
	private double theta;
	private double speed1 = .00001;
	private double speed2 = .00001;
	private double maxTheta = .5;
	private double dispr = 80.;
	private Random random = new Random();

	// ------------------------------------

	public Insect(double x, double y, int theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public Insect(double x, double y, boolean dispersion) {
		generate(x, y, dispersion);
	}

	public void generate(double x, double y, boolean dispersion) {
		double d1 = 0 ;
		double d2 = 0 ;
		if (dispersion) {
			d1 = Math.random() / dispr ;
			d2 = Math.random() / dispr ;
		}
		this.x = x + d1;
		this.y = y + d2;
		theta = (int) (Math.random() * 360);
	}

	// ------------------------------------

	public void move(double newHeading) {
		double left = (newHeading - theta + 360) % 360;
		double right = (theta - newHeading + 360) % 360;
		double thetaChange = 0;
		if (left < right) {
			thetaChange = Math.min(maxTheta, left);
		} else {
			thetaChange = -Math.min(maxTheta, right);
		}
		theta = (theta + thetaChange + 360) % 360;
		x += (speed1 * Math.cos(theta * Math.PI / 180));
		y -= (speed2 * Math.sin(theta * Math.PI / 180));
	}

	public void draw(Graphics gg) {
		int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
		int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
		double e = .5;
		x += random.nextGaussian() * e;
		y += random.nextGaussian() * e;
		int v = 4;
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		
		//g.setColor(Color.ORANGE);
		//g.drawOval((int) x - v-2, (int) y - v-2, v * 4, v * 4);
		g.setColor(Color.DARK_GRAY);
		g.fillArc((int) x - v, (int) y - v, v * 2, v * 2,
		(int) theta - 90 - 20, 40);

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getTheta() {
		return theta;
	}

	@Override
	public void run() {
		double distance;
		String gpsFileName = "trajet/trajet1.gps";
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x1, y1, x2, y2;
		try {
			sleep((int) (1000 * Math.random()));
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
			try {
				if (b != null) {
					if (((s = b.readLine()) != null)) {
						x2 = x;
						y2 = y;
						ts = s.split(" ");
						cTime = simpleDateFormat.parse(ts[0]).getTime();
						toWait = cTime - tmpTime;
						tmpTime = cTime;
						x1 = Double.parseDouble(ts[1]) + Math.random() / dispr;
						y1 = Double.parseDouble(ts[2]) + Math.random() / dispr;
						if (firstTime) {
							x = x1;
							y = y1;
							firstTime = false;
						} else {
							distance = 1.35 * utilities.MapCalc.distance(x1,
									y1, x2, y2);
							int d = 1;
							for (int i = 0; i < distance; i++) {
								move(getAngle(x1, y1, x2, y2));
								Layer.getMapViewer().repaint();
								sleep(d);
							}
						}
					} else {
						break;
					}
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

		// try {
		// sleep(500);
		// int d = 1;
		// int ss = 5000;
		// for (int i = 0; i < ss; i++) {
		// move(0);
		// Layer.getMapViewer().repaint();
		// sleep(d);
		// }
		/*
		 * for (int i = 0; i < s; i++) { move(0);
		 * Layer.getMapViewer().repaint(); sleep(d); }
		 * 
		 * for (int i = 0; i < s; i++) { move(45);
		 * Layer.getMapViewer().repaint(); sleep(d); }
		 * 
		 * for (int i = 0; i < s; i++) { move(90);
		 * Layer.getMapViewer().repaint(); sleep(d); } for (int i = 0; i < s;
		 * i++) { move(0); Layer.getMapViewer().repaint(); sleep(d); } for (int
		 * i = 0; i < s; i++) { move(180); Layer.getMapViewer().repaint();
		 * sleep(d); }
		 */
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

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
}
