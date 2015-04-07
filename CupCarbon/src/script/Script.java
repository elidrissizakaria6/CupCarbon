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

package script;

import java.util.Iterator;
import java.util.LinkedList;

import device.DeviceList;
import device.SensorNode;

public class Script {

	//protected Command curr;
	//protected Iterator<Command> iterator;
	protected LinkedList<Command> commands = new LinkedList<Command>();
	protected SensorNode sensor = null;
	protected int index = 0;
	protected int loopIndex = 0;
	protected boolean waiting = false;

	public Script(SensorNode sensor) {
		//iterator = null;
		index = 0;
		this.sensor = sensor ;
	}

	public void add(int commandType) {
		Command c = new Command(commandType);
		commands.add(c);
	}
	
	public void add(int commandType, int arg) {
		Command c = new Command(commandType, arg);
		commands.add(c);
	}
	
	public void add(int commandType, String arg) {
		Command c = new Command(commandType, arg);
		commands.add(c);
	}
	
	public void add(int commandType, int s1, int s2) {
		Command c = new Command(commandType, s1, s2);
		commands.add(c);
	}
	
	public void add(int commandType, String s1, String s2) {
		Command c = new Command(commandType, s1, s2);
		commands.add(c);
	}

	public void add(Command command) {
		Command c = new Command();
		c.setCommandType(command.getCommandType());
		c.setArg1(command.getArg1());
		commands.add(c);
	}

	public void next() {
		//if (!iterator.hasNext())
		//	iterator = commands.iterator();
		//if (curr.getCommandType() != CommandType.BREAK)
		//	curr = iterator.next();
		
		index++;
		if(index >= commands.size())
			index = loopIndex;
		//return curr;
		//return commands.get(index);
	}
	
	public void init() {
		index = -1 ;
		//iterator = commands.iterator();
		//curr = iterator.next();
	}
	
	public Command getCurrent() {
		return commands.get(index);
	}

	@Override
	public String toString() {
		String s = "";
		Iterator<Command> it = commands.iterator();
		while (it.hasNext()) {
			s += it.next()+"\n";
		}
		return s ;
	}
	
	public void execute() {
		if(!waiting) {
			next();			
		}
		waiting = false ;
		Command com = getCurrent();
		if(com.getCommandType() == CommandType.VAR) {
			sensor.addVariable(""+com.getArg1(),""+com.getArg2());
			execute();
		}
		
		if(com.getCommandType() == CommandType.LOOP) {
			loopIndex = index+1 ;
			execute();
		}
		
		if(com.getCommandType() == CommandType.SEND) {
			String message = com.getArg1();
			int destNodeId = com.getIntOfArg2();
			SensorNode snode = DeviceList.getSensorNodeById(destNodeId);
			if(sensor.radioDetect(snode))
				snode.setMessage(message);
		}
		
		if(com.getCommandType() == CommandType.READ) {
			sensor.readMessage(com.getArg1());
		}
		
		if(com.getCommandType() == CommandType.WAIT) {
			loopIndex = index+1 ;
			execute();
//			waiting = true;
//			if(sensor.dataAvailable()) {
//				waiting = false ;				
//			}
		}
	}
	
	public int getEvent() {
		return getCurrent().getEvent();
	}
	

	/*public static void main(String [] arts) {
		Script script = new Script(null);		
		script.add(CommandType.VAR, "x", "1");
		script.add(CommandType.VAR, "y", "5");
		script.add(CommandType.PSEND, 1000);
		script.add(CommandType.DELAY, 500);
		script.add(CommandType.LOOP);
		script.add(CommandType.PSEND, 2000);
		script.add(CommandType.DELAY, 800);		
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
		script.execute();
		System.out.println(script.getCurrent());
	}*/
	
}
