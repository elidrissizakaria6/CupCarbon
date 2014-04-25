/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

import graph.Graph;
import graph.Vertex;

import java.util.List;
import java.util.ListIterator;

import map.Layer;
import device.Device;
import device.DeviceList;

public class SensorTargetCoverageRun implements Runnable {

	public static Graph toSensorTargetGraph(List<Device> nodes, int size) {
		Device n1 = null;
		Device n2 = null;
		Graph graph = new Graph();
		int i = 0;
		int j = 0;
		ListIterator<Device> iterator = nodes.listIterator();
		ListIterator<Device> iterator2;
		while (iterator.hasNext()) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR && n1.getState() == Device.ALIVE) {
				graph.add(new Vertex(i++, n1.getNodeIdName()));
				n1.setAlgoSelect(false);
			}
		}

		iterator = nodes.listIterator();
		i = 0;
		while (iterator.hasNext() && iterator.nextIndex() < size - 1) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				iterator2 = nodes.listIterator();
				j = 0;
				while (iterator2.hasNext()) {
					n2 = iterator2.next();
					if (n1.detection(n2) && n2.getType() != Device.SENSOR
							&& n2.getState() == Device.ALIVE) {
						graph.get(i).addNeighbor(j);
					}
					j++;
				}
				i++;
			}
		}
		return graph;
	}

	public static void sensorTargetSetCover() {
		Graph graph = null;
		List<Device> nodes = DeviceList.getNodes();
		graph = toSensorTargetGraph(nodes, DeviceList.size());
		int ng = graph.size();
		for (int i = 0; i < ng; i++) {
			if (graph.get(i).getNbNeignbors() == 0) {
				graph.remove(i);
				i--;
				ng--;
			}
		}
		int max, imax;
		while (graph.size() > 0) {
			max = graph.get(0).getNbNeignbors();
			imax = 0;
			for (int i = 1; i < graph.size(); i++) {
				if (max < graph.get(i).getNbNeignbors()) {
					max = graph.get(i).getNbNeignbors();
					imax = i;
				}
			}
			nodes.get(graph.get(imax).getNumber()).setAlgoSelect(true);
			graph.removeWithNeighbors(imax);
		}
		Layer.getMapViewer().repaint();
	}

	public void start() {
		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		while (true) {
			sensorTargetSetCover();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
