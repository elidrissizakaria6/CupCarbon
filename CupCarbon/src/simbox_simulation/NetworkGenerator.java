/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
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

package simbox_simulation;

import javax.swing.JOptionPane;

import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Massinissa Lounis
 * @author Arezki Laga
 * @version 1.0
 * 
 *          Generate links (connections between sensors) and scripts (of the
 *          sensors) that are necessary for the simulation
 */
public class NetworkGenerator {

	// ------------------------------------------------------------------------
	// Check if everything it's OK for simulation
	// -> Check if each sensor has its own script 
	// ------------------------------------------------------------------------
	/**
	 * Check if each sensor has its own script 
	 */
	public static void check() {
		for(Device d : DeviceList.getNodes()) {
			if(d.getScriptFileName().equals("")) {
				JOptionPane.showMessageDialog(null, "Not Ready to simulate!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		JOptionPane.showMessageDialog(null, "Ready to simulate!", "Valid", JOptionPane.INFORMATION_MESSAGE);
	}
	// ------------------------------------------------------------------------
	// Generate the links and script matrices for the CPU simulation
	// ------------------------------------------------------------------------
	/**
	 * Generate the links and script matrices for the CPU simulation
	 */
	public static void generateForCpu() {
		NetworkGeneratorForCpu network = new NetworkGeneratorForCpu();
		network.start();
		try {
			network.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// ------------------------------------------------------------------------
	// Generate the links and script vectors for the GPU simulation
	// ------------------------------------------------------------------------
	/**
	 * Generate the links and script vectors for the GPU simulation
	 */
	public static void generateForGpu() {
		NetworkGeneratorForGpu network = new NetworkGeneratorForGpu();
		network.start();
		try {
			network.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
