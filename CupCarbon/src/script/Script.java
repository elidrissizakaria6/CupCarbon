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

import java.util.ArrayList;
import java.util.Iterator;

import device.SensorNode;

public class Script {

	private Command curr;
	private Iterator<Command> iterator;
	private ArrayList<Command> commands = new ArrayList<Command>();
	protected SensorNode sensor = null;

	public Script(SensorNode sensor) {
		iterator = null;
		this.sensor = sensor ;
	}

	public void add(int commandType, int arg) {
		Command c = new Command(commandType, arg);
		commands.add(c);
	}

	public void add(Command command) {
		Command c = new Command();
		c.setCommandType(command.getCommandType());
		c.setArg1(command.getArg1());
		commands.add(c);
	}

	public Command next() {
		if (iterator == null) {
			iterator = commands.iterator();
			curr = iterator.next();
		} else {
			if (!iterator.hasNext())
				iterator = commands.iterator();
			if (curr.getCommandType() != CommandType.BREAK)
				curr = iterator.next();
		}
		return curr;
	}
	
	public void init() {
		iterator = null;
	}
	
	public Command getCurrent() {
		return curr;
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
	
	public int execute() {
		Command com = next();
		if(com.getCommandType() == CommandType.VAR) {
			sensor.addVariable(""+com.getArg1(),""+com.getArg2());
		}
		return com.getEvent();
	}
	

}
