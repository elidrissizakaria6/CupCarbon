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

package device;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import map.Layer;
import solver.SensorGraph;
import tracking.TrackingTasksManager;
import utilities.MapCalc;
import flying_object.FlyingGroup;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class DeviceList {

	private static List<Device> nodes = new ArrayList<Device>();
	private boolean drawLinks = true;
	private boolean linksDetection = true;
	private boolean displayConnectionDistance = false;
	private LinkedList<Point[]> linksCoord = new LinkedList<Point[]>();
	private static TrackingTasksManager trackingManager;
	//public static LinkedList<Integer> envelope = new LinkedList<Integer>();
	public static LinkedList<LinkedList<Integer>> envelopeList = new LinkedList<LinkedList<Integer>>();

	/**
	 * 
	 */
	public DeviceList() {
		// Thread th = new Thread(this);
		// th.start();
	}

	/**
	 * @return the nodes
	 */
	public static List<Device> getNodes() {
		return nodes;
	}
	
	/**
	 * @return a node by its id
	 */
	public static Device getNodeById(int id) {
		for(Device device : nodes) {
			if(device.getId() == id) return device;
		}
		return null;
	}
	
	/**
	 * @return a sensor node by its id
	 */
	public static SensorNode getSensorNodeById(int id) {		
		for(SensorNode snode : DeviceList.getSensorNodes()) {
			if(snode.getId() == id) return snode;
		}
		return null;
	}
	
	/**
	 * @return the sensor nodes
	 */
	public static List<SensorNode> getSensorNodes() {
		List<SensorNode> snodes = new ArrayList<SensorNode>();
		for(Device n : nodes) {
			if(n.getType() == Device.SENSOR)
				snodes.add((SensorNode) n);
		}
		return snodes;
	}
	
	/**
	 * @return the mobile nodes
	 */
	public static List<Device> getMobileNodes() {
		List<Device> snodes = new ArrayList<Device>();
		for(Device n : nodes) {
			if(n.getType() == Device.MOBILE)
				snodes.add(n);
		}
		return snodes;
	}

	/**
	 * @param fileName
	 */
	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			Device node;
			for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
				node = iterator.next();
				//System.out.println(node.getGPSFileName());
				fos.print(node.getType());
				fos.print(" " + node.getId());
				fos.print(" " + node.getUserId());
				fos.print(" " + node.getX());
				fos.print(" " + node.getY());
				fos.print(" " + node.getRadius());

				if (node.getType() == Device.SENSOR
						|| node.getType() == Device.BASE_STATION
						|| node.getType() == Device.BRIDGE
						|| node.getType() == Device.MOBILE_WR)
					fos.print(" " + node.getRadioRadius());

				if (node.getType() == Device.SENSOR)
					fos.print(" " + node.getCaptureUnitRadius());

				if (node.getType() == Device.FLYING_OBJECT)
					fos.print(" " + ((FlyingGroup) node).getInsectNumber());

				if (node.getType() == Device.SENSOR
						|| node.getType() == Device.FLYING_OBJECT
						|| node.getType() == Device.MOBILE
						|| node.getType() == Device.MOBILE_WR) {
					//System.out.println("----> " + node.getGPSFileName());
					fos.print(" "
							+ ((node.getGPSFileName() == "") ? "#" : node
									.getGPSFileName()));
				}

				if (node.getType() == Device.SENSOR) {
					System.out.println(node.getScriptFileName());
					fos.print(" "+ ((node.getScriptFileName() == "") ? "#" : node.getScriptFileName()));
				}

				fos.println();

			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param fileName
	 */
	public static void open(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str;
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				switch (str.length) {
				case 6:
					addNodeByType(str[0], str[1], str[2], str[3], str[4],
							str[5]);
					break;
				case 7:
					addNodeByType(str[0], str[1], str[2], str[3], str[4],
							str[5], str[6]);
					break;
				case 8:
					addNodeByType(str[0], str[1], str[2], str[3], str[4],
							str[5], str[6], str[7]);
					break;
				case 9:
					addNodeByType(str[0], str[1], str[2], str[3], str[4],
							str[5], str[6], str[7], str[8]);
					break;
				case 10:
					addNodeByType(str[0], str[1], str[2], str[3], str[4],
							str[5], str[6], str[7], str[8], str[9]);
					break;
				}
			}
			br.close();
			Layer.getMapViewer().repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the number of the nodes
	 */
	public static int size() {
		return nodes.size();
	}

	/**
	 * Create a node from a set of values (table type)
	 * 
	 * @param type
	 *            table that contains information about a node to add
	 */
	public static void addNodeByType(String... type) {
		switch (Integer.valueOf(type[0])) {
		case 1:
			add(new SensorNode(type[3], type[4], type[5], type[6], type[7],
					type[8], type[9]));
			break;
		case 2:
			add(new Gas(type[3], type[4], type[5]));
			break;
		case 3:
			add(new FlyingGroup(type[3], type[4], type[5], type[6], type[7]));
			break;
		case 4:
			add(new BaseStation(type[3], type[4], type[5], type[6]));
			break;
		case 5:
			add(new Router(type[3], type[4], type[5], type[6]));
			break;
		case 6:
			add(new Mobile(type[3], type[4], type[5], type[6]));
			break;
		case 7:
			add(new MobileWithRadio(type[3], type[4], type[5], type[6], type[7]));
			break;
		case 8:
			add(new Marker(type[3], type[4], type[5]));
			break;
		case 9:
			add(new StreetVertex(type[3], type[4], type[5], type[6]));
			break;
		}
	}

	/**
	 * @param node
	 */
	public static void add(Device node) {
		nodes.add(node);
	}

	// public void drawDistance(int x, int y, int x2, int y2, int d, Graphics g)
	// {
	// g.setColor(UColor.WHITED_TRANSPARENT);
	// g.drawString(""+d,(x2-x)/2,(y2-y)/2);
	// }

	/**
	 * Draw Links between
	 * 
	 * @param g
	 *            Graphics
	 */
	public void draw(Graphics g) {
		Device n1 = null;
		Device n2 = null;

		ListIterator<Device> iterator;
		ListIterator<Device> iterator2;
		try {
//			if (drawLinks || linksDetection) {
//				iterator = nodes.listIterator();
//				while (iterator.hasNext() && iterator.nextIndex() < size - 1) {
//					n1 = iterator.next();
//					iterator2 = nodes.listIterator(iterator.nextIndex());
//					while (iterator2.hasNext()) {
//						n2 = iterator2.next();
//						if (linksDetection) {
//							if (n1.detection(n2)) {
//								n1.setDetection(true);
//							}
//							if (n2.detection(n1)) {
//								n2.setDetection(true);
//							}
//						}
//					}
//				}
//			}

			for (Device n : nodes) {
				n.draw(g);
				n.setDetection(false);
				if (n.getType() == Device.SENSOR) {
					((SensorNode) n).drawMarked(g);
				}
			}
			
			if(trackingManager != null){
				trackingManager.draw(g);
			}
			
			if (drawLinks || linksDetection) {
				iterator = nodes.listIterator();
				while (iterator.hasNext() && iterator.nextIndex() < nodes.size() - 1) {
					n1 = iterator.next();
					if(!n1.isDead()) {
						iterator2 = nodes.listIterator(iterator.nextIndex());
						while (iterator2.hasNext()) {
							n2 = iterator2.next();
							if(!n2.isDead()) {
								if (n1.radioDetect(n2) && drawLinks) {
									n1.drawRadioLink(n2, g);
									if (displayConnectionDistance) {
										Layer.drawDistance(n1.getX(), n1.getY(),
												n2.getX(), n2.getY(),
												(int) n1.distance(n2), g);
									}
								}
								if (linksDetection) {
									if (n1.detection(n2)) {
										n1.setDetection(true);
										n1.drawDetectionLink(n2, g);
									}
									else
										n1.setDetection(false);
									if (n2.detection(n1)) {
										n2.setDetection(true);
										n2.drawDetectionLink(n1, g);
									}
									else
										n2.setDetection(false);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
		
		for (Device n : nodes) {
			if (n.getType() == Device.SENSOR) {
				((SensorNode) n).drawMarked(g);
			}
		}
	}

	/**
	 * @param g
	 */
	public void draw2(Graphics g) {
		// Device n1 = null;
		// Device n2 = null;

		// ListIterator<Device> iterator ;
		// ListIterator<Device> iterator2;
		/*
		 * if (drawLinks || linksDetection) { iterator = nodes.listIterator();
		 * while (iterator.hasNext() && iterator.nextIndex() < size - 1) { n1 =
		 * iterator.next(); iterator2 =
		 * nodes.listIterator(iterator.nextIndex()); while (iterator2.hasNext())
		 * { n2 = iterator2.next(); if(linksDetection) { if (n1.detection(n2)) {
		 * n1.setDetection(true); } if (n2.detection(n1)) {
		 * n2.setDetection(true); } } } } }
		 */

		for (Device n : nodes) {
			n.draw(g);
			n.setDetection(false);
			if (n.getType() == Device.SENSOR) {
				((SensorNode) n).drawMarked(g);
			}
		}

		g.setColor(Color.black);
		for (Point[] p : linksCoord) {
			g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
		}

		/*
		 * 
		 * if (drawLinks || linksDetection) { iterator = nodes.listIterator();
		 * while (iterator.hasNext() && iterator.nextIndex() < size - 1) { n1 =
		 * iterator.next(); iterator2 =
		 * nodes.listIterator(iterator.nextIndex()); while (iterator2.hasNext())
		 * { n2 = iterator2.next(); if (n1.radioDetect(n2) && drawLinks) {
		 * n1.drawRadioLink(n2, g); if(displayConnectionDistance) {
		 * drawDistance(
		 * n1.getX(),n1.getY(),n2.getX(),n2.getY(),(int)n1.distance(n2),g); } }
		 * if(linksDetection) { if (n1.detection(n2)) { n1.drawDetectionLink(n2,
		 * g); } if (n2.detection(n1)) { n2.drawDetectionLink(n1, g); } } } } }
		 */
	}

	// public Graph toGraph2() {
	// Node n1 = null ;
	// Node n2 = null ;
	// double distance = 0 ;
	// Graph graphe = new Graph() ;
	// for(int i=0; i<nodes.size(); i++) {
	// graphe.add(new Vertex(i,nodes.get(i).getNodeName()));
	// }
	// for (int i = 0; i < nodes.size() - 1; i++) {
	// n1 = nodes.get(i) ;
	// for (int j = i + 1; j < nodes.size(); j++) {
	// n2 = nodes.get(j) ;
	// if(n1.radioDetect(n2)) {
	// distance = n1.distance(n2);
	// graphe.get(i).ajouterVoisin(graphe.get(j), distance) ;
	// graphe.get(j).ajouterVoisin(graphe.get(i), distance) ;
	// }
	// }
	// }
	// return graphe ;
	// }

	public Device get(int idx) {
		return nodes.get(idx);
	}

	public void setLiens(boolean b) {
		drawLinks = b;
	}

	public boolean getLiens() {
		return drawLinks;
	}

	public void setLiensDetection(boolean b) {
		linksDetection = b;
	}

	public boolean getLiensDetection() {
		return linksDetection;
	}

	public void setDisplayDistance(boolean b) {
		displayConnectionDistance = b;
	}

	public boolean getDisplayDistance() {
		return displayConnectionDistance;
	}

	public static void delete(int idx) {
		Device node = nodes.get(idx);
		Layer.getMapViewer().removeMouseListener(node);
		Layer.getMapViewer().removeMouseMotionListener(node);
		Layer.getMapViewer().removeKeyListener(node);
		nodes.remove(idx);
		node = null;
	}

//	public void simulate() {
//		Device node;
//		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
//			node = iterator.next();
//			if (node.isSelected())
//				node.start();
//
//		}
//	}
//
//	public void simulateAll() {
//		//Device node;
//		for (Device node : nodes) {//Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
//			//node = iterator.next();
//			node.setSelection(true);
//			node.start();
//
//		}
//	}
//	
//	public void simulateSensors() {
//		for (Device node : nodes) {
//			if(node.getType()==Device.SENSOR) {
//				node.setSelection(true);
//				node.start();
//			}
//		}
//	}
	
	public void simulateMobiles() {
		for (Device node : nodes) {
			if(node.getType()==Device.MOBILE || node.getType()==Device.MOBILE_WR) {
				node.setSelection(true);
				node.start();
			}
		}
	}
	
//	public static void stopSimulation() {
//		for (Device node : nodes) {
//			node.setSelection(false);
//			node.stopSimulation();;
//		}
//	}

	public static StringBuilder displaySensorGraph() {
		return SensorGraph.toSensorGraph(nodes, nodes.size()).displayNames();
	}

	public static StringBuilder displaySensorTargetGraph() {
		return SensorGraph.toSensorTargetGraph(nodes, nodes.size()).displayNames();
	}

	public void selectInNodeSelection(int cadreX1, int cadreY1, int cadreX2,
			int cadreY2) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			node.setMove(false);
			node.setSelection(false);
			if (Layer.inMultipleSelection(node.getX(), node.getY(), cadreX1,
					cadreX2, cadreY1, cadreY2)) {
				node.setSelection(true);
			}
		}
	}

	public void deleteIfSelected() {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected() && node.getHide()==0) {
				Layer.getMapViewer().removeMouseListener(node);
				Layer.getMapViewer().removeMouseMotionListener(node);
				Layer.getMapViewer().removeKeyListener(node);
				iterator.remove();
				node = null;
			}
		}
	}

	public static void setGpsFileName(String gpsFileName) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setGPSFileName(gpsFileName);
			}
		}
	}

	public static void setScriptFileName(String scriptFileName) {
		Device node;
		System.out.println("Appel ..." + scriptFileName);
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setScriptFileName(scriptFileName);
			}
		}
	}

	public static void updateFromMap(String xS, String yS, String radiusS,
			String radioRadiusS, String captureRadiusS, String gpsFileName,
			String eMax, String eTx, String eRx, String eS, String beta, String targetName
			) {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected()) {
				node.setX(Double.valueOf(xS));
				node.setY(Double.valueOf(yS));
				node.setRadius(Double.valueOf(radiusS));
				node.setRadioRadius(Double.valueOf(radioRadiusS));
				node.setCaptureRadius(Double.valueOf(captureRadiusS));
				node.setGPSFileName(gpsFileName);
				node.seteMax(Double.valueOf(eMax));
				node.seteTx(Double.valueOf(eTx));
				node.seteRx(Double.valueOf(eRx));
				node.seteS(Double.valueOf(eS));
				node.setBeta(Double.valueOf(beta));
				node.setTrgetName(targetName);
				Layer.getMapViewer().repaint();
			}
		}
	}

	// public static void setCover1() {
	// Graph gr = toSensorGraph() ;
	// gr.display();
	// }

	// public Graph getSensorGraph() {
	// return toSensorGraph() ;
	// }

	public static void initAll() {
		//envelope = new LinkedList<Integer>();
		envelopeList = new LinkedList<LinkedList<Integer>>();
		for (Device device : nodes) {
			device.setMarked(false);
			device.setVisited(false);
			device.setDead(false);
		}
		Layer.getMapViewer().repaint();
	}
	
	public static void initAllAlgoSelectedNodes() {
		for (Device device : nodes) {
			device.setMarked(false);
			device.setVisited(false);
		}
		Layer.getMapViewer().repaint();
	}

	public static void initAlgoSelectedNodes() {
		for (Device device : nodes) {
			if (device.isSelected()) {
				device.setMarked(false);
				device.setVisited(false);
			}
		}
		Layer.getMapViewer().repaint();
	}

	public static void setAlgoSelect(boolean b) {
		for (Device node : nodes) {
			node.setMarked(false);
		}
		Layer.getMapViewer().repaint();
	}

	public void setSelectionOfAllNodes(boolean selection, int type,
			boolean addSelect) {
		for (Device dev : nodes) {
			if (!addSelect)
				dev.setSelection(false);
			if (dev.getType() == type || type == -1)
				dev.setSelection(selection);
		}
		Layer.getMapViewer().repaint();
	}

	public void invertSelection() {
		for (Device dev : nodes) {
			dev.invSelection();
		}
		Layer.getMapViewer().repaint();
	}

	// @Override
	public void run() {
		LinkedList<Point[]> tLinksCoord;
		while (true) {
			tLinksCoord = new LinkedList<Point[]>();
			// linksCoord = new LinkedList<Point []>();
			Device n1 = null;
			Device n2 = null;

			// ListIterator<Device> iterator ;
			// ListIterator<Device> iterator2;

			if (drawLinks || linksDetection) {
				// iterator = nodes.listIterator();
				// while (iterator.hasNext() && iterator.nextIndex() < size - 1)
				// {
				for (int i = 0; i < nodes.size() - 1; i++) {
					n1 = nodes.get(i);
					// n1 = iterator.next();
					// iterator2 = nodes.listIterator(iterator.nextIndex());
					// while (iterator2.hasNext()) {
					for (int j = i + 1; j < nodes.size(); j++) {
						n2 = nodes.get(j);
						// n2 = iterator2.next();
						if (n1.radioDetect(n2) && drawLinks) {

							tLinksCoord.add(getCouple(n1, n2));
							// if(displayConnectionDistance) {
							// drawDistance(n1.getX(),n1.getY(),n2.getX(),n2.getY(),(int)n1.distance(n2),g);
							// }
						}
					}
				}
			}
			try {
				linksCoord = tLinksCoord;
				Layer.getMapViewer().repaint();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Point[] getCouple(Device n1, Device n2) {
		int[] coord = MapCalc.geoToIntPixelMapXY(n1.getX(), n1.getY());
		int lx1 = coord[0];
		int ly1 = coord[1];
		coord = MapCalc.geoToIntPixelMapXY(n2.getX(), n2.getY());
		int lx2 = coord[0];
		int ly2 = coord[1];
		// int lx1 = MapCalc.geoToIntPixelMapX(n1.getX(),n1.getY()) ;
		// int ly1 = MapCalc.geoToIntPixelMapY(n1.getX(),n1.getY()) ;
		// int lx2 = MapCalc.geoToIntPixelMapX(n2.getX(),n2.getY()) ;
		// int ly2 = MapCalc.geoToIntPixelMapY(n2.getX(),n2.getY()) ;
		Point[] p = new Point[2];
		p[0] = new Point(lx1, ly1);
		p[1] = new Point(lx2, ly2);
		return p;
	}
	
	public void initId() {
		int k = 0;
		Device.initNumber() ;
		for(Device d : nodes) {
			d.setId(k++);
			Device.incNumber();
		}
		Layer.getMapViewer().repaint();
	}
	
	public void loadRoutesFromFiles() {
		for(Device d : nodes) {
			d.loadRouteFromFile();
		}
	}
	


	
	
	
	
	
	
	
	
	
	
	/**
	 * permet de trouver le device dont l'idName est egal a l'argument idName
	 * @param idName
	 * @return
	 */
	private static Device getDeviceByIdName(String idName){
		if(idName == null || idName.isEmpty()){
			return null;
		}
		for (Device node : nodes) {
			if(idName.equals(node.getNodeIdName())) {
				return node;
			}
		}
		return null;
	}
	
	private static void startTracking() {
		List<SensorNode> trackers = getTrackersList();
		if(!trackers.isEmpty()){
			trackingManager = new TrackingTasksManager();
			for(SensorNode tracker : trackers){
				Device target = getDeviceByIdName(tracker.getTargetName());
				if(target != null){
					trackingManager.addTask(tracker, target);
				}
			}
			trackingManager.startTraking();
		}
	}

	/**
	 * prepare les donnees du tracking
	 * bien que la boucle soit dupliquee, le code est ainsi plus propre
	 * @return
	 */
	private static List<SensorNode> getTrackersList() {
		List<SensorNode> trackers = new ArrayList<SensorNode>();
		for (Device node : nodes) {
			if(node.getType()==Device.SENSOR) {
				if(node.getTargetName()!=null && !node.getTargetName().isEmpty()){
					trackers.add((SensorNode) node);
				}
			}
		}
		return trackers;	
	}

	public void simulate() {
		Device node;
		for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
			node = iterator.next();
			if (node.isSelected())
				node.start();
		}
		startTracking();
	}
	
	public void simulateAll() {	
		for (Device node : nodes) {
			node.setSelection(true);
			node.start();
		}
		startTracking();
	}
	
	public void simulateSensors() {
		for (Device node : nodes) {
			if(node.getType()==Device.SENSOR) {
				node.setSelection(true);
				node.start();
			}
		}
		startTracking();
	}
	
	public static void stopSimulation() {
		for (Device node : nodes) {
			node.setSelection(false);
			node.stopSimulation();
		}
		if(trackingManager!=null){
			trackingManager.stopTracking();
		}
	}
	
	public static void startRealTrackingSimulation(String trackerId, String targetId) {
		trackingManager = new TrackingTasksManager(true);
		trackingManager.addTask(trackerId, targetId);
		trackingManager.startTraking();
	}	
	
	//------
	public static int getLastEnvelopeSize() {
		return envelopeList.getLast().size();
	}
	
	public static void initLastEnvelope() {
		envelopeList.getLast().clear();
	}
	
	public static void addEnvelope() {
		envelopeList.add(new LinkedList<Integer>());
	}
	
	public static void addToLastEnvelope(Integer d) {
		envelopeList.getLast().add(d);
	}
	
	public static LinkedList<Integer> getLastEnvelope() {
		return envelopeList.getLast();
	}
	
	public void drawEnvelope(LinkedList<Integer> envelope, Graphics2D g) {
		if(envelope.size()>0) {
			double x = nodes.get(envelope.get(0)).getX();
			double y = nodes.get(envelope.get(0)).getY();
			int lx1=0;
			int ly1=0;
			int lx2=0;
			int ly2=0;
			int[] coord ;
			for(int i=1; i<envelope.size(); i++) {
				coord = MapCalc.geoToIntPixelMapXY(x, y);
				lx1 = coord[0];
				ly1 = coord[1];
				coord = MapCalc.geoToIntPixelMapXY(nodes.get(envelope.get(i)).getX(), nodes.get(envelope.get(i)).getY());
				lx2 = coord[0];
				ly2 = coord[1];
				g.setColor(Color.BLUE);
				g.drawLine(lx1, ly1, lx2, ly2);
				x = nodes.get(envelope.get(i)).getX();
				y = nodes.get(envelope.get(i)).getY();		
			}
			coord = MapCalc.geoToIntPixelMapXY(nodes.get(envelope.get(0)).getX(), nodes.get(envelope.get(0)).getY());
			lx1 = coord[0];
			ly1 = coord[1];
			g.drawLine(lx2, ly2, lx1, ly1);
		}
	}
	
	public void drawEnvelopeList(Graphics2D g) {
		for(LinkedList<Integer> envelope : envelopeList) {
			drawEnvelope(envelope, g);
		}
	}
	
}
