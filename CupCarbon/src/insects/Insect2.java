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

public class Insect2 extends Thread {

	protected double x = 0;
	protected double y = 0;
	private double theta;
	private double speed1 = .00001;// 4;
	private double speed2 = .00001;// 4;
	private double maxTheta = .05;
	private double dispr = 50.;
	private Random random = new Random();

	// ------------------------------------

	public Insect2(double x, double y, int theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public Insect2() {
		generate();
	}

	public void generate() {
		x = 48.39188295873048 + Math.random() / dispr;
		y = -4.44371223449707 + Math.random() / dispr;
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
		int v = 6;
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.DARK_GRAY);

		g.fillArc((int) x - v, (int) y - v, v * 2, v * 2,
				(int) theta - 90 - 20, 40);

	}

	public double getDistance(Insect2 insect) {
		double dX = insect.getX() - x;
		double dY = insect.getY() - y;

		return (int) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}

	public double getDistance(Point p) {
		double dX = p.x - x;
		double dY = p.y - y;

		return (int) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
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

	// public void setSpeed(double speed) {
	// this.speed = speed;
	// }

	public void setMaxTurnTheta(int theta) {
		maxTheta = theta;
	}

	public void drawRanges(Graphics g) {
		drawCircles(g, (int) x, (int) y);

		// int radius = Obstacle.Ranges.detectionRange;

		// boolean top = (y < Ranges.detectionRange);
		// boolean bottom = (y > Ranges.width - Ranges.detectionRange);

		// if (x < Ranges.detectionRange) { // if left
		// drawCircles(g, Ranges.height + x, y);
		// if (top) {
		// drawCircles(g, Ranges.height + x, Ranges.width + y);
		// } else if (bottom) {
		// drawCircles(g, Ranges.height + x, y - Ranges.width);
		// }
		// } else if (x > Ranges.height - Ranges.detectionRange) { // if right
		// drawCircles(g, x - Ranges.height, y);
		// if (top) {
		// drawCircles(g, x - Ranges.height, Ranges.width + y);
		// } else if (bottom) {
		// drawCircles(g, x - Ranges.height, y - Ranges.width);
		// }
		// }
		// if (top) {
		// drawCircles(g, x, Ranges.width + y);
		// } else if (bottom) {
		// drawCircles(g, x, y - Ranges.width);
		// }
	}

	/**
	 * Draws the range circles around the bird. The inner circle is the point at
	 * which birds will separate to aviod the obstacle. The outer circle is the
	 * range at which the birds can detect the obstacle.
	 * 
	 * @param g
	 *            The graphics object to draw the bird on.
	 * @param x
	 *            The X coordinate of the bird
	 * @param y
	 *            The Y coordinate of the bird
	 */
	protected void drawCircles(Graphics g, int x, int y) {
		g.setColor(Color.gray);
		g.drawOval(x - Ranges.detectionRange, y - Ranges.detectionRange,
				2 * Ranges.detectionRange, 2 * Ranges.detectionRange);
		// g.setColor(Color.LIGHT_GRAY);
		// g.drawOval(x - Ranges.separationRange, y - Ranges.separationRange,
		// 2 * Ranges.separationRange, 2 * Ranges.separationRange);
	}


	public void run2() {
	}
	
	@Override
	public void run() {
		String gpsFileName = "trajet/trajet1.gps";
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x2, y2;
		try {
			sleep(1000);
			if (!gpsFileName.equals("")) {
				fis = new FileInputStream(gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				String desc_str = b.readLine();
				String from_str = b.readLine();
				String to_str = b.readLine();
				System.out.println("Description : " + desc_str);
				System.out.println("From : " + from_str);
				System.out.println("To : " + to_str);
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
						x = Double.parseDouble(ts[1]);
						y = Double.parseDouble(ts[2]);
						if (firstTime)
							firstTime = false;
						else {
							double th = getAngle(x, y, x2, y2);
							int d = 1;
							int ss = 5000;
							for (int i = 0; i < ss; i++) {
								move(th);
								Layer.getMapViewer().repaint();
								sleep(d);
							}
						}
					} else {
						break;
					}
				}
				try {
					Thread.sleep(toWait / 10);
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
