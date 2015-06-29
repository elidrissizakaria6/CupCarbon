package graph;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;

import map.Layer;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;
import device.DeviceList;
import device.SensorNode;

public class GraphFile extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFileChooser chooser;
	 String choosertitle;
	public void run() {
		
	}
	public static void open(String pathfileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					pathfileName));
//			System.out.println(br.readLine());
//			System.out.println(br.readLine());
//			System.out.println(br.readLine());
			int zoom = Integer.valueOf(br.readLine().split(" ")[1]);
			Layer.getMapViewer().setZoom(zoom);
			double la = Double.valueOf(br.readLine().split(" ")[1]);
			double lo = Double.valueOf(br.readLine().split(" ")[1]);
			Layer.getMapViewer().setCenterPosition(new GeoPosition(la, lo));
			String line;
			String[] str;
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				double x=Double.valueOf(str[2]);
				double y=Double.valueOf(str[3]);
				GeoPosition gp1=Layer.mapViewer.convertPointToGeoPosition(new Point2D.Double(x,y));
				DeviceList.add(new SensorNode(gp1.getLatitude(),gp1.getLongitude(),0,Double.valueOf(str[4]),Double.valueOf(str[5])));
				
			}
			
			br.close();
			Layer.getMapViewer().repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void save(String pathfileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(pathfileName));
			//Save parameters
			fos.println("zoom " + Layer.getMapViewer().getZoom());
			fos.println("centerposition_la "
					+ Layer.getMapViewer().getCenterPosition().getLatitude());
			fos.println("centerposition_lo "
					+ Layer.getMapViewer().getCenterPosition().getLongitude());
			
			List<Device> nodes=DeviceList.getNodes();
			Device node;
			DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("#.##############",symbols);
			df.setRoundingMode(RoundingMode.UP);
			for (Iterator<Device> iterator = nodes.iterator(); iterator.hasNext();) {
				node = iterator.next();
				//System.out.println(node.getGPSFileName());
//				fos.print(node.getType());
				Point2D.Double p=(java.awt.geom.Point2D.Double) Layer.mapViewer.convertGeoPositionToPoint(new GeoPosition(node.getX(), node.getY()));
				fos.print(node.getId());
				fos.print(" " + node.getUserId());
				fos.print(" " + p.getX());
				fos.print(" " + p.getY());
//				fos.print(" " + node.getRadius());

//				if (node.getType() == Device.SENSOR
//						|| node.getType() == Device.BASE_STATION
//						|| node.getType() == Device.BRIDGE
//						|| node.getType() == Device.MOBILE_WR)
					fos.print(" " + df.format(node.getRadioRadius()));

//				if (node.getType() == Device.SENSOR)
					fos.print(" " + node.getCaptureUnitRadius());

//				if (node.getType() == Device.FLYING_OBJECT)
//					fos.print(" " + ((FlyingGroup) node).getInsectNumber());

//				if (node.getType() == Device.SENSOR
//						|| node.getType() == Device.FLYING_OBJECT
//						|| node.getType() == Device.MOBILE
//						|| node.getType() == Device.MOBILE_WR) {
//					//System.out.println("----> " + node.getGPSFileName());
//					fos.print(" "
//							+ ((node.getGPSFileName() == "") ? "#" : node
//									.getGPSFileName()));
//				}
//
//				if (node.getType() == Device.SENSOR) {
//					System.out.println(node.getScriptFileName());
//					fos.print(" "+ ((node.getScriptFileName() == "") ? "#" : node.getScriptFileName()));
//				}

				fos.println();

			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
