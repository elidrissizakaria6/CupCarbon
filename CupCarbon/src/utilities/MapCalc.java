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

package utilities;

import java.awt.Point;
import java.awt.geom.Point2D;

import map.Layer;

import org.jdesktop.swingx.mapviewer.GeoPosition;


public class MapCalc {

	public static Point2D geoXYToPixelMap(double x, double y) {
		return Layer
				.getMapViewer()
				.getTileFactory()
				.geoToPixel(new GeoPosition(x, y),
						Layer.getMapViewer().getZoom());
	}

	public static GeoPosition geoXYToPixel(int x, int y) {
		return Layer.getMapViewer().getTileFactory()
				.pixelToGeo(new Point(x, y), Layer.getMapViewer().getZoom());
	}

	public static Point2D geoToPixelMap(GeoPosition gp) {
		return Layer.getMapViewer().getTileFactory()
				.geoToPixel(gp, Layer.getMapViewer().getZoom());
	}

	public static Point2D pixelPanelToPixelMap(int x, int y) {
		return Layer.getMapViewer().getTileFactory().geoToPixel(
						Layer.getMapViewer().convertPointToGeoPosition(
								new Point(x, y)),
						Layer.getMapViewer().getZoom());
	}

	public static int geoToIntPixelMapX(double x, double y) {
		return (int) geoXYToPixelMap(x, y).getX();
	}

	public static int geoToIntPixelMapY(double x, double y) {
		return (int) geoXYToPixelMap(x, y).getY();
	}
	
	public static int [] geoToIntPixelMapXY(double x, double y) {
		Point2D p = geoXYToPixelMap(x, y);
		int v [] = {(int) p.getX(), (int) p.getY()};
		return v;
	}
	
	public static double distanceEnPixels(double x1, double y1, double x2, double y2) {
		Point2D p1 = geoXYToPixelMap(x1, y1);
		Point2D p2 = geoXYToPixelMap(x2, y2);
		return p1.distance(p2);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double earth_radius = 6378137;   // Terre = sphère de 6378km de rayon
		double rla1 = Math.toRadians(x1) ;
		double rlo1 = Math.toRadians(y1) ;
		double rla2 = Math.toRadians(x2) ;
		double rlo2 = Math.toRadians(y2) ;
		
		double dla = (rla2-rla1)/2.0;
		double dlo = (rlo2-rlo1)/2.0;
		
		double a = (Math.sin(dla) * Math.sin(dla)) + Math.cos(rla1) * Math.cos(rla2) * (Math.sin(dlo) * Math.sin(dlo)); 
		double d = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (earth_radius * d);
	}
	
	public static int rayonEnPixel(double rayon) {
		GeoPosition p1 = new GeoPosition(36.000000, 40.000000);
		Point2D pt1 = Layer.getMapViewer().getTileFactory().geoToPixel(p1, Layer.getMapViewer().getZoom());
		int v1 = (int) pt1.getX();
		GeoPosition p2 = new GeoPosition(36.000000, 40.0011125);
		Point2D pt2 = Layer.getMapViewer().getTileFactory().geoToPixel(p2, Layer.getMapViewer().getZoom());
		int v2 = (int) pt2.getX();
		int rp = (int) (rayon * (v2 - v1) / 100);
		return rp;
	}
}
