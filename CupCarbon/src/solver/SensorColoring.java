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

package solver;

import graph.GraphStd;

import java.util.List;

import map.Layer;
import device.Device;
import device.DeviceList;


/**
 * @author Ahcene Bounceur
 * @author Nadir Detti
 * @version 1.0
 */
public class SensorColoring {

	public void executeColoring() {
		GraphStd graphe = null;
		
		List<Device> nodes = DeviceList.getNodes();
		
		graphe = SensorGraph.toSensorGraph(nodes, DeviceList.size());
		
		int c=0;
		for (int i = 0; i < graphe.size(); i++) {
			nodes.get(graphe.get(i).getNumber()).setChannel(c++);
		}
		Layer.getMapViewer().repaint();
	}
	
}
