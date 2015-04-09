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
	private String arg1 = "" ;
	private String arg2 = "" ;
	private int destination ;
	
	public Command() {
		this(CommandType.SEND, "0");
	}
	
	public Command(int commandType) {
		this.commandType = commandType ;
		this.arg1 = "" ;
		this.arg2 = "" ;
	}
	
	public Command(int commandType, int arg) {
		this.commandType = commandType ;
		this.arg1 = ""+arg ;
		this.arg2 = "" ;
	}
	
	public Command(int commandType, int arg1, int arg2) {
		this.commandType = commandType ;
		this.arg1 = ""+arg1 ;
		this.arg2 = ""+arg2 ;
	}
	
	public Command(int commandType, String arg) {
		this.commandType = commandType ;
		this.arg1 = arg ;
		this.arg2 = "" ;
	}
	
	public Command(int commandType, String arg1, String arg2) {
		this.commandType = commandType ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}
	
	public int getCommandType() {
		return commandType;
	}
	
	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}
	
	public String getArg1() {
		return arg1;
	}
	
	public void setArg1(String arg1) {
		this.arg1 = arg1 ;
	}
	
	public int getIntOfArg1() {
		return Integer.parseInt(arg1);
	}
	
	public void setArg1(int arg) {
		this.arg1 = String.valueOf(arg);
	}
	
	public String getArg2() {
		return arg2;
	}
	
	public int getIntOfArg2() {
		return Integer.parseInt(arg2);
	}
	
	public void setArg2(String arg2) {
		this.arg2 = arg2 ;
	}
	
	public void set2(int arg) {
		this.arg2 = String.valueOf(arg);
	}
	
//	public int getEvent() {
//		if (commandType == CommandType.PSEND) return Integer.parseInt(arg1);
//		if (commandType == CommandType.DELAY) return Integer.parseInt(arg1);
//		if (commandType == CommandType.SEND) return arg1.length();
//		if (commandType == CommandType.READ) return 127;
//		if (commandType == CommandType.WAIT) return Integer.MAX_VALUE;		
//		if (commandType == CommandType.VAR) return 0 ;
//		
//		return Integer.MAX_VALUE;
//	}
	
	public int getDestination() {
		return destination;
	}
	
	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	@Override
	public String toString() {
		return CommandType.getCommandName(commandType)+"\t"+arg1;
	}

}
