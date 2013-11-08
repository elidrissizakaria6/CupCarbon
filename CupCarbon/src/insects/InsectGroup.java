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

public class InsectGroup {
	private Vector<InsectSingle> insects;

	public InsectGroup() {
		insects = new Vector<InsectSingle>();
	}

	public InsectGroup(int n) {
		insects = new Vector<InsectSingle>();
		for (int i = 0; i < n; i++) {
			insects.add(new InsectSingle());
		}
	}

	int v;

	synchronized public void move() {
		// boolean b = true;
		// Random r = new Random();
		// int k=0;
		for (InsectSingle insect : insects) {
			// if (b) {
			// if(v++<10) {
			// insect.move((int)(r.nextGaussian()*100));
			// }
			// else
			// v=0;
			// if(k++>290)
			// b = false;
			// } else
			insect.move(generalHeading(insect));
		}
	}

	private int generalHeading(InsectSingle insect) {
		Point target = new Point(0, 0);
		int numInsects = 0;
		for (int i = 0; i < insects.size(); i++) {
			InsectSingle otherInsect = (InsectSingle) insects.elementAt(i);
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
		for (InsectSingle insect : insects)
			insect.draw(g);
	}
}
