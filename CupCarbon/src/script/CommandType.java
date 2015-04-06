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

public final class CommandType {
	
	public static final int DELAY = 0 ;
	public static final int PSEND = 1 ;
	public static final int SEND = 2 ;
	public static final int READ = 3 ;
	public static final int BREAK = 4 ;
	public static final int WAIT = 5 ;
	public static final int VAR = 6 ;
	public static final int AT = 7 ;
	
	//-- AT
	public static final int CH = 10 ;
	public static final int ID = 11 ;	
	public static final int NI = 12 ;
	public static final int SH = 13 ;
	public static final int SL = 14 ;
	public static final int DH = 15 ;
	public static final int DL = 16 ;
	public static final int MY = 17 ;
	
	
	public static String getCommandName(int i) {
		if(i==DELAY) return "DELAY";
		if(i==PSEND) return "PSEND";
		if(i==SEND) return "SEND";
		if(i==BREAK) return "BREAK";
		if(i==WAIT) return "WAIT";
		if(i==VAR) return "VAR";
		return "";
	}
	
}
