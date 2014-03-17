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

package wisen_simulation;

import device.Device;

public interface Simulator_Interface {

	public void initSimulation();

	public void startSimulation();

	public void stopSimulation();

	public void pauseSimulation();

	public void endSimulation();

	public void consumptionEnergy(Event event);

	public void setSimulationMode(SimulationMode sMode);

	public SimulationMode getSimulationMode();

	public void setSimulationDelay(long delay);

	public long getSimulationDelay();

	public void setSimulationLogicDelay(long delay);

	public long getSimulationLogicDelay();

	public long getRemainingSimulationTime();

	public int getNumberSentMessages();

	public int getNumberSentMessages(long delai1, long delai2, TimeMode tMode);

	public int getNumberSentMessages(Device device);

	public int getNumberSentMessages(Device device, long delai1, long delai2, TimeMode tMode);

	public Object getSimulationState();

}
