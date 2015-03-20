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

package simbox_simulation;

/**
 * @author Ahcene Bounceur
 * @author Massinissa Lounis
 * @author Arezki Laga
 * @version 1.0
 * 
 * The necessary parameters for the simulation
 */
public class SimulationInputs {

	public static boolean discreteEvent = true;
	public static boolean mobility = false;
	public static int step = 1 ;
	public static int energyMax = 0;
	public static int iterNumber = 0;
	public static int nbSensors = 0;

	public static int scriptSize = 0;
	public static int[][][] script;
	public static byte[][] links;
	
	public static int[] gpuScript;
	public static byte[] gpuLinks;
		
	public static byte eRTx = 1;	
	
	public static boolean visual ;
	public static int visualDelay;
	
}
