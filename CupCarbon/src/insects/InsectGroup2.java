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

import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class InsectGroup2 {
	private Vector<Insect2> insect2s;
	private int height = 100000000;
	private int width = 600000000;

	public InsectGroup2() {
		insect2s = new Vector<Insect2>();
	}

	public InsectGroup2(int n) {
		insect2s = new Vector<Insect2>();
		for (int i = 0; i < n; i++) {
			insect2s.add(new Insect2(48.39188295873048, -4.44371223449707, true));
		}
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

	public void draw(Graphics g) {		
		for (Insect2 insect2 : insect2s) {
			insect2.draw(g);
		}
	}

	public void lancer() {
		for (Insect2 insect2 : insect2s)
			insect2.start();
	}
}
