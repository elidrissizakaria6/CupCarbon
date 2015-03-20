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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Ahcene Bounceur
 * @author Massinissa Lounis
 * @author Arezki Laga
 * @version 1.0
 * 
 *          Load an OpenCL script from a file
 */
public final class OpenCLScriptLoader {

	// ------------------------------------------------------------
	// Load an OpenCL script from the file fileName
	// ------------------------------------------------------------
	/**
	 * @param fileName
	 * @return String of the script
	 * 
	 *         Load an OpenCL script from the file fileName
	 */
	public static String loadScriptFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	// ------------------------------------------------------------
	// Load the OpenCL script of the consumption method
	// ------------------------------------------------------------
	/**
	 * @return String of the script
	 * 
	 * Load the OpenCL script of the consumption method
	 */
	public static String loadConsumptionOCLScript() {
		return loadScriptFile("opencl_scripts/consumption.cl");
	}

	// ------------------------------------------------------------
	// Load the OpenCL script of the next instruction method
	// ------------------------------------------------------------
	/**
	 * @return String of the script
	 * 
	 * Load the OpenCL script of the next instruction method
	 */
	public static String loadNextInstructionOCLScript() {
		return loadScriptFile("opencl_scripts/nextinstruction.cl");
	}

	// ------------------------------------------------------------
	// Load the OpenCL script of the stop condition method
	// ------------------------------------------------------------
	/**
	 * @return String of the script
	 * 
	 * Load the OpenCL script of the stop condition method
	 */
	public static String loadStopConditionOCLScript() {
		return loadScriptFile("opencl_scripts/stopcondition.cl");
	}
}
