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
	private Vector<Insect2> insects;

	public InsectGroup2() {
		insects = new Vector<Insect2>();
	}

	public InsectGroup2(int n) {
		insects = new Vector<Insect2>();
		for (int i = 0; i < n; i++) {
			insects.add(new Insect2());
		}
	}

	int v;

	synchronized public void move() {
		for (Insect2 insect : insects)
			insect.move(generalHeading(insect));
	}

	private double generalHeading(Insect2 insect) {
		Point target = new Point(0, 0);
		int numInsects = 0;
		for (int i = 0; i < insects.size(); i++) {
			Insect2 otherInsect = (Insect2) insects.elementAt(i);
			Point otherLocation = closestLocation(insect.getX(), insect.getY(),
					otherInsect.getX(), otherInsect.getY());

			double distance = insect.getDistance(otherLocation);

			if (!insect.equals(otherInsect) && distance > 0
					&& distance <= Ranges.detectionRange) {
				Point align = new Point((int) (100 * Math.cos(otherInsect
						.getTheta() * Math.PI / 180)),
						(int) (-100 * Math.sin(otherInsect.getTheta() * Math.PI
								/ 180)));
				align = normalisePoint(align, 100); // alignment weight is 100
				boolean tooClose = (distance < Ranges.separationRange);
				double weight = 200.0;
				if (tooClose) {
					weight *= Math.pow(1 - (double) distance
							/ Ranges.separationRange, 2);
				} else {
					weight *= -Math
							.pow((double) (distance - Ranges.separationRange)
									/ (Ranges.detectionRange - Ranges.separationRange),
									2);
				}
				Point attract = sumPoints(otherLocation.x, otherLocation.y,
						-1.0, insect.x, insect.y, 1.0);
				attract = normalisePoint(attract, weight); // weight is variable
				Point dist = sumPoints(align.x, align.y, 1.0, attract.x,
						attract.y, 1.0);
				dist = normalisePoint(dist, 100); // final weight is 100
				target = sumPoints(target.x, target.y, 1.0, dist.x, dist.y, 1.0);
			} else {
				Point dist = sumPoints(insect.getX(), insect.getY(), 1.0,
						otherLocation.x, otherLocation.y, -1.0);
				dist = normalisePoint(dist, 1000);
				double weight = Math.pow((1 - (double) distance
						/ Ranges.detectionRange), 2);
				target = sumPoints(target.x, target.y, 1.0, dist.x, dist.y,
						weight); // weight is
			}
			numInsects++;
		}

		if (numInsects == 0) {
			return insect.getTheta();
		} else { // average target points and add to position
			target = sumPoints(insect.getX(), insect.getY(), 1.0, target.x,
					target.y, 1 / (double) numInsects);
		}
		int targetTheta = (int) (180 / Math.PI * Math.atan2(insect.getY()
				- target.y, target.x - insect.getX()));
		return (targetTheta + 360) % 360; // angle for Insect to steer towards
	}

	public Point closestLocation(double px, double py, double ox, double oy) {
		double dX = Math.abs(ox - px);
		double dY = Math.abs(oy - py);
		double x = ox;
		double y = oy;
		if (Math.abs(Ranges.height - ox + px) < dX) {
			dX = Ranges.height - ox + px;
			x = ox - Ranges.height;
		}
		if (Math.abs(Ranges.height - px + ox) < dX) {
			dX = Ranges.height - px + ox;
			x = ox + Ranges.height;
		}

		if (Math.abs(Ranges.width - oy + py) < dY) {
			dY = Ranges.width - oy + py;
			y = oy - Ranges.width;
		}
		if (Math.abs(Ranges.width - py + oy) < dY) {
			dY = Ranges.width - py + oy;
			y = oy + Ranges.width;
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
		for (Insect2 insect : insects)
			insect.draw(g);
	}

	public void lancer() {
		for (Insect2 insect : insects)
			insect.start()	;	
	}
}
