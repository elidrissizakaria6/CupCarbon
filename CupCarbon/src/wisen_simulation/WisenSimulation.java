package wisen_simulation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import map.Layer;
import project.Project;
import script.CommandType;
import simbox_simulation.SimulationInputs;
import cupcarbon.WsnSimulationWindow;
import device.Device;
import device.DeviceList;

public class WisenSimulation extends Thread {

	private boolean discreteEvent = true;
	private boolean mobility = false;
	private int step = 1;
	private int iterNumber = 0;
	//private int nbSensors = 0;

	private boolean visual;
	private int visualDelay;

	public WisenSimulation() {

	}

	// ------------------------------------------------------------
	// Run simulation 
	// ------------------------------------------------------------
	public void simulate() {
		SimLog.init();
		discreteEvent = SimulationInputs.discreteEvent;
		mobility = SimulationInputs.mobility;
		System.out.println("mobility "+mobility);
		step = SimulationInputs.step;
		iterNumber = SimulationInputs.iterNumber;		
		visual = SimulationInputs.visual;
		visualDelay = SimulationInputs.visualDelay;

		WsnSimulationWindow.setState("Simulation : initialization ...");
		System.out.println("Initialization ... ");
		List<Device> devices = DeviceList.getNodes();
		SimLog.add("=========");
		for (Device device : devices) {
			device.setDead(false);
			device.getBattery().init(SimulationInputs.energyMax);
			device.loadScript();
			device.getScript().init();
			device.getScript().execute();
			device.setEvent(device.getScript().getEvent());
			if (mobility) {
				device.fixori();
				device.loadRouteFromFile();
				if (device.canMove())
					device.setEvent2(device.getNextTime());
				else
					device.setEvent2(999999999);
			}
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
					Project.getProjectResultsPath() + "/cpu_simulation_cc" + as + ".csv"));
			int conso;
			
			SimLog.add("=========");
			if (mobility) {					
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
			for (iter = 0; (iter < iterNumber) && (!stopSimulation()); iter++) {
				
				SimLog.add("-------------------");
				SimLog.add("-> "+min);
				SimLog.add("-------------------");
				ps.print(time + ";");

				for (Device device : devices) {
					ps.print(device.getBatteryLevel() + ";");
				}
				for (Device device : devices) {
					ps.print(device.getEvent() + ";");
				}
				
				// ============================================================				
				
				time += min;				
				for (Device device1 : devices) {
					//if(!device1.isDead()) {
						conso = 0;
						for (Device device2 : devices) {						
							conso += (device1.radioDetect(device2)?1:0) 								
									 * device2.getScript().getCurrent().getCommandType()
									* (1 - (device2.isDead()?1:0));
						}
						device1.consume(min * conso);					
						device1.setEvent(device1.getEvent()-min);
						
						if (mobility)
							device1.setEvent2(device1.getEvent2()-min);
					//}
				}

				ps.println();
				for (Device device1 : devices) {
					//if(!device1.isDead()) {
						//device1.getScript().waitVerification();						
						if ((device1.getEvent() == 0) || (device1.getScript().getCurrent().getCommandType() == CommandType.WAIT)) {						
						//if ((device1.getEvent() == 0)) {	
							
							device1.getScript().execute();
							device1.setEvent(device1.getScript().getEvent());
							//device1.setEvent(device1.getScript().next().getEvent());
						}
						if (mobility)
							if (device1.getEvent2() == 0) {
								if (device1.canMove()) {
									device1.moveToNext(visual, visualDelay);
									device1.setEvent2(device1.getNextTime());
								}
							}
						if (device1.getBattery().empty()) {
							//device1.setEvent(99999999);
							device1.setDead(true);
						}
					//}
				}
				if (mobility) {					
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
				WsnSimulationWindow.setProgress((int) (1000 * iter / iterNumber));
			}
			SimLog.close();
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

		if (mobility) {
			for (Device device : devices) {
				device.toori();
				device.stopSimulation();
			}
			Layer.getMapViewer().repaint();
		}		
	}

	// ------------------------------------------------------------
	// Run simulation (call the simulate() method)
	// ------------------------------------------------------------
	@Override
	public void run() {
		simulate();
	}

	// ------------------------------------------------------------
	// Min : currEvent1
	// ------------------------------------------------------------
	public int getMin() {
		//int min = (int) 10e8;
		int min = Integer.MAX_VALUE;
		for (Device device : DeviceList.getNodes()) {
			//SimLog.add("::: "+device.getEvent());
			if ((min > device.getEvent()))
				min = device.getEvent();
		}
		return min;
	}

	// ------------------------------------------------------------
	// Min : currEvent2
	// ------------------------------------------------------------
	public int getMin2() {
		int min = (int) 10e8;
		for (Device device : DeviceList.getNodes())
			if ((min > device.getEvent2()))
				min = device.getEvent2();
		return min;
	}

	// ------------------------------------------------------------
	// Stop Simulation !
	// ------------------------------------------------------------
	public boolean stopSimulation() {
		for (Device device : DeviceList.getNodes()) {
			if (!device.isDead())
				return false;
		}
		return true;
	}
	
}
