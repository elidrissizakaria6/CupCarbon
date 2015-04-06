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

import sensorunit.SensorUnit;
import utilities.MapCalc;
import utilities.UColor;
import battery.Battery;

/**
 * @author Ahcene Bounceur
 * @author Lounis Massinissa
 * @version 1.0
 */

public class SensorNode extends DeviceWithRadio {

	protected SensorUnit sensorUnit;
	protected boolean flyingObjectDetection = false;
	protected int type = Device.SENSOR;
	protected boolean detecting = false ;

	/**
	 * Constructor 1 Instanciate the capture unit 
	 * Instanciate the battery
	 */
	public SensorNode() {
		super();
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery(sensorUnit);
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
	public SensorNode(double x, double y, double radius, double radioRadius) {
		super(x, y, radius, radioRadius);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, this);
		battery = new Battery(sensorUnit);
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
	public SensorNode(double x, double y, double radius, double radioRadius,
			double cuRadius) {
		super(x, y, radius, radioRadius);
		sensorUnit = new SensorUnit(this.longitude, this.latitude, cuRadius, this);
		battery = new Battery(sensorUnit);
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
	public SensorNode(double x, double y, double radius, double radioRadius,
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
	public SensorNode(String x, String y, String radius, String radioRadius,
			String cuRadius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius),
				Double.valueOf(radioRadius));
		sensorUnit = new SensorUnit(this.longitude, this.latitude, Double.valueOf(cuRadius),
				this);
		battery = new Battery(sensorUnit);
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
	public SensorNode(String x, String y, String radius, String radioRadius,
			String cuRadius, String gpsFileName, String scriptFileName) {
		this(x, y, radius, radioRadius, cuRadius);
		gpsFileName = (gpsFileName.equals("#") ? "" : gpsFileName);
		scriptFileName = (scriptFileName.equals("#") ? "" : scriptFileName);
		setGPSFileName(gpsFileName);
		setScriptFileName(scriptFileName);
	}
	
//	@Override
//	public String getGPSFileName() {
//		return gpsFileName;
//	}

	@Override
	public void setCaptureRadius(double captureRadio) {
		sensorUnit.setRadius(captureRadio);
	}

	@Override
	public void draw(Graphics g) {
		if (visible) {
			initDraw(g);
			// Layer.getMapViewer().setAlpha(100);
			int[] coord = MapCalc.geoToIntPixelMapXY(longitude, latitude);
			int x = coord[0];
			int y = coord[1];
			// int x = MapCalc.geoToIntPixelMapX(this.x, this.y);
			// int y = MapCalc.geoToIntPixelMapY(this.x, this.y);
			int rayon = MapCalc.radiusInPixels(getRadioRadius()) ; 
			//rayon = (int)((rayon * ((getBatteryLevel() / 100000000.)*100.))/100.) ;
			int rayon2 = MapCalc.radiusInPixels(this.radius);
			int capRadius = MapCalc.radiusInPixels(sensorUnit.getRadius());

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
			if(hide == 0 || hide==4) {	
				if (inside) {
					g.setColor(UColor.MAUVEF_TRANSPARENT);
				} else {
					if(channel == 0) {
						g.setColor(UColor.MAUVE_TRANSPARENT);
						//if(faulty) g.setColor(UColor.NOIRF_TTTRANSPARENT);
					}
					else 
						g.setColor(UColor.channelColor[channel]);
				}
				if(!dead) {
					g.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
					g.fillOval(x - rayon2, y - rayon2, rayon2 * 2, rayon2 * 2);
				}
			}
			if(hide == 0 || hide == 1) {
				sensorUnit.setXY(x, y);
				sensorUnit.draw(g, 0, detection);
			}
			if(hide == 0 || hide == 3) {
				g.drawOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
			}
			if(hide == 2) {
				sensorUnit.setXY(x, y);
				sensorUnit.draw(g, 1, detection);
			}

			if (selected) {
				g.setColor(UColor.NOIR_TTRANSPARENT);
				g.drawOval(x - rayon - 4, y - rayon - 4, (rayon + 4) * 2,
						(rayon + 4) * 2);
			}

			if (flyingObjectDetection) {
				g.setColor(UColor.ROUGE_TRANSPARENT);
				g.fillOval(x - rayon * 2, y - rayon * 2, rayon * 4, rayon * 4);
			}

			drawIncRedDimNode(x, y, g);
			drawAugDimRadio(x, y, g);
			drawRadius(x, y, rayon, g);
			drawRadioRadius(x, y, rayon2, g);
			sensorUnit.drawDetectionRadius(x, y, capRadius, g);

			if (underSimulation) {
				g.setColor(UColor.GREEN);
				g.fillOval(x - 3, y - 3, 6, 6);
			} else {
				g.setColor(UColor.RED);
				if(dead) g.setColor(Color.BLACK);
				g.fillOval(x - 3, y - 3, 6, 6);
			}

			if (displayDetails && hide<4 && hide>0) {
				g.setColor(UColor.GREEN);
				g.fillRect(x+rayon+10, y+rayon-(int)(battery.getCapacity()/1000000.), 5, (int)(battery.getCapacity()/1000000.));
				g.setColor(Color.gray);
				g.drawRect(x+rayon+10, y+rayon-(int)(battery.getCapacity()/1000000.), 5, (int)(battery.getCapacity()/1000000.));
				g.drawLine(x+rayon, y+rayon, x+rayon+25, y+rayon);
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
		return sensorUnit.getRadius();
	}	

	// ------------------------------------------------------------------------
	// Return the type which is a SENSOR
	// In particular for this object (SensorNode) the type can be also 
	// a target
	// ------------------------------------------------------------------------
	@Override
	public int getType() {
		return type;
	}
	
	// Set type to SENSOR
	public void setAsSensor() {
		type = Device.SENSOR;
	}

	// Set type to SENSOR
	public void setAsTarget() {
		type = Device.TARGET;
	}


	@Override
	public String getIdFL() {
		return "S";
	}

	/**
	 * Set the capture unit
	 * 
	 * @param sensorUnit
	 */
	public void setCaptureUnit(SensorUnit sensorUnit) {
		this.sensorUnit = sensorUnit;
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
	public SensorNode clone() throws CloneNotSupportedException {
		SensorNode newSensor = (SensorNode) super.clone();
		SensorUnit newCaptureUnit = (SensorUnit) sensorUnit.clone();
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
	
	public void setDetecting(boolean detecting) {
		this.detecting = detecting ;
	}
	
	public boolean isDetecting() {
		return detecting ;
	}
	
}