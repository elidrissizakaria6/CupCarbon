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

import wisen_simulation.SimLog;
import device.DeviceList;
import device.SensorNode;

public class Script {

	protected LinkedList<Command> commands = new LinkedList<Command>();
	protected SensorNode sensor = null;
	protected int index = 0;
	protected int loopIndex = 0;
	protected boolean waiting = false;
	protected int event = Integer.MAX_VALUE-1;
	
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

		if(getCurrent().getCommandType() == CommandType.VAR) {
			SimLog.add("S"+sensor.getId()+" Set "+getCurrent().getArg1()+"="+getCurrent().getArg2());
			sensor.addVariable(""+getCurrent().getArg1(),""+getCurrent().getArg2());			
			event = 0;
		}
		
		if(getCurrent().getCommandType() == CommandType.DELAY) {
			SimLog.add("S"+sensor.getId()+" Delay of "+(getCurrent().getIntOfArg1()/250)+" milliseconds");
			event = Integer.parseInt(getCurrent().getArg1());						
		}
		
		if(getCurrent().getCommandType() == CommandType.LOOP) {
			SimLog.add("S"+sensor.getId()+" Starts the loop section.");
			loopIndex = index+1 ;			
		}
		
		if(getCurrent().getCommandType() == CommandType.SEND) {			
			String message = getCurrent().getArg1();
			if(message.charAt(0)=='$')
				message = sensor.getVariableValue(message.substring(1));
			event = message.length();
			
			if(getCurrent().getArg2().equals("*")) {
				SimLog.add("S"+sensor.getId()+" Sends the message : \""+message+"\" to the nodes : ");
				for(SensorNode snode : sensor.getSensorNodeNeighbors()) {
					if(sensor.radioDetect(snode) && !snode.isDead()) {
						SimLog.add("  -> S"+snode.getId()+" ");
						snode.setMessage(new String(message));	
					}
				}
			}
			else {
				int destNodeId = getCurrent().getIntOfArg2();
				SensorNode snode = DeviceList.getSensorNodeById(destNodeId);
				if(sensor.radioDetect(snode) && !snode.isDead()) {
					SimLog.add("S"+sensor.getId()+" Sends the message : \""+message+"\" to S"+snode.getId());
					snode.setMessage(message);	
				}
			}
		}
		
		if(getCurrent().getCommandType() == CommandType.READ) {
			event = sensor.readMessage(getCurrent().getArg1());	
		}
		
		if(getCurrent().getCommandType() == CommandType.WAIT) {			
			if(sensor.dataAvailable()) {
				SimLog.add("S"+sensor.getId()+" Buffer available, exit waiting.");
				waiting = false ;
				event = sensor.getDataSize();
			}
			else {
				SimLog.add("S"+sensor.getId()+" is waiting ...");
				event = Integer.MAX_VALUE-1;
				waiting = true;
			}
			sensor.verifyData();
		}
		
		if(getCurrent().getCommandType() == CommandType.BREAK) {
			SimLog.add("S"+sensor.getId()+" BREAK !");
			waiting = true;
		}
		
		if(getCurrent().getCommandType() == CommandType.ADD) {
			String k = getCurrent().getArg1();
			String v1 = getCurrent().getArg1();
			String v2 = getCurrent().getArg2();
			if(k.charAt(0)=='$')
				k = k.substring(1);
			if(v1.charAt(0)=='$')
				v1 = sensor.getVariableValue(v1.substring(1));			
			if(v2.charAt(0)=='$')
				v2 = sensor.getVariableValue(v2.substring(1));
			int z = Integer.valueOf(v1) + Integer.valueOf(v2) ;
			
			SimLog.add("S"+sensor.getId()+" "+k+"="+k+"+"+Integer.valueOf(v2)+" -> "+z);
			sensor.addVariable(k,""+z);		
			event = 0;
		}
		
	}	
	
	public int getEvent() {
		return event;
	}	
	
}
