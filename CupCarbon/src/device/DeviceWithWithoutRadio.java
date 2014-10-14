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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import map.Layer;
import project.Project;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public abstract class DeviceWithWithoutRadio extends Device {

	protected LinkedList<Long> routeTime;
	protected LinkedList<Double> routeX;
	protected LinkedList<Double> routeY;
	protected boolean loop = false;
	protected int nLoop = 0;
	protected int routeIndex = 0;
	protected boolean readyForSimulation = false;

	/**
	 * Empty constructor
	 */
	public DeviceWithWithoutRadio() {
	}

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public DeviceWithWithoutRadio(double x, double y, double radius) {
		super(x, y, radius);
	}

	// --------------------------------------------------------------------------------
	// Mobility methods
	// --------------------------------------------------------------------------------
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
				fis = new FileInputStream(Project.getProjectGpsPath() + File.separator + gpsFileName);				
				b = new BufferedReader(new InputStreamReader(fis));
				underSimulation = true;
				b.readLine();
				b.readLine();
				b.readLine();
				loop = Boolean.parseBoolean(b.readLine());
				nLoop = Integer.parseInt(b.readLine());
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

	// ------------------------------------------------------------------------
	// Do some actions before the simulation
	// ------------------------------------------------------------------------
	public void preprocessing() {
		routeIndex = 0;
	}

	// ------------------------------------------------------------------------
	// Simulate
	// ------------------------------------------------------------------------
	public void runSensorSimulation() {
		loadRouteFromFile();
		fixori();
		underSimulation = true;
		Consumer c = new Consumer(this, 1000);
		c.start();
		underSimulation = false;
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
			try {
				Thread.sleep(toWait * Device.moveSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			routeIndex = 0;
			selected = false;
			toori();
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
		runSensorSimulation();
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
			return ((diff * 100) * Device.dataRate / 1000);
		}
		return 0;
	}

	// ------------------------------------------------------------------------
	// Go to next point and update
	// ------------------------------------------------------------------------
	@Override
	public void exeNext(boolean visual, int visualDelay) {
		if (routeTime != null && nLoop > 0) {
			routeIndex++;
			if ((routeIndex == (routeTime.size()))) {
				nLoop--;
				if (!loop || nLoop == 0) {
					routeIndex--;
				} else {
					routeIndex = 0;
				}

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
	// @Override
	public void goToNext() {
		if (routeTime != null) {
			routeIndex++;
			if (routeIndex == routeTime.size()) {
				if (loop) {
					nLoop--;
					routeIndex = 0;
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Test the existence of a next point
	// ------------------------------------------------------------------------
	public boolean hasNext() {
		if (nLoop == 0 && loop)
			return false;
		if (routeIndex < routeTime.size())
			return true;
		return false;
	}

	// ------------------------------------------------------------------------
	// Test if the gps file name is not empty
	// ------------------------------------------------------------------------
	public boolean canMove() {
		if (getGPSFileName().equals(""))
			return false;
		return true;
	}

	public LinkedList<Long> getRouteTime() {
		return routeTime;
	}

	public void setRouteTime(LinkedList<Long> routeTime) {
		this.routeTime = routeTime;
	}

	public LinkedList<Double> getRouteX() {
		return routeX;
	}

	public void setRouteX(LinkedList<Double> routeX) {
		this.routeX = routeX;
	}

	public LinkedList<Double> getRouteY() {
		return routeY;
	}

	public void setRouteY(LinkedList<Double> routeY) {
		this.routeY = routeY;
	}
	
	
}
