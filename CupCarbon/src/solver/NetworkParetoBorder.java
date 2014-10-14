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
 * @author Ali Benzerbadj
 * @version 1.0
 */
public class NetworkParetoBorder {

	public void execute() {
//		System.out.println("---------------------------");
//		System.out.println("---------------------------");
//		System.out.println("---------------------------");
		// From sensors to Graph
		GraphStd graphe = null;		
		List<Device> nodes = DeviceList.getNodes();		
		graphe = SensorGraph.toSensorGraph(nodes, DeviceList.size());		
		
		for (int i = 0; i < graphe.size(); i++) {
			nodes.get(i).setRank(0);
			nodes.get(i).setAlgoSelect(false);
		}
		
		Device n1, n2 ;
		int rank = 0;
		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getX() < n2.getX()) && (n1.getY() < n2.getY())) {
					rank++;
				}
			}
			n1.setRank(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		int min = 10000000;
		int rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getRank();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getRank()<=min)
				nodes.get(i).setAlgoSelect(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------		
		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getX() > n2.getX()) && (n1.getY() < n2.getY())) {
					rank++;
				}
			}
			n1.setRank(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getRank();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getRank()<=min)
				nodes.get(i).setAlgoSelect(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------				
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getX() > n2.getX()) && (n1.getY() > n2.getY())) {
					rank++;
				}
			}
			n1.setRank(rank);
			System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getRank();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getRank()<=min)
				nodes.get(i).setAlgoSelect(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------		
		for (int i = 0; i < graphe.size(); i++) {
			n1 = nodes.get(i);
			rank = 0;
			for (int j = 0; j < graphe.size(); j++) {
				n2 = nodes.get(j);
				if((n1.getX() < n2.getX()) && (n1.getY() > n2.getY())) {
					rank++;
				}
			}
			n1.setRank(rank);
			//System.out.println(n1.getNodeIdName()+" : "+rank);
		}
		
		min = 10000000;
		rmin = 0;
		for (int i = 0; i < graphe.size(); i++) {
			rmin = nodes.get(i).getRank();
			if(rmin<min) {
				min = rmin; 
			}
		}
		for (int i = 0; i < graphe.size(); i++) {
			if(nodes.get(i).getRank()<=min)
				nodes.get(i).setAlgoSelect(true);
		}
		//System.out.println(min);
		//System.out.println("---------------------------");
		//------------------------------------------------------------
		//------------------------------------------------------------
		//------------------------------------------------------------
		//------------------------------------------------------------
		
		
		
		//------------------------------------------------------------
		// Begin : example 

//		for (int i = 0; i < graphe.size(); i++) {
//			nodes.get(i).setAlgoSelect(true);
//		}
		// End : example
		//------------------------------------------------------------
		
		
		// Update sensors (coloring)
		Layer.getMapViewer().repaint();
	}
	
}
