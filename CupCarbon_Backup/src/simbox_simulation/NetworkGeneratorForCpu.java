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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import map.Layer;
import project.Project;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 * 
 *          Generate links (connections between sensors) and scripts (of the
 *          sensors) that are necessary for the simulation
 */
public class NetworkGeneratorForCpu extends Thread {

	// ------------------------------------------------------------------------
	// Generate the links and script matrices for the CPU simulation
	// ------------------------------------------------------------------------
	public void generate() {
		WsnSimulationWindow.setState("Network generating ...");
		System.out.println("Network Generation For CPU ...");

		Layer.getDeviceList().initId();

		Device d1 = null;
		Device d2 = null;

		ListIterator<Device> iterator;
		ListIterator<Device> iterator2;

		List<Device> nodes = DeviceList.getNodes();
		int n = nodes.size();
		int scriptSize = SimulationInputs.scriptSize;

		SimulationInputs.links = new byte[n][n];
		SimulationInputs.script = new int[n][scriptSize][2];

		int i = 0;
		int j = 0;
		iterator = nodes.listIterator();

		WsnSimulationWindow.setState("Network Generating for CPU Simulation ...");
		int iterNumber = n * (n + 1) / 2;
		int iter = 0;
		while (iterator.hasNext()) {
			d1 = iterator.next();
			d1.getBattery().init(SimulationInputs.energyMax);
			String s = "";
			try {
				System.out.println("---> " + d1.getScriptFileName());
				String projectScriptPath = Project.getProjectScriptPath() + File.separator + d1.getScriptFileName();
				BufferedReader br = new BufferedReader(new FileReader(projectScriptPath));
				for (int j2 = 0; j2 < scriptSize; j2++) {
					s = br.readLine();
					System.out.println(s);
					String[] inst = s.split(" ");
					if (inst[0].toLowerCase().equals("psend")) {
						SimulationInputs.script[i][j2][0] = 1;
						SimulationInputs.script[i][j2][1] = Integer
								.parseInt(inst[1]) * 8;
					}
					if (inst[0].toLowerCase().equals("delay")) {
						SimulationInputs.script[i][j2][0] = 0;
						SimulationInputs.script[i][j2][1] = Integer
								.parseInt(inst[1]) * Device.dataRate / 1000;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			SimulationInputs.links[i][i] = 1;
			if (iterator.nextIndex() < n) {
				j = i + 1;
				iterator2 = nodes.listIterator(iterator.nextIndex());
				while (iterator2.hasNext()) {
					WsnSimulationWindow.setProgress((int) (1000 * (iter++) / iterNumber));
					d2 = iterator2.next();
					if (d1.radioDetect(d2)) {
						SimulationInputs.links[i][j] = 1;
						SimulationInputs.links[j][i] = 1;
					}
					j++;
				}
				i++;
			}
		}
		
		WsnSimulationWindow.setProgress(0);
		SimulationInputs.nbSensors = n;
		System.out.println("End of network generating.");
		WsnSimulationWindow.setState("End of network generating.");
	}
	// ------------------------------------------------------------------------
	// Generate the links and script matrices for the CPU simulation (Thread)
	// ------------------------------------------------------------------------
	@Override
	public void run() {
		generate();
	}
}
