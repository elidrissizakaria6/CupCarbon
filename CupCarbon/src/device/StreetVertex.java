/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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
import java.util.LinkedList;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;

public class StreetVertex extends Device {

	private static String idFL = "P"; // ID First Letter

	private LinkedList<StreetVertex> neighbors = new LinkedList<StreetVertex>();
	private StreetVertex cNeighbor;
	//private int kNeighbor = 0;
	private boolean busStation = false;

	public StreetVertex(double x, double y, double radius, boolean busStation) {
		super(x, y, radius);
		this.busStation = busStation;
	}

	public StreetVertex(String x, String y, String radius, String busStation) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius));
		this.busStation = Boolean.valueOf(busStation);
	}

	@Override
	public void draw(Graphics g) {
		double alpha;
		double dx;
		double dy;
		int[] coord;
		if (visible) {
			initDraw(g);
			coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
			//int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
			int rayon = MapCalc.radiusInPixels(this.radius);
			int x2, y2;			

			if (cNeighbor != null) {
				coord = MapCalc.geoToIntPixelMapXY(cNeighbor.getX(), cNeighbor.getY());
				x2 = coord[0];
				y2 = coord[1];
				//x2 = MapCalc.geoToIntPixelMapX(cNeighbor.getX(), cNeighbor.getY());
				//y2 = MapCalc.geoToIntPixelMapY(cNeighbor.getX(), cNeighbor.getY());
				g.setColor(UColor.ORANGE);
				g.drawLine(x, y, x2, y2);
				Layer.drawDistance(this.x, this.y, cNeighbor.getX(),
						cNeighbor.getY(), (int) this.distance(cNeighbor), g);
			}

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
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
						(rayon + 4) * 2);
			}
			
			drawMoveArrows(x, y, g);

			if (hide == 0) {
				if (busStation) {
					g.setColor(Color.ORANGE);
					g.fillOval(x - 10, y - 10, 20, 20);
					//g.setColor(Color.BLACK);
					//g.drawRect(x - 6, y - 6, 12, 12);
				}
				g.setColor(Color.BLUE);
				g.fillOval(x - 3, y - 3, 6, 6);
				g.drawOval(x - 10, y - 10, 20, 20);
			}
			
			for (StreetVertex sv : neighbors) {		
				coord = MapCalc.geoToIntPixelMapXY(sv.getX(), sv.getY());
				x2 = coord[0];
				y2 = coord[1];				
				//x2 = MapCalc.geoToIntPixelMapX(sv.getX(), sv.getY());
				//y2 = MapCalc.geoToIntPixelMapY(sv.getX(), sv.getY());
				g.setColor(Color.DARK_GRAY);
				g.drawLine(x, y, x2, y2);

				// Draw arrows
				dx = x2 - x;
				dy = y2 - y;
				alpha = Math.atan(dy / dx);
				alpha = 180 * alpha / Math.PI;
				if ((dx >= 0 && dy >= 0) || (dx >= 0 && dy <= 0))
					g.fillArc((int) x2 - 15, (int) y2 - 15, 30, 30,
							180 - (int) alpha - 10, 20);
				else
					g.fillArc((int) x2 - 15, (int) y2 - 15, 30, 30,
							-(int) alpha - 10, 20);

				Layer.drawDistance(this.x, this.y, sv.getX(), sv.getY(),
						(int) this.distance(sv), g);
			}
			
			// g.setColor(UColor.WHITE_TRANSPARENT);
			// g.drawOval(x-3, y-3, 6, 6);
			drawId(x, y, g);
		}
	}

	public static StreetVertex getCentre(StreetVertex streetVertex1,
			StreetVertex streetVertex2, boolean b) {
		// b = true : the created node will be selected
		double x1 = streetVertex1.getX();
		double y1 = streetVertex1.getY();
		double x2 = streetVertex2.getX();
		double y2 = streetVertex2.getY();
		double x = x1 + ((x2 - x1) / 2.0);
		double y = y1 + ((y2 - y1) / 2.0);
		StreetVertex marker = new StreetVertex(x, y, 25, false);
		if (b)
			marker.setSelection(true);
		return marker;
	}

	@Override
	public void run() {
		selected = false;
	}

	@Override
	public int getType() {
		return Device.VERTEX;
	}

	@Override
	public String getIdFL() {
		return idFL;
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	public void init() {
		cNeighbor = null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);
		if (selected) {
			// if (e.getKeyChar() == 'u') {
			// while (!(StreetGraph.get(kNeighbor).isVisible()) ||
			// (StreetGraph.get(kNeighbor) == this)) {
			// kNeighbor++;
			// if (kNeighbor >= StreetGraph.size())
			// kNeighbor = 0;
			// }
			// cNeighbor = StreetGraph.get(kNeighbor);
			// kNeighbor++;
			// if (kNeighbor >= StreetGraph.size())
			// kNeighbor = 0;
			// }
			// if ((e.getKeyChar() == 't')) {
			// if(neighbors != null || cNeighbor != null)
			// neighbors.add(cNeighbor);
			// if(cNeighbor != null ) cNeighbor.add(this);
			// }
			// if ((e.getKeyChar() == 'y')) {
			// if(neighbors != null || cNeighbor != null)
			// neighbors.remove(cNeighbor);
			// if(cNeighbor != null ) cNeighbor.remove(this);
			// }
			if ((e.getKeyChar() == 'p')) {
				busStation = !busStation;
			}
		}
	}

	public void add(StreetVertex streetVertex) {
		neighbors.add(streetVertex);
	}

	public void remove(StreetVertex streetVertex) {
		neighbors.remove(streetVertex);
	}

	public void removeAllNeighbors() {
		neighbors = null;
		cNeighbor = null;
	}

	public void removeAll() {
		for (StreetVertex streetVertex : neighbors) {
			streetVertex.remove(this);
		}
		removeAllNeighbors();
	}

	@Override
	public void setRadioRadius(double radiuRadius) {
	}

	@Override
	public void setCaptureRadius(double captureRadius) {
	}

	@Override
	public void setGPSFileName(String gpsFileName) {
	}

	@Override
	public void setCOMFileName(String comFileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getGPSFileName() {
		return "";
	}

	public LinkedList<StreetVertex> getNeighbors() {
		return neighbors;
	}

	public boolean hasNeighbor(StreetVertex sv) {
		for (StreetVertex v : neighbors) {
			if (v == sv)
				return true;
		}
		return false;
	}

	public boolean isBusStation() {
		return busStation;
	}

}