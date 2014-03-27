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

package wisen_simulation2;

import device.Device;

public class Event implements Comparable<Event> {

	private int message;
	private long eventDate;
	private int epsilon;
	private int powerRatio = 100;
	private Commands eventType;
	private DeviceSimulator deviceSimulator = null;
	private Device device = null;
	private Simulation simulation = null;
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}	

	public Event() {
		this(0, 0, 0, Commands.COM_UNKNOWN, 100);
	}

	public Event(int message, long eventdate, Commands eventtype, int powratio) {
		this(message, eventdate, 0, eventtype, powratio);
	}

	public Event(int message, long eventdate, int epsilon, Commands eventtype,
			int powratio) {
		setMessage(message);
		setEventDate(eventdate);
		setEpsilon(epsilon);
		setEventType(eventtype);
		setPowerRatio(powratio);
		setDevicesimulator(null);
		setSimulation(null);
	}

	public Event(int message, long eventdate, int epsilon, Commands eventtype) {
		this(message, eventdate, epsilon, eventtype, 100);
	}

	public void update(int message, long date, int epsilon, Commands eventtype,
			int powratio) {
		setMessage(message);
		setEventDate(date);
		setEpsilon(epsilon);
		setEventType(eventtype);
		setPowerRatio(powratio);
		if (simulation != null) {
			simulation.getScheduler().sort();
		}
	}

	@Override
	public int compareTo(Event e) {
		return ((this.eventDate == e.eventDate) ? (this.epsilon - e.epsilon)
				: ((this.eventDate > e.eventDate) ? 1 : -1));
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public long getEventDate() {
		return eventDate;
	}

	public void setEventDate(long eventDate) {
		this.eventDate = eventDate;
	}

	public Commands getEventType() {
		return eventType;
	}

	public void setEventType(Commands eventType) {
		this.eventType = eventType;
	}

	public int getPowerRatio() {
		return powerRatio;
	}

	public void setPowerRatio(int powratio) {
		this.powerRatio = powratio;
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	public DeviceSimulator getDevicesimulator() {
		return deviceSimulator;
	}

	public void setDevicesimulator(DeviceSimulator devicesimulator) {
		this.deviceSimulator = devicesimulator;
	}

	public int getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(int epsilon) {
		this.epsilon = epsilon;
	}

}
