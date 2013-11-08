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

import utilities.MapCalc;

public class InsectSingle {

	protected double x = 0;
	protected double y = 0;
	private int theta;
	private double speed = 4;
	private int maxTheta = 10;

	// ------------------------------------

	public InsectSingle(double x, double y, int theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public InsectSingle() {
		this(48.39188295873048 + Math.random() / 100, -4.44371223449707
				+ Math.random() / 100, (int) (Math.random() * 360));
		// this((int) (Math.random() * Ranges.height),
		// (int) (Math.random() * Ranges.width),
		// (int) (Math.random() * 360));
	}

	// ------------------------------------

	public void move(int newHeading) {
		int left = (newHeading - theta + 360) % 360;
		int right = (theta - newHeading + 360) % 360;
		int thetaChange = 0;
		if (left < right) {
			thetaChange = Math.min(maxTheta, left);
		} else {
			thetaChange = -Math.min(maxTheta, right);
		}
		theta = (theta + thetaChange + 360) % 360;
		x += (int) (speed * Math.cos(theta * Math.PI / 180)) + Ranges.height;
		x %= Ranges.height;
		System.out.println(x);
		y -= (int) (speed * Math.sin(theta * Math.PI / 180)) - Ranges.width;
		y %= Ranges.width;
	}

	public void draw(Graphics gg) {
		int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
		int y = MapCalc.geoToIntPixelMapY(this.x, this.y);

		int v = 8;
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.black);
		// g.fillArc(x - 12, y - 12, 24, 24,
		// currentTheta + 180 - 20, 40);
		//
		// g.fillArc(x - v, y - v, v*2, v*2,
		// theta + 180 - 30, 60);
		// g.setColor(Color.white);
		// g.fillArc(x - v-2, y-2 - v, (v+2)*2, (v+2)*2,
		// theta + 180 - 20, 40);
		g.setColor(Color.black);
		g.fillArc(x - v, y - v, v * 2, v * 2, theta + 180 - 20, 40);
		// g.setColor(Color.lightGray);
		// g.drawOval(x - v, y - v, v*2, v*2);
		// drawRanges(g);
	}

	//

	public double getDistance(InsectSingle insect) {
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

	public int getTheta() {
		return theta;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setMaxTurnTheta(int theta) {
		maxTheta = theta;
	}

	public void drawRanges(Graphics g) {
		drawCircles(g, (int) x, (int) y);

		// int radius = Obstacle.Ranges.detectionRange;

		//boolean top = (y < Ranges.detectionRange);
		//boolean bottom = (y > Ranges.width - Ranges.detectionRange);

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
}
