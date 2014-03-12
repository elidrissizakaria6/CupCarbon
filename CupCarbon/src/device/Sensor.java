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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;
import battery.Battery;
import captureunit.CaptureUnit;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Sensor extends DeviceWithRadio {

	protected CaptureUnit captureUnit;
	protected boolean insectDetection = false;
	private LinkedList<Long> routeTime;
	private LinkedList<Double> routeX;
	private LinkedList<Double> routeY;
	private boolean loop = false;
	private int routeIndex = 0;
	private boolean readyForSimulation = false;

	/**
	 * Constructor 1 Instanciate the capture unit Instanciate the battery
	 */
	public Sensor() {
		super();
		captureUnit = new CaptureUnit(this.x, this.y, this);
		battery = new Battery(captureUnit);
		withRadio = true;
		withSensor = true;
	}

	/**
	 * Constructor 2
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 */
	public Sensor(double x, double y, double radius, double radioRadius) {
		super(x, y, radius, radioRadius);
		captureUnit = new CaptureUnit(this.x, this.y, this);
		battery = new Battery(captureUnit);
		withRadio = true;
		withSensor = true;
	}

	/**
	 * Constructor 3
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the capture unit (default value = 10 meters)
	 */
	public Sensor(double x, double y, double radius, double radioRadius,
			double cuRadius) {
		super(x, y, radius, radioRadius);
		captureUnit = new CaptureUnit(this.x, this.y, cuRadius, this);
		battery = new Battery(captureUnit);
		withRadio = true;
		withSensor = true;
	}

	/**
	 * Constructor 4
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the capture unit (default value = 10 meters)
	 * @param sb
	 *            A two dimensional table that contains a set of informations
	 *            about the sensor (temperature, co2, etc.) The first column
	 *            contains the name of the parameter The second column contains
	 *            the value of the corresponding parameter
	 */
	public Sensor(double x, double y, double radius, double radioRadius,
			double cuRadius, String[][] sb) {
		this(x, y, radius, radioRadius, cuRadius);
		this.setInfos(sb);
	}

	/**
	 * Constructor 5 the same as the Constructor 3 with "String" argument
	 * instead of "double"
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the capture unit (default value = 10 meters)
	 */
	public Sensor(String x, String y, String radius, String radioRadius,
			String cuRadius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius),
				Double.valueOf(radioRadius));
		captureUnit = new CaptureUnit(this.x, this.y, Double.valueOf(cuRadius),
				this);
		battery = new Battery(captureUnit);
		withRadio = true;
		withSensor = true;
	}

	/**
	 * Constructor 6
	 * 
	 * @param x
	 *            Latitude
	 * @param y
	 *            Longitude
	 * @param radius
	 *            Radius of the sensor (default value = 0 meters)
	 * @param radioRadius
	 *            Radius (range) of the radio (in meter)
	 * @param cuRadius
	 *            Radius of the capture unit (default value = 10 meters)
	 * @param gpsFileName
	 *            The path of the GPS file
	 * @param scriptFileName
	 *            The path of the script file
	 */
	public Sensor(String x, String y, String radius, String radioRadius,
			String cuRadius, String gpsFileName, String scriptFileName) {
		this(x, y, radius, radioRadius, cuRadius);
		gpsFileName = (gpsFileName.equals("#") ? "" : gpsFileName);
		scriptFileName = (scriptFileName.equals("#") ? "" : scriptFileName);
		setGPSFileName(gpsFileName);
		setScriptFileName(scriptFileName);
	}

	@Override
	public String getGPSFileName() {
		return gpsFileName;
	}

	@Override
	public void setCaptureRadius(double captureRadio) {
		captureUnit.setRadius(captureRadio);
	}

	@Override
	public void draw(Graphics g) {
		if (visible) {
			initDraw(g);
			// Layer.getMapViewer().setAlpha(100);
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			// int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
			// int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
			int rayon = MapCalc.radiusInPixels(getRadioRadius());
			int rayon2 = MapCalc.radiusInPixels(this.radius);

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
			g.setColor(Color.DARK_GRAY);
			switch (hide) {
			case 0: {
				if (inside) {
					g.setColor(UColor.MAUVEF_TRANSPARENT);
				} else
					g.setColor(UColor.MAUVE_TRANSPARENT);
				g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
				captureUnit.setXY(x, y);
				captureUnit.draw(g, 0, detection);

			}
			case 1:
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
				captureUnit.setXY(x, y);
				captureUnit.draw(g, 1, detection);
			}

			if (selected) {
				g.setColor(UColor.NOIR_TTRANSPARENT);
				g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
						(rayon + 4) * 2);
			}

			if (insectDetection) {
				g.setColor(UColor.ROUGE_TRANSPARENT);
				g.fillOval(x - rayon * 2, y - rayon * 2, rayon * 4, rayon * 4);
			}

			drawIncRedDimNode(x, y, g);
			dessinAugDimRadio(x, y, g);
			drawRadius(x, y, rayon, g);
			drawRadioRadius(x, y, rayon2, g);

			if (underSimulation) {
				g.setColor(UColor.VERT);
				g.fillOval(x - 3, y - 3, 6, 6);
			} else {
				g.setColor(UColor.ROUGE);
				g.fillOval(x - 3, y - 3, 6, 6);
			}

			if (displayDetails) {
				g.setColor(Color.gray);
				g.drawString("" + battery.getCapacity(), x + 2 + rayon / 2, y
						+ 2 + rayon / 2);
			}

			drawMoveArrows(x, y, g);
			drawId(x, y, g);
			drawInfos(this, g);
		}
	}

	@Override
	public double getCaptureUnitRadius() {
		return captureUnit.getRadius();
	}

	// ------------------------------------------------------------------------
	// Load Route from file to Lists
	// ------------------------------------------------------------------------
	public void loadRouteFromFile() {
		routeIndex = 0;
		routeTime = new LinkedList<Long>();
		routeX = new LinkedList<Double>();
		routeY = new LinkedList<Double>();
		FileInputStream fis;
		BufferedReader b = null;
		String s;
		String[] ts;
		try {
			if (!gpsFileName.equals("")) {
				readyForSimulation = true;
				fis = new FileInputStream(gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				underSimulation = true;
				b.readLine();
				b.readLine();
				b.readLine();
				loop = Boolean.parseBoolean(b.readLine());
				while ((s = b.readLine()) != null) {
					ts = s.split(" ");
					routeTime.add(Long.parseLong(ts[0]));
					routeX.add(Double.parseDouble(ts[1]));
					routeY.add(Double.parseDouble(ts[2]));
				}
				b.close();
				fis.close();
			} else
				readyForSimulation = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preprocessing() {
		routeIndex = 0;
	}

	// ------------------------------------------------------------------------
	// Simulate
	// ------------------------------------------------------------------------
	public void simulate() {
		loadRouteFromFile();
		xori = x;
		yori = y;
		if (readyForSimulation) {
			underSimulation = true;
			routeIndex = 0;
			selected = false;
			long tmpTime = 0;
			long cTime = 0;
			long toWait = 0;
			do {
				cTime = routeTime.get(routeIndex);
				toWait = cTime - tmpTime;
				tmpTime = cTime;
				if (toWait < 0) {
					toWait = cTime;
				}
				x = routeX.get(routeIndex);
				y = routeY.get(routeIndex);
				Layer.getMapViewer().repaint();
				try {
					Thread.sleep(toWait * Device.moveSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				goToNext();
			} while (hasNext());
			routeIndex = 0;
			selected = false;
			x = xori;
			y = yori;
			thread = null;
			underSimulation = false;
			Layer.getMapViewer().repaint();
		}
	}
	// ------------------------------------------------------------------------
	// Run
	// ------------------------------------------------------------------------
	@Override
	public void run() {
		simulate();
	}

	// ------------------------------------------------------------------------
	// Duration to Next Time 
	// ------------------------------------------------------------------------
	@Override
	public int getNextTime() {
		if (routeTime.size() > 0) {
			int diff = 0;
			if (routeIndex == 0)
				diff = (int) (1 * routeTime.get(routeIndex));
			else
				diff = (int) (routeTime.get(routeIndex) - routeTime
						.get(routeIndex - 1));
			return ((diff * 100) * Device.frequency / 1000);
		}
		return 0;
	}

	// ------------------------------------------------------------------------
	// Go to next point and update 
	// ------------------------------------------------------------------------
	@Override
	public void exeNext(boolean visual, int visualDelay) {
		if (routeTime != null) {
			routeIndex++;
			if ((routeIndex == (routeTime.size()))) {
				if (!loop) {
					routeIndex--;
				} else
					routeIndex = 0;
			}
			x = routeX.get(routeIndex);
			y = routeY.get(routeIndex);
		}
		if (visual) {
			try {
				Layer.getMapViewer().repaint();
				Thread.sleep(visualDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ------------------------------------------------------------------------
	// Go to next index (routeIndex)
	// ------------------------------------------------------------------------
	//@Override
	public void goToNext() {
		if (routeTime != null) {
			routeIndex++;
			if (routeIndex == routeTime.size()) {
				if (loop) {
					routeIndex = 0;
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Test the existence of a next ponit
	// ------------------------------------------------------------------------
	public boolean hasNext() {
		if (routeIndex < routeTime.size())
			return true;
		return false;
	}

	// ------------------------------------------------------------------------
	// 
	// ------------------------------------------------------------------------
	@Override
	public int getType() {
		return Device.SENSOR;
	}

	@Override
	public String getIdFL() {
		return "S";
	}

	/**
	 * Set the capture unit
	 * 
	 * @param captureUnit
	 */
	public void setCaptureUnit(CaptureUnit captureUnit) {
		this.captureUnit = captureUnit;
	}

	/**
	 * Set the battery
	 * 
	 * @param battery
	 */
	public void setBattery(Battery battery) {
		this.battery = battery;
	}

	@Override
	public Sensor clone() throws CloneNotSupportedException {
		Sensor newSensor = (Sensor) super.clone();
		CaptureUnit newCaptureUnit = (CaptureUnit) captureUnit.clone();
		Battery newBattery = (Battery) battery.clone();
		newSensor.setCaptureUnit(newCaptureUnit);
		newCaptureUnit.setNode(newSensor);
		newSensor.setBattery(newBattery);
		return newSensor;
	}

	@Override
	public String getNodeIdName() {
		return getIdFL() + id;
	}

	@Override
	public void setGPSFileName(String gpsFileName) {
		this.gpsFileName = gpsFileName;
	}

	public boolean canMove() {
		if (getGPSFileName().equals(""))
			return false;
		return true;
	}
}