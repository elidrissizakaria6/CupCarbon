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

public class Command {
	
	private int commandType ;
	private int arg ;
	private int destination ;
	
	public Command() {
		this(CommandType.SEND,0);
	}
	
	public Command(int commandType, int arg) {
		this.commandType = commandType ;
		this.arg = arg ;
	}
	
	public int getCommandType() {
		return commandType;
	}
	
	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}
	
	public int getArg1() {
		return arg;
	}
	
	public void setArg1(int arg) {
		this.arg = arg;
	}
	
	public int getEvent() {
		if (commandType == CommandType.PSEND) return arg;
		if (commandType == CommandType.DELAY) return arg;
		if (commandType == CommandType.WAIT) return 99999999;
		return 0;
	}
	
	public int getDestination() {
		return destination;
	}
	
	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		return CommandType.getCommandName(commandType)+"\t"+arg;
	}
}
