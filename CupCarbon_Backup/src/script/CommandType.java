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
	public static final int BREAK = 3 ;
	
	public static String getCommandName(int i) {
		if(i==0) return "DELAY";
		if(i==1) return "PSEND";
		if(i==2) return "SEND";
		if(i==3) return "BREAK";
		return "";
	}
	
}
