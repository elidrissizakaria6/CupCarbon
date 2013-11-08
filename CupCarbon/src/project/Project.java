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

package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import map.Layer;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.DeviceList;
import device.MarkerList;
import device.StreetGraph;

public final class Project {

	public static String projectPath = "";
	public static String projectName = "";

	public static void setProjectName(String path, String name) {
		projectPath = path;
		projectName = name;
	}

	public static String getProjectPathName() {
		return projectPath + "/" + projectName;
	}

	public static String getProjectNodePathName() {
		return projectPath + "/config/nodes.cfg";
	}

	public static String getProjectMarkerPathName() {
		return projectPath + "/config/markers.cfg";
	}

	public static String getProjectStreetVertexPathName() {
		return projectPath + "/config/graph.cfg";
	}

	public static String getProjectGpsPath() {
		return projectPath + "/gps";
	}

	public static String getProjectComPath() {
		return projectPath + "/pcom";
	}

	public static String getProjectLogPath() {
		return projectPath + "/logs";
	}

	public static String getProjectResultsPath() {
		return projectPath + "/results";
	}

	public static void saveProject() {
		if (projectPath.equals("") || projectName.equals(""))
			JOptionPane.showMessageDialog(null,
					"Project must be created or must be open.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		else {
			saveParameters();
			DeviceList.save(getProjectNodePathName());
			MarkerList.save(getProjectMarkerPathName());
			StreetGraph.save(getProjectStreetVertexPathName());
			JOptionPane.showMessageDialog(null, "Project saved !");
		}
	}

	public static void openProject(String path, String name) {
		setProjectName(path, name);
		loadParameters();
		DeviceList.open(getProjectNodePathName());
		MarkerList.open(getProjectMarkerPathName());
		StreetGraph.open(getProjectStreetVertexPathName());
	}

	public static void newProject(String path, String name) {
		setProjectName(path, name + ".cup");
		File file = new File(path);
		file.mkdir();
		file = new File(path + "/gps");
		file.mkdir();
		file = new File(path + "/tmp");
		file.mkdir();
		file = new File(path + "/config");
		file.mkdir();
		file = new File(path + "/omnet");
		file.mkdir();
		file = new File(path + "/pcom");
		file.mkdir();
		file = new File(path + "/logs");
		file.mkdir();
		file = new File(path + "/results");
		file.mkdir();
		saveParameters();
	}

	public static void loadParameters() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					getProjectPathName()));
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			int zoom = Integer.valueOf(br.readLine().split(" ")[1]);
			Layer.getMapViewer().setZoom(zoom);
			double la = Double.valueOf(br.readLine().split(" ")[1]);
			double lo = Double.valueOf(br.readLine().split(" ")[1]);
			Layer.getMapViewer().setCenterPosition(new GeoPosition(la, lo));
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveParameters() {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(
					getProjectPathName()));
			fos.println("CupCarbon v. 1.0");
			fos.println("----------------");
			fos.println("Name "
					+ projectName.substring(0, projectName.length() - 4));
			fos.println("zoom " + Layer.getMapViewer().getZoom());
			fos.println("centerposition_la "
					+ Layer.getMapViewer().getCenterPosition().getLatitude());
			fos.println("centerposition_lo "
					+ Layer.getMapViewer().getCenterPosition().getLongitude());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getGpsFileFromName(String name) {
		if (name.endsWith(".gps"))
			return getProjectGpsPath() + "/" + name;
		else
			return getProjectGpsPath() + "/" + name + ".gps";
	}

	public static String getGpsFileExtension(String name) {
		if (name.endsWith(".gps"))
			return name;
		else
			return name + ".gps";
	}

	public static String getComFileFromName(String name) {
		if (name.endsWith(".com"))
			return getProjectComPath() + "/" + name;
		else
			return getProjectComPath() + "/" + name + ".com";
	}

	public static String getComFileExtension(String name) {
		if (name.endsWith(".com"))
			return name;
		else
			return name + ".com";
	}

	public static String getLogFileFromName(String name) {
		if (name.endsWith(".log"))
			return getProjectLogPath() + "/" + name;
		else
			return getProjectLogPath() + "/" + name + ".log";
	}

	public static String getLogFileExtension(String name) {
		if (name.endsWith(".log"))
			return name;
		else
			return name + ".log";
	}

	public static String getResultFileFromName(String name) {
		if (name.endsWith(".res"))
			return getProjectResultsPath() + "/" + name;
		else
			return getProjectResultsPath() + "/" + name + ".res";
	}

	public static String getResultFileExtension(String name) {
		if (name.endsWith(".res"))
			return name;
		else
			return name + ".res";
	}
}
