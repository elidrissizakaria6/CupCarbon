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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import map.Layer;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Arezki Laga
 * @version 1.0
 * 
 *          Generate links (connections between sensors) and scripts (of the
 *          sensors) that are necessary for the simulation
 */
public class NetworkGeneratorForGpu extends Thread {
	// ------------------------------------------------------------------------
	// Generate the links and script vectors for the GPU simulation
	// ------------------------------------------------------------------------
	@Override
	public void run() {
		System.out.println("Network Generation For GPU ...");

		Layer.getDeviceList().initId();

		Device d1 = null;
		Device d2 = null;

		ListIterator<Device> iterator;
		ListIterator<Device> iterator2;

		List<Device> nodes = DeviceList.getNodes();
		int n = nodes.size();
		int scriptSize = SimulationInputs.scriptSize;

		SimulationInputs.gpuLinks = new byte[n * n];
		SimulationInputs.gpuScript = new int[n * scriptSize * 2];

		iterator = nodes.listIterator();
		int i = 0;
		int j = 0;
		String s = "";

		WsnSimulationWindow.setState("Network Generating for GPU Simulation ...");
		
		int iterNumber = n * (n + 1) / 2;
		int iter = 0;
		
		while (iterator.hasNext()) {			
			d1 = iterator.next();
			d1.getBattery().init(SimulationInputs.energyMax);			
			try {
				BufferedReader br = new BufferedReader(new FileReader(d1.getScriptFileName()));
				for (int j2 = 0; j2 < (scriptSize * 2); j2 += 2) {
					s = br.readLine();
					String[] inst = s.split(" ");
					if (inst[0].toLowerCase().equals("psend")) {
						SimulationInputs.gpuScript[i * scriptSize * 2 + j2] = Integer.parseInt(inst[1]) * 8;
						SimulationInputs.gpuScript[i * scriptSize * 2 + j2 + 1] = 1;
					}
					if (inst[0].toLowerCase().equals("delay")) {
						SimulationInputs.gpuScript[i * scriptSize * 2 + j2] = Integer.parseInt(inst[1]) * Device.dataRate / 1000;
						SimulationInputs.gpuScript[i * scriptSize * 2 + j2 + 1] = 0;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			SimulationInputs.gpuLinks[i * n + i] = 1;
			if (iterator.nextIndex() < n) {
				j = i + 1;
				iterator2 = nodes.listIterator(iterator.nextIndex());
				while (iterator2.hasNext()) {
					WsnSimulationWindow.setProgress((int) (1000 * (iter++) / iterNumber));
					d2 = iterator2.next();
					if (d1.radioDetect(d2)) {
						SimulationInputs.gpuLinks[i * n + j] = 1;
						SimulationInputs.gpuLinks[j * n + i] = 1;
					}
					j++;
				}
			}
			i++;
		}
		WsnSimulationWindow.setProgress(0);
		SimulationInputs.nbSensors = n;
		System.out.println("End of network generating.");
		WsnSimulationWindow.setState("End of network generating.");
	}
}
