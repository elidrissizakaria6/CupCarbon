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

package battery;

import radiomodule.RadioModule;
import captureunit.CaptureUnit;
import controlunit.UControl;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class Battery implements Cloneable {

	private UControl uProc = new UControl();
	private RadioModule radioModule = new RadioModule(0);
	private CaptureUnit captureUnit;
	private double initialCapacity = 100000.;
	private double capacity = initialCapacity;

	/**
	 * Battery initialization
	 * 
	 * @param captureUnit
	 */
	public Battery(CaptureUnit captureUnit) {
		this.captureUnit = captureUnit;
	}

	/**
	 * @return the initial capacity of the battery
	 */
	public int getInitialCapacity() {
		// return (int)(capacite/capaciteDeBase*100.) ;
		return (int) (initialCapacity);
	}
	
	/**
	 * @return the capacity of the battery
	 */
	public int getCapacity() {
		// return (int)(capacite/capaciteDeBase*100.) ;
		return (int) (capacity);
	}

	/**
	 * @return the capacity of the battery (in percent)
	 */
	public int getCapacityInPercent() {
		return (int) (capacity / initialCapacity * 100.);
	}

	/**
	 * Set the value of the capacity of the battery
	 * 
	 * @param capacity
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * Initialization of the battery (energy max)
	 */
	public void init() {
		capacity = initialCapacity;
	}

	/**
	 * Consume 1 unit of the battery
	 */
	public void consume() {
		double vUp = 0;
		double vA = 0;
		double vUc = 0;

		if (uProc != null)
			vUp = uProc.getConsumedUnit();
		if (radioModule != null)
			vA = radioModule.getConsumedUnit();
		if (captureUnit != null)
			vUc = captureUnit.getConsumedUnit();

		double v = vUp + vA + vUc;
		if (capacity >= v)
			capacity -= v;
		else if (capacity > 0)
			capacity = 0;
	}

	/**
	 * Consume v units of the battery
	 * 
	 * @param v
	 *            Number of the units to consume
	 */
	public void consume(double v) {
		capacity -= v;
		if (capacity < 0)
			capacity = 0;
	}

	/**
	 * Consume the battery by taking into account the consumptions of the
	 * elements that are connected to the battery: - Microcontroller - RadioModule -
	 * Capture unit
	 * 
	 * @param unit1
	 * @param unit2
	 * @param unit3
	 */
	public void consume(double unit1, double unit2, double unit3) {
		double vUp = 0;
		double vA = 0;
		double vUc = 0;

		if (uProc != null)
			vUp = uProc.getConsumedUnit(unit1);
		if (radioModule != null)
			vA = radioModule.getConsumedUnit(unit2);
		if (captureUnit != null)
			vUc = captureUnit.getConsumedUnit(unit3);

		double v = vUp + vA + vUc;
		if (capacity >= v)
			capacity -= v;
		else if (capacity > 0)
			capacity = 0;
	}

	/**
	 * @return if the battery is empty
	 */
	public boolean empty() {
		return (capacity <= (30. * initialCapacity / 100.));
	}

	/**
	 * Connect to a Capture Unit
	 * 
	 * @param unitCapture
	 */
	public void setCaptureUnit(CaptureUnit unitCapture) {
		this.captureUnit = unitCapture;
	}

	/**
	 * Connect to a Microcontroller
	 * 
	 * @param uProc
	 */
	public void setUControl(UControl uProc) {
		this.uProc = uProc;
	}

	/**
	 * Connect to a Antanna
	 * 
	 * @param radioModule
	 */	
	public void setAntenna(RadioModule radioModule) {
		this.radioModule = radioModule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Battery clone() throws CloneNotSupportedException {
		Battery newBattery = (Battery) super.clone();
		newBattery.setCaptureUnit(captureUnit.clone());
		newBattery.setUControl((UControl) uProc.clone());
		newBattery.setAntenna((RadioModule) radioModule.clone());
		return newBattery;
	}
}
