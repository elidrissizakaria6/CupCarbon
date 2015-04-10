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
	private String arg3 = "" ;
	private String arg4 = "" ;
	private String arg5 = "" ;
	private String arg6 = "" ;
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
	
	public Command(int commandType, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) {
		this.commandType = commandType ;
		this.arg1 = ""+arg1 ;
		this.arg2 = ""+arg2 ;
		this.arg3 = ""+arg3 ;
		this.arg4 = ""+arg4 ;
		this.arg5 = ""+arg5 ;
		this.arg6 = ""+arg6 ;
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
	
	public String getArg3() {
		return arg3;
	}

	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}

	public String getArg4() {
		return arg4;
	}

	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}

	public String getArg5() {
		return arg5;
	}

	public void setArg5(String arg5) {
		this.arg5 = arg5;
	}

	public String getArg6() {
		return arg6;
	}

	public void setArg6(String arg6) {
		this.arg6 = arg6;
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
