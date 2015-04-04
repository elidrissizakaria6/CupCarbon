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

package wisen_simulation_mehdi;

import device.Device;

public class MehdiEvent implements Comparable<MehdiEvent> {

	private int message;
	private long eventDate;
	private int epsilon;
	private int powerRatio = 100;
	private MehdiCommands eventType;
	private MehdiDeviceSimulator deviceSimulator = null;
	private Device device = null;
	private MehdiSimulation simulation = null;
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}	

	public MehdiEvent() {
		this(0, 0, 0, MehdiCommands.COM_UNKNOWN, 100);
	}

	public MehdiEvent(int message, long eventdate, MehdiCommands eventtype, int powratio) {
		this(message, eventdate, 0, eventtype, powratio);
	}

	public MehdiEvent(int message, long eventdate, int epsilon, MehdiCommands eventtype,
			int powratio) {
		setMessage(message);
		setEventDate(eventdate);
		setEpsilon(epsilon);
		setEventType(eventtype);
		setPowerRatio(powratio);
		setDevicesimulator(null);
		setSimulation(null);
	}

	public MehdiEvent(int message, long eventdate, int epsilon, MehdiCommands eventtype) {
		this(message, eventdate, epsilon, eventtype, 100);
	}

	public void update(int message, long date, int epsilon, MehdiCommands eventtype,
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
	public int compareTo(MehdiEvent e) {
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

	public MehdiCommands getEventType() {
		return eventType;
	}

	public void setEventType(MehdiCommands eventType) {
		this.eventType = eventType;
	}

	public int getPowerRatio() {
		return powerRatio;
	}

	public void setPowerRatio(int powratio) {
		this.powerRatio = powratio;
	}

	public MehdiSimulation getSimulation() {
		return simulation;
	}

	public void setSimulation(MehdiSimulation simulation) {
		this.simulation = simulation;
	}

	public MehdiDeviceSimulator getDevicesimulator() {
		return deviceSimulator;
	}

	public void setDevicesimulator(MehdiDeviceSimulator devicesimulator) {
		this.deviceSimulator = devicesimulator;
	}

	public int getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(int epsilon) {
		this.epsilon = epsilon;
	}

}
