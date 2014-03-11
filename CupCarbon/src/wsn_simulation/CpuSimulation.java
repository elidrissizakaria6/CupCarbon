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

package wsn_simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
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

	public static boolean discreteEvent = true;
	public boolean mobility = false;
	public static int step = 1;
	public static int energyMax = 0;
	public static int iterNumber = 0;
	public static int nbSensors = 0;
	public static int scriptSize = 0;

	public static LinkedList<double[][][]> tours;

	public static int[][][] script;
	public static byte[] iscript;
	public static int[] event;
	public static int[] event2;
	public static int[] deadSensor;
	public static int[] energy;
	public static byte eRTx = 1;
	public static byte[][] links;
	
	public boolean visual;
	public int visualDelay;

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
		// for (int k = 0; k < nbSensors; k++) {
		for (Device device : devices) {
			event[k] = script[k][0][1];
			energy[k] = energyMax;
			if (mobility) {
				device.fixori();
				device.loadRouteFromFile();
				if(device.canMove())
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
			String as = "" ;
			if(mobility) as = "_mob";
			PrintStream ps = new PrintStream(new FileOutputStream(Project.getProjectResultsPath() + "/cpu_simulation"+as+".csv"));
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
					List<Device> nodes = DeviceList.getNodes();
					iterator = nodes.listIterator();
					int n = nodes.size();
					int ii = 0;
					int jj = 0;
					while (iterator.hasNext()) {
						d1 = iterator.next();
						links[ii][ii] = 1;
						if (iterator.nextIndex() < n) {
							jj = ii + 1;
							iterator2 = nodes
									.listIterator(iterator.nextIndex());
							while (iterator2.hasNext()) {
								d2 = iterator2.next();
								if (d1.radioDetect(d2)) {
									links[ii][jj] = 1;
									links[jj][ii] = 1;
								} else {
									links[ii][jj] = 0;
									links[jj][ii] = 0;
								}
								jj++;
							}
							ii++;
						}
					}
					if (discreteEvent) {
						min1 = min();
						min2 = min2();
					} else {
						min1 = step;
						min2 = step;
					}

					if (min1 <= min2)
						min = min1;
					if (min2 < min1)
						min = min2;
				} else
					min = min();
				// ============================================================

				time += min;

				for (int i = 0; i < nbSensors; i++) {
					conso = 0;
					for (int j = 0; j < nbSensors; j++) {
						conso += links[i][j] * script[i][iscript[j]][0]
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
							if(DeviceList.getNodes().get(i).canMove()) {
								DeviceList.getNodes().get(i).exeNext(visual, visualDelay);
								event2[i] = DeviceList.getNodes().get(i).getNextTime();
							}
						}
					if (energy[i] <= 0) {
						event[i] = 99999999;
						deadSensor[i] = 1;
					}
				}
				WsnSimulationWindow.setProgress((int) (1000 * iter / iterNumber));
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
		for (Device d : DeviceList.getNodes()) {
			d.getBattery().setCapacity(energy[i++]);
		}
		if(mobility) {
			for (Device device : devices) {
				device.tori();
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
	public int min() {
		int min = (int) 10e8;
		for (int i = 0; i < nbSensors; i++)
			if ((min > event[i]))
				min = event[i];
		return min;
	}

	public int min2() {
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