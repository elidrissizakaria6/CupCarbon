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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ListIterator;

import map.Layer;
import project.Project;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Arezki Laga
 * @version 1.0
 * 
 * 
 */
public class CpuSimulation extends Thread {

	private boolean discreteEvent = true;
	private boolean mobility = false;
	private int step = 1;
	private int energyMax = 0;
	private int iterNumber = 0;
	private int nbSensors = 0;
	private int scriptSize = 0;
	private int[][][] script;
	private byte[] iscript;
	private int[] event;
	private int[] event2;
	private int[] deadSensor;
	private int[] energy;
	private byte eRTx = 1;
	private byte[][] links;

	private boolean visual;
	private int visualDelay;

	public CpuSimulation() {
		init();
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void init() {
		discreteEvent = SimulationInputs.discreteEvent;
		mobility = SimulationInputs.mobility;
		step = SimulationInputs.step;
		energyMax = SimulationInputs.energyMax;
		iterNumber = SimulationInputs.iterNumber;
		nbSensors = SimulationInputs.nbSensors;
		scriptSize = SimulationInputs.scriptSize;
		visual = SimulationInputs.visual;
		visualDelay = SimulationInputs.visualDelay;

		SimulationInputs.gpuScript = null;
		SimulationInputs.gpuLinks = null;
		script = SimulationInputs.script;
		links = SimulationInputs.links;

		iscript = new byte[nbSensors];
		event = new int[nbSensors];
		event2 = new int[nbSensors];
		deadSensor = new int[nbSensors];
		energy = new int[nbSensors];

	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void simulate() {
		WsnSimulationWindow.setState("Simulation : initialization ...");
		System.out.println("Initialization ... ");
		List<Device> devices = DeviceList.getNodes();
		int k = 0;
		for (Device device : devices) {
			event[k] = script[k][0][1];
			energy[k] = energyMax;
			if (mobility) {
				device.fixori();
				device.loadRouteFromFile();
				if (device.canMove())
					event2[k] = device.getNextTime();
				else
					event2[k] = 999999999;
			}
			k++;
		}
		System.out.println("End of Initialization.");
		int min = 0;
		int min1;
		int min2;
		long time = 0;
		long startTime = System.currentTimeMillis();
		System.out.println("Start Simulation (CPU : D-Event) ... ");
		long iter = 0;
		WsnSimulationWindow.setState("Simulation : End of initialization.");
		WsnSimulationWindow.setState("Simulate (CPU) ...");

		try {
			String as = "";
			if (mobility)
				as = "_mob";
			PrintStream ps = new PrintStream(new FileOutputStream(
					Project.getProjectResultsPath() + "/cpu_simulation" + as
							+ ".csv"));
			int conso;
			for (iter = 0; (iter < iterNumber) && (!stopSimulation()); iter++) {
				ps.print(time + ";");

				for (int i = 0; i < nbSensors; i++) {
					ps.print(energy[i] + ";");
				}
				ps.println();

				// for (int i = 0; i < nbSensors; i++) {
				// System.out.print(event[i]+" ");
				// }
				// for (int i = 0; i < nbSensors; i++) {
				// System.out.print(event2[i]+" ");
				// }
				// System.out.println();

				// ============================================================
				if (mobility) {
					Device d1 = null;
					Device d2 = null;
					ListIterator<Device> iterator;
					ListIterator<Device> iterator2;
					iterator = devices.listIterator();
					int n = devices.size();
					int i2 = 0;
					int j2 = 0;
					while (iterator.hasNext()) {
						d1 = iterator.next();
						links[i2][i2] = 1;
						if (iterator.nextIndex() < n) {
							j2 = i2 + 1;
							iterator2 = devices.listIterator(iterator
									.nextIndex());
							while (iterator2.hasNext()) {
								d2 = iterator2.next();
								if (d1.radioDetect(d2)) {
									links[i2][j2] = 1;
									links[j2][i2] = 1;
								} else {
									links[i2][j2] = 0;
									links[j2][i2] = 0;
								}
								j2++;
							}
							i2++;
						}
					}
					if (discreteEvent) {
						min1 = getMin();
						min2 = getMin2();
					} else {
						min1 = step;
						min2 = step;
					}
					if (min1 <= min2)
						min = min1;
					if (min2 < min1)
						min = min2;
				} else
					min = getMin();
				// ============================================================

				time += min;

				for (int i = 0; i < nbSensors; i++) {
					conso = 0;
					for (int j = 0; j < nbSensors; j++) {
						conso += links[i][j] * script[j][iscript[j]][0]
								* (1 - deadSensor[j]);
					}
					energy[i] -= min * conso * eRTx;
					if (energy[i] < 0)
						energy[i] = 0;
					event[i] -= min;
					if (mobility)
						event2[i] -= min;
				}

				for (int i = 0; i < nbSensors; i++) {
					if (event[i] == 0) {
						iscript[i]++;
						iscript[i] = (byte) (iscript[i] % scriptSize);
						event[i] = script[i][iscript[i]][1];
					}
					if (mobility)
						if (event2[i] == 0) {
							if (devices.get(i).canMove()) {
								devices.get(i).exeNext(visual, visualDelay);
								event2[i] = devices.get(i).getNextTime();
							}
						}
					if (energy[i] <= 0) {
						event[i] = 99999999;
						deadSensor[i] = 1;
					}
				}
				WsnSimulationWindow
						.setProgress((int) (1000 * iter / iterNumber));
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("End of Simulation (CPU : D-Event).");
		System.out.println(((endTime - startTime) / 1000.) + " sec");
		WsnSimulationWindow.setState("End (CPU Sim) at iter " + iter
				+ ". Simulation Time : " + ((endTime - startTime) / 1000.)
				+ " sec.");
		WsnSimulationWindow.setProgress(0);
		int i = 0;
		for (Device d : devices) {
			d.getBattery().setCapacity(energy[i++]);
		}
		if (mobility) {
			for (Device device : devices) {
				device.toori();
				device.stopSimulation();
			}
			Layer.getMapViewer().repaint();
		}
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	@Override
	public void run() {
		simulate();
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public int getMin() {
		int min = (int) 10e8;
		for (int i = 0; i < nbSensors; i++)
			if ((min > event[i]))
				min = event[i];
		return min;
	}

	public int getMin2() {
		int min = (int) 10e8;
		for (int i = 0; i < nbSensors; i++)
			if ((min > event2[i]))
				min = event2[i];
		return min;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public boolean stopSimulation() {
		for (int k = 0; k < nbSensors; k++) {
			if (energy[k] > 0)
				return false;
		}
		return true;
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------

}