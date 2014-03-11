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

package map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;

import project.Project;
import utilities.MapCalc;
import utilities.UColor;
import cupcarbon.CupCarbon;
import cupcarbon.DeviceParametersWindow;
import device.BaseStation;
import device.Device;
import device.DeviceList;
import device.Gas;
import device.Marker;
import device.MarkerList;
import device.Mobile;
import device.MobileWithRadio;
import device.NetworkLoader;
import device.Router;
import device.Sensor;
import device.StreetGraph;
import device.StreetVertex;
import flying_object.FlyingGroup;

public class Layer implements Painter<Object>, MouseListener,
		MouseMotionListener, KeyListener {

	public static JXMapViewer mapViewer = null;
	public static DeviceList nodeList = null;
	public static MarkerList markerList = null;
	public static StreetGraph streetGraph = null;
	public static boolean afficherIndicateur = false;
	public static double x = 0;
	public static double y = 0;
	public static char lastKey = 0;
	public static int lastKeyCode = 0;	
	public static boolean dessinerCadre = false;
	public static int cadreX1 = 0;
	public static int cadreY1 = 0;
	public static int cadreX2 = 0;
	public static int cadreY2 = 0;
	public static int selectType = 0;
	public static boolean altDown = false;
	public static boolean shiftDown = false;
	public static boolean cmdDown = false;
	public static boolean ctrlDown = false;
	public static boolean mousePressed = false;
	public static String projectPath = "";
	public static String projectName = "";
	
	private boolean debutSelection = false;

	public Layer() {
	}

	public Layer(JXMapViewer mapViewer) {
		Layer.mapViewer = mapViewer;
		nodeList = new DeviceList();
		markerList = new MarkerList();
		streetGraph = new StreetGraph();
		/*
		 * noeuds.add(new Sensor(47.71403518643726,-3.4284210205078125, 0,
		 * 1000)); noeuds.add(new
		 * BaseStation(47.705256511726034,-3.3805274963378906, 0, 500));
		 * noeuds.add(new Router(47.71726906227617,-3.3673095703125, 0, 1000));
		 * noeuds.add(new Sensor(47.709299506131345,-3.3626747131347656, 0,
		 * 1000)); noeuds.add(new Sensor(47.724313596879625,-3.3518600463867188,
		 * 0, 600)); noeuds.add(new Sensor(47.7263921299974,-3.3664512634277344,
		 * 0, 500)); noeuds.add(new
		 * Mobile(47.69127688626756,-3.3623313903808594, 100)); noeuds.add(new
		 * Gas(47.70075109139772,-3.420696258544922,40)); noeuds.add(new
		 * Router(47.715536653750945,-3.412628173828125, 0, 1000));
		 * noeuds.add(new Sensor(47.71345768748889,-3.395977020263672, 0,
		 * 1000)); noeuds.add(new
		 * SimpleInsect(47.693703371072026,-3.376750946044922,1000));
		 */

		mapViewer.setOverlayPainter(this);
		mapViewer.addMouseListener(this);
		mapViewer.addMouseMotionListener(this);
		mapViewer.addKeyListener(this);
		// insectinit();

	}	

	public boolean isDebutSelection() {
		return debutSelection;
	}

	public void setDebutSelection(boolean debutSelection) {
		this.debutSelection = debutSelection;
	}

	public static JXMapViewer getMapViewer() {
		return mapViewer;
	}

	@Override
	public void paint(Graphics2D g, Object arg1, int arg2, int arg3) {
		g.setFont(new Font("arial", 0, 12));
		Rectangle rect = mapViewer.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		if (afficherIndicateur) {
			g.setColor(UColor.ROUGE);
			if (lastKey == '1') {
				g.drawString("   Sensor", (float) x, (float) y);
			}
			if (lastKey == '2') {
				g.drawString("   Gas", (float) x, (float) y);
			}
			if (lastKey == '3') {
				g.drawString("   Insects", (float) x, (float) y);
			}
			if (lastKey == '4') {
				g.drawString("   Router", (float) x, (float) y);
			}
			if (lastKey == '5') {
				g.drawString("   Base Station", (float) x, (float) y);
			}
			if (lastKey == '6') {
				g.drawString("   Mobile", (float) x, (float) y);
			}
			if (lastKey == '7') {
				g.drawString("   Mobile With Radio", (float) x, (float) y);
			}
			if (lastKey == '8') {
				g.drawString("   Marker", (float) x, (float) y);
			}
			if (lastKey == '9') {
				g.drawString("   Vertex", (float) x, (float) y);
			}
			g.drawOval((int) (x - 8), (int) (y - 8), 16, 16);

			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, 75, 30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, 165, 30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, -75, -30);
			//
			// g.fillArc((int) (x - 10), (int) (y - 10), 20, 20, -15, 30);
		}

		markerList.draw(g);
		streetGraph.dessiner(g);
		nodeList.draw(g);

		if (dessinerCadre) {
			Point2D p1 = MapCalc.pixelPanelToPixelMap(cadreX1, cadreY1);
			Point2D p2 = MapCalc.pixelPanelToPixelMap(cadreX2, cadreY2);
			g.setColor(UColor.WHITE_DTRANSPARENT);
			g.fillRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
			g.setColor(UColor.WHITE_TRANSPARENT);
			g.drawRect((int) p1.getX(), (int) p1.getY(),
					(int) (p2.getX() - p1.getX()),
					(int) (p2.getY() - p1.getY()));
		}
		
		// OSM Test BEGIN
		// This part is used just to understand how to add shapes on the OSM map
		// The class OsmTest is required
		//OsmTest osm = new OsmTest() ;
		//osm.drawFromGPS(g);
		// OSM Test END
		
		g.dispose();
	}	

	public static DeviceList getDeviceList() {
		return nodeList;
	}

	public void simulate() {
		nodeList.simulate();
	}

	public void simulateAll() {
		nodeList.simulateAll();
	}

	@Override
	public void mouseClicked(MouseEvent arg) {
		if (afficherIndicateur) {
			Point p = new Point(arg.getX(), arg.getY());
			GeoPosition gp = mapViewer.convertPointToGeoPosition(p);
			if (lastKey == '1') {
				DeviceList.add(new Sensor(gp.getLatitude(), gp.getLongitude(),
						0, 100, 20));
				mapViewer.repaint();
			}
			if (lastKey == '2') {
				DeviceList
						.add(new Gas(gp.getLatitude(), gp.getLongitude(), 10));
				mapViewer.repaint();
			}
			if (lastKey == '3') {
				DeviceList.add(new FlyingGroup(gp.getLatitude(), gp.getLongitude(),
						500, 200));
				mapViewer.repaint();
			}
			if (lastKey == '4') {
				DeviceList.add(new Router(gp.getLatitude(), gp.getLongitude(),
						0, 100));
				mapViewer.repaint();
			}
			if (lastKey == '5') {
				DeviceList.add(new BaseStation(gp.getLatitude(), gp
						.getLongitude(), 0, 500));
				mapViewer.repaint();
			}
			if (lastKey == '6') {
				DeviceList.add(new Mobile(gp.getLatitude(), gp.getLongitude(),
						10));
				mapViewer.repaint();
			}
			if (lastKey == '7') {
				DeviceList.add(new MobileWithRadio(gp.getLatitude(), gp
						.getLongitude(), 10, 100));
				mapViewer.repaint();
			}
			if (lastKey == '8') {
				MarkerList.add(new Marker(gp.getLatitude(), gp.getLongitude(),
						20));
				mapViewer.repaint();
			}
			if (lastKey == '9') {
				StreetGraph.add(new StreetVertex(gp.getLatitude(), gp
						.getLongitude(), 20, false));
				mapViewer.repaint();
			}
		}
		CupCarbon.updateInfos();
	}

	// public static void addNode(Device node) {
	// DeviceList.add(node);
	// mapViewer.repaint();
	// }

	// public static void addMarker(Marker marker) {
	// MarkerList.add(marker);
	// mapViewer.repaint();
	// }

	public static void addMarker(int index, Marker marker) {
		markerList.add(index, marker);
		mapViewer.repaint();
	}

	// public static void addStreetVertex(StreetVertex streetVertex) {
	// StreetGraph.add(streetVertex);
	// mapViewer.repaint();
	// }

	public static void addStreetVertex(int index, StreetVertex streetVertex) {
		streetGraph.add(index, streetVertex);
		mapViewer.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg) {
		mousePressed = true;
		if (lastKeyCode == 16) {
			mapViewer.setPanEnabled(false);
			debutSelection = true;
			cadreX1 = arg.getX();
			cadreY1 = arg.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg) {
		if (shiftDown && mousePressed) {
			mousePressed = false;
			lastKeyCode = 0;
			mapViewer.setPanEnabled(true);
			debutSelection = false;
			dessinerCadre = false;

			nodeList.selectInNodeSelection(cadreX1, cadreX2, cadreY1, cadreY2);
			markerList
					.selectInNodeSelection(cadreX1, cadreX2, cadreY1, cadreY2);
			streetGraph.selectInNodeSelection(cadreX1, cadreX2, cadreY1,
					cadreY2);

			mapViewer.repaint();
		}
	}

	public static boolean inMultipleSelection(double x, double y, int cadreX1,
			int cadreY1, int cadreX2, int cadreY2) {
		GeoPosition gp1 = Layer.getMapViewer().convertPointToGeoPosition(
				new Point(cadreX1, cadreY1));
		GeoPosition gp2 = Layer.getMapViewer().convertPointToGeoPosition(
				new Point(cadreX2, cadreY2));
		return x < gp1.getLatitude() && y > gp1.getLongitude()
				&& x > gp2.getLatitude() && y < gp2.getLongitude();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (key.isShiftDown())
			shiftDown = true;
		if (key.isAltDown())
			altDown = true;
		lastKeyCode = key.getKeyCode();
		if (lastKeyCode == 27) {
			lastKey = 0;
			afficherIndicateur = false;
			streetGraph.init();
			mapViewer.repaint();
		}
		if (key.isControlDown())
			ctrlDown = true;
		if (key.isMetaDown())
			cmdDown = true;
	}

	@Override
	public void keyReleased(KeyEvent key) {
		altDown = false;
		shiftDown = false;
		ctrlDown = false;
		cmdDown = false;
	}

	@Override
	public void keyTyped(KeyEvent key) {
		lastKey = key.getKeyChar();
		afficherIndicateur = false;
		if (lastKey == '1' || lastKey == '2' || lastKey == '3'
				|| lastKey == '4' || lastKey == '5' || lastKey == '6'
				|| lastKey == '7' || lastKey == '8' || lastKey == '9') {
			afficherIndicateur = true;
			mapViewer.repaint();
		}

		if (lastKey == 'v') {
			nodeList.setLiens(!nodeList.getLiens());
			markerList.setLinks(!markerList.getLinks());
		}
		
		if (lastKey == 'A') {
			markerList.setArrows(!markerList.getArrows());
		}

		if (lastKey == 'b') {
			nodeList.setLiensDetection(!nodeList.getLiensDetection());
		}

		if (lastKey == 'w') {
			if (selectType++ == 9)
				selectType = 0;
		}

		if (lastKey == 's' && (ctrlDown || cmdDown)) {
			Project.saveProject();
		}

		if (lastKey == 'x') {
			nodeList.setDisplayDistance(!nodeList.getDisplayDistance());
		}

		if (lastKey == 't' || lastKey == 'o') {
			int k = 0;
			StreetVertex sv1 = null;
			StreetVertex sv2 = null;
			for (StreetVertex sv : streetGraph.getGraph()) {
				if (sv.isSelected()) {
					if (k == 0)
						sv1 = sv;
					if (k == 1)
						sv2 = sv;
					k++;
				}
			}
			if (k == 2) {
				if (sv1.hasNeighbor(sv2)) {
					sv1.remove(sv2);
				} else {
					sv1.add(sv2);
				}
			}
		}
		
		if (lastKey == 'y' || lastKey == 'o') {
			int k = 0;
			StreetVertex sv1 = null;
			StreetVertex sv2 = null;
			for (StreetVertex sv : streetGraph.getGraph()) {
				if (sv.isSelected()) {
					if (k == 0)
						sv1 = sv;
					if (k == 1)
						sv2 = sv;
					k++;
				}
			}
			if (k == 2) {
				if (sv2.hasNeighbor(sv1)) {
					sv2.remove(sv1);
				} else {
					sv2.add(sv1);
				}
			}
		}
		
		if (lastKey == '>') {
			Device.moveSpeed += 5;
			CupCarbon.updateInfos();
		}
		
		if (lastKey == '<') {
			Device.moveSpeed -= 5;
			if(Device.moveSpeed<0)
				Device.moveSpeed = 0;
			CupCarbon.updateInfos();
		}

		if (lastKeyCode == 8) {
			nodeList.deleteIfSelected();
			markerList.deleteIfSelected();
			streetGraph.deleteIfSelected();
			CupCarbon.updateInfos();
			mapViewer.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg) {
		if (lastKeyCode == 16) {
			cadreX2 = arg.getX();
			cadreY2 = arg.getY();
			dessinerCadre = true;
			mapViewer.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Point2D pd = MapCalc.pixelPanelToPixelMap(me.getX(), me.getY());
		x = pd.getX();
		y = pd.getY();
		if (afficherIndicateur) {
			mapViewer.repaint();
		}
	}

	public void addNodeInMap(char c) {
		lastKey = c;
		afficherIndicateur = true;
		mapViewer.repaint();
	}

	public static void sensorParametersInit() {
		DeviceParametersWindow.textField_5.setText("");
		DeviceParametersWindow.textField_6.setText("");
		DeviceParametersWindow.textField_7.setText("");
		DeviceParametersWindow.textField_8.setText("");
		DeviceParametersWindow.textField_9.setText("");
	}

	public void loadCityNodes() {
		NetworkLoader nl = new NetworkLoader(mapViewer);
		nl.start();
	}

	public void loadCityNodes2() {
		try {
			double x = 0;
			double y = 0;
			// FileInputStream fis = new FileInputStream("santander.txt");
			FileInputStream fis = new FileInputStream("dubai.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String s;
			String[] ps;
			String[] ics;
			// for (int i = 0; i < 1000; i++) {
			// s = br.readLine();
			int k = 0;
			while (((s = br.readLine()) != null) && (k++ < 72)) {
				String[][] info = new String[7][2];
				info[0][0] = "Sensor Type : ";
				info[1][0] = "";
				info[2][0] = "";
				info[3][0] = "Battery : ";
				info[4][0] = "Name : ";
				info[5][0] = "Date : ";
				info[6][0] = "Time : ";
				info[0][1] = "";
				info[1][1] = "";
				info[2][1] = "";
				info[3][1] = "";
				info[4][1] = "";
				info[5][1] = "";
				info[6][1] = "";
				s = s.substring(1, s.length() - 1);
				ps = s.split(",");
				for (String cs : ps) {
					if (cs.endsWith("}"))
						cs = cs.substring(0, cs.length() - 1);
					if (cs.startsWith("\"values\":{\""))
						cs = cs.substring(10);
					ics = cs.split(":");
					ics[0] = ics[0].substring(1, ics[0].length() - 1);
					ics[1] = ics[1].substring(1, ics[1].length() - 1);
					if (ics[0].equals("Lattitude"))
						x = Double.parseDouble(ics[1]);
					if (ics[0].equals("Longitude"))
						y = Double.parseDouble(ics[1]);
					if (ics[0].equals("Sensor_Type"))
						info[0][1] = ics[1];
					if (ics[0].equals("Temperature")) {
						info[1][0] = "Temperature : ";
						info[1][1] = ics[1] + " °C";
					}

					if (ics[0].equals("Co_Index")) {
						info[2][0] = "CO Index : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Light")) {
						info[2][0] = "Light : ";
						info[2][1] = ics[1] + " lux";
					}
					if (ics[0].equals("Noise")) {
						info[2][0] = "Noise : ";
						info[2][1] = ics[1] + " dB";
					}
					if (ics[0].equals("State")) {
						info[2][0] = "State : ";
						info[2][1] = ics[1];
					}
					if (ics[0].equals("Battery"))
						info[3][1] = ics[1] + " %";
					if (ics[0].equals("Node"))
						info[4][1] = ics[1];
					if (ics[0].equals("Date"))
						info[5][1] = ics[1];
					if (ics[0].equals("Time"))
						info[6][1] = ics[1];
				}
				DeviceList.add(new Sensor(x, y, 0, 30, 10, info));
				// MarkerList.add(new Marker(x,y,10));
				mapViewer.repaint();
			}
			br.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void loadNodes() {
		String fileName = "nodes.dat";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String[] stab = br.readLine().split(" ");
			int sensorNumber = Integer.parseInt(stab[0]);
			int targetNumber = Integer.parseInt(stab[1]);
			double captuRadius = Double.parseDouble(stab[2]);
			double radioRadius = Double.parseDouble(stab[3]);
			double v1;
			double v2;
			double x;
			double y;

			/*
			 * Point2D p1 = MapCalc.pixelPanelToPixelMap(0, 0); x1 =
			 * Layer.getMapViewer().convertPointToGeoPosition(p1).getLatitude();
			 * y1 =
			 * Layer.getMapViewer().convertPointToGeoPosition(p1).getLongitude
			 * (); Point2D p2 = MapCalc.pixelPanelToPixelMap(0, 50); x2 =
			 * Layer.getMapViewer().convertPointToGeoPosition(p2).getLatitude();
			 * y2 =
			 * Layer.getMapViewer().convertPointToGeoPosition(p2).getLongitude
			 * (); double d1 = x1-x2; System.out.println("d1 = "+d1); double d2
			 * = y1-y2; System.out.println("d2 = "+d2);
			 */
			// double distUnit = 0.004723650625642506 ; //50 pixels
			Point p;

			for (int i = 0; i < sensorNumber; i++) {
				stab = br.readLine().split(" ");
				v1 = Double.parseDouble(stab[0]);
				// System.out.print(v1+" ");
				v2 = Double.parseDouble(stab[1]);
				// System.out.println(v2);
				p = new Point((int) v1, (int) v2);
				x = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLatitude();
				y = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLongitude();
				DeviceList.add(new Sensor(x, y, 0, radioRadius, captuRadius));

			}
			// System.out.println("-------------");
			for (int i = 0; i < targetNumber; i++) {
				stab = br.readLine().split(" ");
				v1 = Double.parseDouble(stab[0]);
				// System.out.print(v1+" ");
				v2 = Double.parseDouble(stab[1]);
				// System.out.println(v2);
				p = new Point((int) v1, (int) v2);
				x = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLatitude();
				y = Layer.getMapViewer().convertPointToGeoPosition(p)
						.getLongitude();
				DeviceList.add(new MobileWithRadio(x, y, 10, 0));
			}
			// System.out.println("-------------");
			stab = br.readLine().split(" ");
			v1 = Double.parseDouble(stab[0]);
			// System.out.print(v1+" ");
			v2 = Double.parseDouble(stab[1]);
			// System.out.println(v2);
			p = new Point((int) v1, (int) v2);
			x = Layer.getMapViewer().convertPointToGeoPosition(p).getLatitude();
			y = Layer.getMapViewer().convertPointToGeoPosition(p)
					.getLongitude();
			DeviceList.add(new BaseStation(x, y, 0, 150));
			// System.out.println("-------------");

			mapViewer.repaint();

			// System.out.println(sensorNumber);
			// System.out.println(targetNumber);
			// System.out.println(radius);
			// System.out.println(radioRadius);
			br.close();
			// System.out.println("-------------");
			// Thread th = new Thread(this);
			// th.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSelectionOfAllNodes(boolean selection, int type,
			boolean addSelection) {
		nodeList.setSelectionOfAllNodes(selection, type, addSelection);
	}

	public void invertSelection() {
		nodeList.invertSelection();
		markerList.invertSelection();
		streetGraph.invertSelection();
	}

	public void setSelectionOfAllMarkers(boolean selection, int type,
			boolean addSelection) {
		markerList.setSelectionOfAllMarkers(selection, type, addSelection);
	}

	public void setSelectionOfAllStreetVertices(boolean selection, int type,
			boolean addSelection) {
		streetGraph.setSelectionOfAllVertices(selection, type, addSelection);
	}

	public static void drawDistance(double x, double y, double x2, double y2, int d, Graphics g) {
		int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToIntPixelMapXY(x2, y2);
		int lx2 = coord[0];
		int ly2 = coord[1];
//		int lx1 = MapCalc.geoToIntPixelMapX(x, y);
//		int ly1 = MapCalc.geoToIntPixelMapY(x, y);
//		int lx2 = MapCalc.geoToIntPixelMapX(x2, y2);
//		int ly2 = MapCalc.geoToIntPixelMapY(x2, y2);
		g.setColor(Color.DARK_GRAY);
		g.drawString("" + d, ((lx1 + lx2) / 2), ((ly1 + ly2) / 2));
		// g.drawLine(lx1,ly1,lx2,ly2);
	}

}
