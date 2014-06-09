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
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import map.Layer;
import tracking.TargetWS;
import tracking.TrackerWS;
import utilities.MapCalc;

/**
 * 
 * @author Khaoula - Chabha
 * 
 * KH
 *
 */
public class TrackingPointsList {

	private static List<Marker> points;
	private boolean drawLinks = true;
	private boolean drawArrows = true;

	private static boolean underSimulation = false;

	public TrackingPointsList() {
		points = new ArrayList<Marker>();
	}

	public static void addNodeByType(String... type) {
		add(new Marker(type[0], type[1], type[2]));
	}

	public static void add(Marker marker) {
		points.add(marker);
	}

	public void add(int index, Marker marker) {
		points.add(index, marker);
	}

	/**
	 * Draw the markers (in red)
	 * 
	 * @param g
	 *            Graphical object
	 */
	public void draw(Graphics g) {
		if (points!=null && !points.isEmpty() && underSimulation) {
			try {
//				recalculateRoute();
				double x1 = 0;
				double y1 = 0;
				double x2 = 0;
				double y2 = 0;
				double dx = 0;
				double dy = 0;
				double alpha = 0;
				int lx1 = 0;
				int ly1 = 0;
				int lx2 = 0;
				int ly2 = 0;
				int[] coord;
				for (Marker marker : points)
					marker.draw(g);
				if (drawLinks && points.size() > 0) {
					boolean firstTime = true;
					for (Marker marker : points) {
						if (firstTime) {
							firstTime = false;
							x1 = marker.getX();
							y1 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x1, y1);
							lx1 = coord[0];
							ly1 = coord[1];
							// lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
							// ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
							g.setColor(Color.blue);
							g.drawOval((int) lx1 - 5, (int) ly1 - 5, (int) 10,
									(int) 10);
						} else {
							x2 = marker.getX();
							y2 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x2, y2);
							lx2 = coord[0];
							ly2 = coord[1];
							// lx2 = MapCalc.geoToIntPixelMapX(x2, y2);
							// ly2 = MapCalc.geoToIntPixelMapY(x2, y2);

							g.setColor(Color.blue);

							// Draw the link between markers
							g.drawLine((int) lx1, (int) ly1, (int) lx2,
									(int) ly2);
							// Draw arrows
							if (drawArrows) {
								dx = lx2 - lx1;
								dy = ly2 - ly1;
								alpha = Math.atan(dy / dx);
								alpha = 180 * alpha / Math.PI;
								if ((dx >= 0 && dy >= 0)
										|| (dx >= 0 && dy <= 0))
									g.fillArc((int) lx2 - 15, (int) ly2 - 15,
											30, 30, 180 - (int) alpha - 10, 20);
								else
									g.fillArc((int) lx2 - 15, (int) ly2 - 15,
											30, 30, -(int) alpha - 10, 20);
							}
							x1 = marker.getX();
							y1 = marker.getY();
							coord = MapCalc.geoToIntPixelMapXY(x1, y1);
							lx1 = coord[0];
							ly1 = coord[1];
							// lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
							// ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static Marker get(int idx) {
		return points.get(idx);
	}

	public static int getIndex(Marker marker) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i) == marker)
				return i;
		}
		return -1;
	}

	public void setLinks(boolean b) {
		drawLinks = b;
	}

	public boolean getLinks() {
		return drawLinks;
	}

	public void setArrows(boolean b) {
		drawArrows = b;
	}

	public boolean getArrows() {
		return drawArrows;
	}

	public static int size() {
		return points.size();
	}

	public static void simulate() {
		
		underSimulation = true;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(underSimulation){
					
					try {
						Thread.sleep(Device.moveSpeed/2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					recalculateRoute();
				}
			}
		});
		
		thread.start();
	}

	@SuppressWarnings("deprecation")
	public static void stopSimulation() {
		underSimulation = false;
		if(thread!=null){
			thread.stop();
		}
		
	}

	public void selectInNodeSelection(int cadreX1, int cadreY1, int cadreX2,
			int cadreY2) {
		Marker marker;
		for (Iterator<Marker> iterator = points.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			marker.setMove(false);
			marker.setSelection(false);
			if (Layer.inMultipleSelection(marker.getX(), marker.getY(),
					cadreX1, cadreX2, cadreY1, cadreY2)) {
				marker.setSelection(true);
			}
		}
	}

	public void deleteIfSelected() {
		Marker marker;
		for (Iterator<Marker> iterator = points.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			if (marker.isSelected()) {
				Layer.getMapViewer().removeMouseListener(marker);
				Layer.getMapViewer().removeMouseMotionListener(marker);
				Layer.getMapViewer().removeKeyListener(marker);
				iterator.remove();
				marker = null;
			}
		}
	}

	public static void deleteAll() {
		Marker marker;
		for (Iterator<Marker> iterator = points.iterator(); iterator.hasNext();) {
			marker = iterator.next();
			Layer.getMapViewer().removeMouseListener(marker);
			Layer.getMapViewer().removeMouseMotionListener(marker);
			Layer.getMapViewer().removeKeyListener(marker);
			iterator.remove();
			marker = null;
		}
	}

	//private static TrackingServiceClient receiver = new TrackingServiceClient();
	private static Thread thread;

	public static void recalculateRoute() {
//		for(Device device : DeviceList.getNodes() ){
//			if(device.getType() == Device.SENSOR) {
//				device.getTarget()
//			}
//		}
		// Récupérer les données du trackeur
		Device dev1 = DeviceList.getNodes().get(0);
		Device dev2 = DeviceList.getNodes().get(1);
		TrackerWS.clean();
		TrackerWS.create(dev1.getNodeIdName(), 0, 0);
		TargetWS.create(dev2.getNodeIdName(), 0, 0);
		Point point1 = TrackerWS.getCoords(dev1.getNodeIdName());// receiver.getLastPoint(1);
		Point point2 = TargetWS.getCoords(dev2.getNodeIdName());//receiver.getLastPoint(2);
		if (point1 != null && point2 != null) {
			String host = "http://router.project-osrm.org/viaroute?hl=fr&loc="
					+ point1.getX() + "," + point1.getY() + "&loc="
					+ point2.getX() + "," + point2.getY() + "&output=gpx";
			try {
				URL racine = new URL(host);
				URLConnection uc = racine.openConnection();
				InputStream in = uc.getInputStream();
				BufferedReader b = new BufferedReader(new InputStreamReader(in));
				final String[] s1 = b.readLine().split("lat=\"");
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						updatePoints(s1);
						
					}
				});
				
				b.close();
			} catch (MalformedURLException e) {
				System.err.println(host + " : URL non comprise.");
			} catch (IOException e) {
				System.err.println("------ Connexion problem ! ------");
			}
		}
	}

	private static void updatePoints(String[] s1) {
		String[] s2;
		deleteAll();
		for (int i = 1; i < s1.length; i++) {
			s2 = s1[i].split("\"");
			points.add(new Marker(Double.valueOf(s2[0]), Double
					.valueOf(s2[2]), 25));
		}
	}
	

}
