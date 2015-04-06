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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import project.Project;
import synchronization.MehdiLock;
import device.Device;

public class MehdiDeviceSimulator implements Runnable {

	// private static int index = 0;
	// private int id;
	
	private String scriptFile = "";
	private String resultFile = "RS";
	public MehdiLock lock;
	private Thread thread = null;
	private MehdiEvent currentEvent;
	private long clock = 0;
	private List<MehdiEvent> events = null;
	private int eventsNumber = 0;
	private MehdiSimulation simulation = null;
	private Device device;
	private int eps = 0;
	private PrintStream rbr = null;

	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile ;
	}
	
	public void saveResult(long date) {
		rbr.println("" + date + " " + device.getId() + " " + device.getUserId()
				+ " " + device.getBatteryLevel());
	}

	public void saveResult2() {
		rbr.println("" + (System.nanoTime() - simulation.getStartTime()) + " "
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

	public MehdiSimulation getSimulator() {
		return simulation;
	}

	public void setSimulator(MehdiSimulation simulator) {
		this.simulation = simulator;
	}

	public MehdiDeviceSimulator() {
		this(null);
	}

	public MehdiDeviceSimulator(Device device) {
		// id = ++index;
		this.setDevice(device);
		lock = new MehdiLock();
	}

	public boolean scriptAssigned() {
		return (scriptFile != "");
	}

	public void loadScript() {

		try {
			events = new Vector<MehdiEvent>();
			BufferedReader br = new BufferedReader(new FileReader(scriptFile));
			String line;
			String[] str;
			MehdiEvent e = null;
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
					e = new MehdiEvent(Integer.parseInt(str[1]), 0, epsilon++,
							MehdiCommands.COM_SEND, Integer.parseInt(str[2]));
				} else if (str[0].toLowerCase().equals("delay")) {
					epsilon = 0;
					e = new MehdiEvent(0, Integer.parseInt(str[1]), 0,
							MehdiCommands.COM_DELAY);
				} else if (str[0].toLowerCase().equals("break"))
					e = new MehdiEvent(0, 0, 0, MehdiCommands.COM_BREAK);
				else
					e = new MehdiEvent(0, 0, 0, MehdiCommands.COM_UNKNOWN);

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

	public void init(MehdiSimulation simulator) {

		try {
			rbr = new PrintStream(new FileOutputStream(
					Project.getResultFileFromName(getResultFile())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		this.simulation = simulator;
		if (scriptAssigned()) {
			loadScript();

			eventsNumber = events.size();
			clock = 0;
			currentEvent = null;
			if (eventsNumber > 0) {
				MehdiEvent First = getNextEvent2();
				if (First.getEventType() != MehdiCommands.COM_BREAK) {
					clock = First.getEventDate();
					currentEvent = new MehdiEvent(First.getMessage(), clock,
							First.getEpsilon(), First.getEventType());
					currentEvent.setDevice(getDevice());
					currentEvent.setDevicesimulator(this);
					currentEvent.setSimulation(getSimulator());
					simulator.getScheduler().addEvent(currentEvent);
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
		MehdiEvent Next = getNextEvent2();
		while (Next.getEventType() != MehdiCommands.COM_BREAK) {
			lock.P();
			// if(((System.nanoTime() - simulator.startTime) / (simulator.pas *
			// 1000000)) >= pas){
			// pas++;
			// rbr.println(""+(System.nanoTime() -
			// simulator.startTime)+"  "+device.getId()+"  "+device.getUserid()+"  "+device.getNivBattery());
			// }
			clock += Next.getEventDate();
			currentEvent.update(Next.getMessage(), clock, Next.getEpsilon(),
					Next.getEventType(), Next.getPowerRatio());
			simulation.getSemaphore().V();
			Next = getNextEvent2();

		}
		lock.P();
		simulation.getScheduler().removeEvent(currentEvent);
		simulation.getSemaphore().V();
		System.out.println(" -> " + getDevice().getUserId());
	}

	public MehdiEvent getNextEvent() {
		return events.get(0);
	}

	public MehdiEvent getNextEvent2() {
		int accumulated_delay = 0;

		MehdiEvent Next = getNextEvent();
		while (Next.getEventType() == MehdiCommands.COM_DELAY) {
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