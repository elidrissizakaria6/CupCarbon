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

package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import project.Project;
import synchronization.Lock;
import device.Device;

public class DeviceSimulator implements Runnable {
	
	public static int index = 0;
	public String scenariofile = "";
	private String resultFile = "RS";
	public Lock lock;
	public Thread thread = null;
	public Event currentEvent;
	public int id;
	private long horloge = 0;
	public List<Event> events = null;
	private int eventsNumber = 0;
	private Simulation simulator = null;
	private Device device;
	private int eps = 0;
	private PrintStream rbr = null;

	public void saveResult(long date) {
		rbr.println("" + date + " " + device.getId() + " " + device.getUserId()
				+ " " + device.getBatteryLevel());
	}

	public void saveResult2() {
		rbr.println("" + (System.nanoTime() - simulator.startTime) + " "
				+ device.getId() + " " + device.getUserId() + " "
				+ device.getBatteryLevel());
	}

	public String getResultFile() {
		return resultFile + device.getId();
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
		resultFile.replaceAll(" ", "_");
	}

	public Simulation getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulation simulator) {
		this.simulator = simulator;
	}

	public DeviceSimulator() {
		this(null);
	}

	public DeviceSimulator(Device device) {
		id = ++index;
		this.setDevice(device);
		lock = new Lock();
	}

	public boolean scenarioAssigned() {
		return (scenariofile != "");
	}

	public void loadScenario() {

		try {
			events = new Vector<Event>();
			BufferedReader br = new BufferedReader(new FileReader(scenariofile));
			String line;
			String[] str;
			Event e = null;
			int epsilon = 1; // a modifier (en float)
			// int i=0;
			while ((line = br.readLine()) != null) {
				str = line.split(" ");
				// i++;
				// JRE 1.7, (ca ne marche pas sur JRE 1.6)
				// switch (str[0].toLowerCase()) {
				// case "send": e = new
				// Event(Integer.parseInt(str[1]),0,Commands.COM_SEND); break;
				// case "delay": e = new
				// Event(0,Integer.parseInt(str[1]),Commands.COM_DELAY); break;
				// case "break": e = new Event(0,0,Commands.COM_BREAK); break;
				// default : e = new Event(0,0,Commands.COM_UNKNOWN);
				// }

				// JRE 1.6
				if (str[0].toLowerCase().equals("send")) {
					e = new Event(Integer.parseInt(str[1]), 0, epsilon++,
							Commands.COM_SEND, Integer.parseInt(str[2]));
				} else if (str[0].toLowerCase().equals("delay")) {
					epsilon = 0;
					e = new Event(0, Integer.parseInt(str[1]), 0,
							Commands.COM_DELAY);
				} else if (str[0].toLowerCase().equals("break"))
					e = new Event(0, 0, 0, Commands.COM_BREAK);
				else
					e = new Event(0, 0, 0, Commands.COM_UNKNOWN);

				e.setDevice(getDevice());
				e.setDevicesimulator(this);
				e.setSimulation(getSimulator());
				events.add(events.size(), e);
			}
			// JOptionPane.showMessageDialog(null, i);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void init(Simulation simulator) {

		try {
			rbr = new PrintStream(new FileOutputStream(
					Project.getResultFileFromName(getResultFile())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.simulator = simulator;
		if (scenarioAssigned()) {
			loadScenario();

			eventsNumber = events.size();
			horloge = 0;
			currentEvent = null;
			if (eventsNumber > 0) {
				Event First = getNextEvent2();
				if (First.getEventType() != Commands.COM_BREAK) {
					horloge = First.getEventDate();
					currentEvent = new Event(First.getMessage(), horloge,
							First.getEpsilon(), First.getEventType());
					currentEvent.setDevice(getDevice());
					currentEvent.setDevicesimulator(this);
					currentEvent.setSimulation(getSimulator());
					simulator.scheduler.addEvent(currentEvent);
				}
			}
		}
	}

	public void start() {
		if (currentEvent != null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void end() {
		rbr.close();
	}

	@Override
	public void run() {
		Event Next = getNextEvent2();
		while (Next.getEventType() != Commands.COM_BREAK) {
			lock.P();
			// if(((System.nanoTime() - simulator.startTime) / (simulator.pas *
			// 1000000)) >= pas){
			// pas++;
			// rbr.println(""+(System.nanoTime() -
			// simulator.startTime)+"  "+device.getId()+"  "+device.getUserid()+"  "+device.getNivBattery());
			// }
			horloge += Next.getEventDate();
			currentEvent.upDate(Next.getMessage(), horloge, Next.getEpsilon(),
					Next.getEventType(), Next.getPowerRatio());
			simulator.semaphore.V();
			Next = getNextEvent2();

		}
		lock.P();
		simulator.scheduler.removeEvent(currentEvent);
		simulator.semaphore.V();
		System.out.println(" -> " + getDevice().getUserId());
	}

	public Event getNextEvent() {
		return events.get(0);
	}

	public Event getNextEvent2() {
		int accumulated_delay = 0;

		Event Next = getNextEvent();
		while (Next.getEventType() == Commands.COM_DELAY) {
			accumulated_delay += Next.getEventDate();
			events.remove(0);
			events.add(eventsNumber - 1, Next);
			Next = getNextEvent();
		}

		eps = eps + accumulated_delay + 1;
		Next.setEventDate(eps);
		events.remove(0);
		events.add(eventsNumber - 1, Next);
		return Next;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}
