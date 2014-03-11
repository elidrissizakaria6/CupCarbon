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
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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
public class Sensor2 extends DeviceWithRadio {

	protected CaptureUnit captureUnit;
	protected boolean insectDetection = false;

	/**
	 * Constructor 1 Instanciate the capture unit Instanciate the battery
	 */
	public Sensor2() {
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
	public Sensor2(double x, double y, double radius, double radioRadius) {
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
	public Sensor2(double x, double y, double radius, double radioRadius,
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
	public Sensor2(double x, double y, double radius, double radioRadius,
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
	public Sensor2(String x, String y, String radius, String radioRadius,
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
	public Sensor2(String x, String y, String radius, String radioRadius,
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
				// g.setColor(UColor.VERT);
				// System.out.println(battery.getCapacityInPercent());
				// g.fillRect(x + rayon + 10, y - battery.getCapacityInPercent()
				// + rayon,
				// 5, battery.getCapacityInPercent());
				// g.setColor(Color.gray);
				// g.drawRect(x + rayon + 10, y - battery.getCapacityInPercent()
				// + rayon,
				// 5, battery.getCapacityInPercent() * 100);
				// g.drawLine(x + rayon, y + rayon, x + rayon + 25, y + rayon);
				// g.drawString("" + battery.getCapacity(), x + 15
				// + rayon, y + 20 + rayon);
				g.setColor(Color.gray);
				g.drawString("" + battery.getCapacity(), x + 2 + rayon/2, y + 2
						+ rayon/2);
				// g.setColor(Color.MAGENTA);
				// g.drawString("(" + battery.getCapacity() + ")", (int) (x +
				// 10),
				// (int) (y + 20));
			}

			// for(int i=0; i<Layer.getDeviceList().size(); i++) {
			// if(Layer.getDeviceList().get(i).getType()==Device.INSECT) {
			// Insects insects = (Insects) Layer.getDeviceList().get(i) ;
			// for(int j=0; j<insects.size(); j++) {
			// SingleInsect insect = insects.getIthInsect(j);
			// if(detection(insect)) {
			// insect.setDetected(true);
			// insectDetection = true ;
			// this.drawDetectionLink(insect, g);
			// Layer.getMapViewer().repaint();
			// }
			// else {
			// insect.setDetected(false);
			// insectDetection = false ;
			// Layer.getMapViewer().repaint();
			// }
			// }
			// }
			// }

			drawMoveArrows(x, y, g);
			drawId(x, y, g);
			drawInfos(this, g);
			// dessinerSelectAlgo(x,y,rayon,g);
		}
	}

	@Override
	public double getCaptureUnitRadius() {
		return captureUnit.getRadius();
	}

	// @Override
	public void run2() {
		state = this.getState();
		radioRangeRadius = radioRangeRadiusOri;
		battery.init();
		selected = false;
		underSimulation = true;
		double err = 0;
		double rayonOri = radioRangeRadius;
		// ------ Mobile -----
		double totalDistance = 0;
		selected = false;
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x2, y2;
		try {
			if (!gpsFileName.equals("")) {
				fis = new FileInputStream(gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				underSimulation = true;
				String desc_str = b.readLine();
				String from_str = b.readLine();
				String to_str = b.readLine();
				System.out.println("Description : " + desc_str);
				System.out.println("From : " + from_str);
				System.out.println("To : " + to_str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ------ END Mobile ----
		long tmpTime = -3600000;
		long cTime = 0;
		long toWait = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		LinkedList<String> sb = new LinkedList<String>();
		try {
			if (b != null) {
				if (((s = b.readLine()) != null)) {
					sb.add(s);
				}
				b.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!battery.empty()) {
			try {
				for (int i = 0; i < sb.size(); i++) {
					x2 = x;
					y2 = y;
					ts = sb.get(i).split(" ");
					cTime = simpleDateFormat.parse(ts[0]).getTime();
					toWait = cTime - tmpTime;
					tmpTime = cTime;
					x = Double.parseDouble(ts[1]);
					y = Double.parseDouble(ts[2]);
					if (firstTime)
						firstTime = false;
					else {
						// System.out.println((int) MapCalc.distance(x, y,
						// x2,
						// y2));
						totalDistance += MapCalc.distance(x, y, x2, y2);
					}
					// Layer.getMapViewer().repaint();
				}
				try {
					Thread.sleep(toWait / 10);
					battery.consume();
					err = this.radioRangeRadius
							* (porteeErr * random.nextGaussian()) / 1000000.;
					radioRangeRadius = (rayonOri + err)
							- (rayonOri
									* (100 - battery.getCapacityInPercent()) / 10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Layer.getMapViewer().repaint();
				// battery.consommer();
				// err = this.radioRadius * (porteeErr * random.nextGaussian())/
				// 100.;
				// radioRadius = (rayonOri + err) - (rayonOri * (100 -
				// battery.getCapacite()) / 100);
				// Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(totalDistance);
		// Layer.getMapViewer().repaint();
		underSimulation = false;
		thread = null;
		try {
			if (b != null)
				b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		state = this.getState();
		radioRangeRadius = radioRangeRadiusOri;
		battery.init();
		selected = false;
		underSimulation = true;
		double err = 0;
		double rayonOri = radioRangeRadius;
		// ------ Mobile -----
		double totalDistance = 0;
		selected = false;
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x2, y2;
		try {
			if (!gpsFileName.equals("")) {
				fis = new FileInputStream(gpsFileName);
				b = new BufferedReader(new InputStreamReader(fis));
				underSimulation = true;
				String desc_str = b.readLine();
				String from_str = b.readLine();
				String to_str = b.readLine();
				System.out.println("Description : " + desc_str);
				System.out.println("From : " + from_str);
				System.out.println("To : " + to_str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ------ END Mobile ----
		long tmpTime = -3600000;
		long cTime = 0;
		long toWait = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		double xori = x;
		double yori = y;
		while (!battery.empty()) {
			try {
				if (b != null) {
					if (((s = b.readLine()) != null)) {
						x2 = x;
						y2 = y;
						ts = s.split(" ");
						cTime = simpleDateFormat.parse(ts[0]).getTime();
						toWait = cTime - tmpTime;
						tmpTime = cTime;
						x = Double.parseDouble(ts[1]);
						y = Double.parseDouble(ts[2]);
						if (firstTime)
							firstTime = false;
						else {
							// System.out.println((int) MapCalc.distance(x, y,
							// x2,
							// y2));
							totalDistance += MapCalc.distance(x, y, x2, y2);
						}
						// Layer.getMapViewer().repaint();
					} else {
						// System.out.println("-----------------");
						// System.out.println(totalDistance);
						// JOptionPane.showMessageDialog(new JFrame(), "" +
						// totalDistance);
						// Layer.getMapViewer().repaint();

						break;

						// break; // Comment this line if you want to simulate
						// the
						// sensor until the battery is empty
					}
				}
				try {
					Thread.sleep(toWait / 10);
					battery.consume();
					err = this.radioRangeRadius * (porteeErr * random.nextGaussian()) / 1000000.;
					radioRangeRadius = (rayonOri + err) - (rayonOri * (100 - battery.getCapacityInPercent()) / 10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Layer.getMapViewer().repaint();
				// battery.consommer();
				// err = this.radioRadius * (porteeErr * random.nextGaussian())/
				// 100.;
				// radioRadius = (rayonOri + err) - (rayonOri * (100 -
				// battery.getCapacite()) / 100);
				// Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		x = xori ;
		y = yori ;
		System.out.println(totalDistance);
		// Layer.getMapViewer().repaint();
		underSimulation = false;
		thread = null;
		try {
			if (b != null)
				b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getType() {
		return Device.SENSOR;
	}

	@Override
	public String getIdFL() {
		return "S";
	}

	/*
	 * public void dessinerSelectAlgo(int x, int y, int r, Graphics g) {
	 * if(selectedByAlgo) { g.setColor(Color.RED); if(hide==2) {
	 * g.setColor(Color.gray); g.drawOval(x-r-3, y-r-3, (r+3)*2, (r+3)*2);
	 * g.drawOval(x-r-4, y-r-4, (r+4)*2, (r+4)*2);
	 * g.setColor(UColor.VERTF_TRANSPARENT); g.fillOval(x-r-3, y-r-3, (r+3)*2,
	 * (r+3)*2); } g.setColor(Color.DARK_GRAY); g.drawOval(x-6, y-6, 12, 12);
	 * g.drawOval(x-7, y-7, 14, 14); } }
	 */

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
	public Sensor2 clone() throws CloneNotSupportedException {
		Sensor2 newSensor = (Sensor2) super.clone();
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

//	public double getBatteryLevel() {
//		return battery.getCapacity();
//	}
	
	@Override
	public int getNextTime() { return 0 ;}
	
	@Override
	public void loadRouteFromFile() {}
	
	@Override
	public void exeNext(boolean visual, int visualDelay) {}
	
	@Override
	public boolean canMove() {return false;}

}