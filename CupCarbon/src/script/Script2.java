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

public class Script2 {

	private Command curr;
	private static Iterator<Command> iterator;
	private static ArrayList<Command> commands = new ArrayList<Command>();

	public Script2() {
		iterator = null;
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

	@Override
	public String toString() {
		String s = "";
		Iterator<Command> it = commands.iterator();
		while (it.hasNext()) {
			s += it.next()+"\n";
		}
		return s ;
	}

}
