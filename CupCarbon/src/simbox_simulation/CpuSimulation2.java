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
import java.util.LinkedList;

import map.Layer;
import project.Project;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 * 
 * 
 */
public class CpuSimulation2 extends Thread {

	public static boolean discreteEvent = true;
	public static int step = 1 ;
	public static int energyMax = 0;
	public static int iterNumber = 0;
	public static int nbSensors = 0;
	public static int scriptSize = 0;

	public static LinkedList<double[][][]> tours;
	
	public static int[][][] script;
	public static byte[] iscript;
	public static int[] event;
	public static int[] deadSensor;
	public static int[] energy;
	public static byte eRTx = 1;
	public static byte[][] links;

	public CpuSimulation2() {
		init();
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void init() {
		discreteEvent = SimulationInputs.discreteEvent;
		step = SimulationInputs.step;
		energyMax = SimulationInputs.energyMax;
		iterNumber = SimulationInputs.iterNumber;
		nbSensors = SimulationInputs.nbSensors;
		scriptSize = SimulationInputs.scriptSize;
		
		SimulationInputs.gpuScript=null;
		SimulationInputs.gpuLinks=null;
		script = SimulationInputs.script;
		links = SimulationInputs.links;
		
		iscript = new byte[nbSensors];
		event = new int[nbSensors];
		deadSensor = new int[nbSensors];
		energy = new int[nbSensors];
		
	}

	// ------------------------------------------------------------
	//
	// ------------------------------------------------------------
	public void simulate() {	
		WsnSimulationWindow.setState("Simulation : initialization ...");
		System.out.println("Initialization ... ");
		for (int i = 0; i < nbSensors; i++) {
			event[i] = script[i][0][1];
			energy[i] = energyMax;
		}
		System.out.println("End of Initialization.");
		int min;
		long time = 0;		
		long startTime = System.currentTimeMillis();		
		System.out.println("Start Simulation (CPU : D-Event) ... ");
		long iter=0;
		WsnSimulationWindow.setState("Simulation : End of initialization.");
		WsnSimulationWindow.setState("Simulate (CPU) ...");
				
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(Project.getProjectResultsPath()+"/cpu_simulation.csv"));			
			for (iter = 0; (iter < iterNumber) && (!stopSimulation()); iter++) {
				ps.print(time+ ";");
				for (int i = 0; i < nbSensors; i++) {
					ps.print(energy[i]+ ";");
				}
				ps.println();
				if(discreteEvent)
					min = min();
				else
					min = step;
				time += min;			
				int conso;
				for (int i = 0; i < nbSensors; i++) {				
					conso = 0;
					for (int j = 0; j < nbSensors; j++) {
						conso += links[i][j] * script[j][iscript[j]][0] * (1-deadSensor[j]);
					}
					energy[i] -= min * conso * eRTx;
					if(energy[i]<0) energy[i]=0;
					event[i] -= min;
					
				}

				for (int i = 0; i < nbSensors; i++) {
					if (event[i] == 0) {
						iscript[i]++;
						iscript[i] = (byte)(iscript[i] % scriptSize);
						event[i] = script[i][iscript[i]][1];
					}
					if (energy[i] <= 0) {
						event[i] = 99999999;
						deadSensor[i]=1;
					}				
				}
				WsnSimulationWindow.setProgress((int)(1000*iter/iterNumber));
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		long endTime = System.currentTimeMillis();
		System.out.println("End of Simulation (CPU : D-Event).");		
		System.out.println(((endTime-startTime)/1000.)+" sec");
		WsnSimulationWindow.setState("End (CPU Sim) at iter " + iter + ". Simulation Time : "
				+ ((endTime - startTime) / 1000.) + " sec.");
		WsnSimulationWindow.setProgress(0);
		int i=0;
		for(Device d : DeviceList.getNodes()) {
			d.getBattery().setCapacity(energy[i++]);
		}
		Layer.getMapViewer().repaint();
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
	public static int min() {
		int min = (int) 10e8;
		for (int i = 0; i < nbSensors; i++)
			if ((min > event[i]))
				min = event[i];
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