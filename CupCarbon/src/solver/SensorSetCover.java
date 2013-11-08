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
import graph.GraphStd;
import graph.Vertex;
import graph.VertexStd;

import java.util.List;
import java.util.ListIterator;

import map.Layer;
import device.Device;
import device.DeviceList;

public class SensorSetCover {

	public static GraphStd toSensorGraph(List<Device> nodes, int size) {
		Device n1 = null;
		Device n2 = null;
		double distance = 0;
		GraphStd graphe = new GraphStd();
		int i = 0;
		int j = 0;
		ListIterator<Device> iterator = nodes.listIterator();
		ListIterator<Device> iterator2;
		while (iterator.hasNext()) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				// System.out.println(n1.getNodeName()+" : "+i);
				graphe.add(new VertexStd(i++, n1.getNodeIdName()));
				n1.setAlgoSelect(false);
			}
		}

		iterator = nodes.listIterator();
		i = 0;
		while (iterator.hasNext() && iterator.nextIndex() < size - 1) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				iterator2 = nodes.listIterator(iterator.nextIndex());
				j = i + 1;
				while (iterator2.hasNext()) {
					n2 = iterator2.next();
					if (n1.radioDetect(n2) && n2.getType() == Device.SENSOR) {
						distance = n1.distance(n2);
						graphe.get(i).ajouterVoisin(graphe.get(j), distance);
						graphe.get(j).ajouterVoisin(graphe.get(i), distance);
					}
					j++;
				}
				i++;
			}
		}
		return graphe;
	}

	public static Graph toSensorTargetGraph(List<Device> nodes, int size) {
		Device n1 = null;
		Device n2 = null;
		// double distance = 0;
		Graph graphe = new Graph();
		int i = 0;
		int j = 0;
		ListIterator<Device> iterator = nodes.listIterator();
		ListIterator<Device> iterator2;
		while (iterator.hasNext()) {
			n1 = iterator.next();
			if (n1.getType() == Device.SENSOR) {
				// System.out.println(n1.getNodeName()+" : "+i);
				graphe.add(new Vertex(i++, n1.getNodeIdName()));
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
					if (n1.detection(n2) && n2.getType() != Device.SENSOR) {
						// distance = n1.distance(n2);
						graphe.get(i).addNeighbor(j);
					}
					j++;
				}
				i++;
			}
		}
		return graphe;
	}

	public static void sensorSetCover() {
		GraphStd graphe = null;
		List<Device> nodes = DeviceList.getNodes();
		graphe = toSensorGraph(nodes, DeviceList.size());
		//
		// graphe.afficher();
		// L'algorithme
		// Séparer les noeuds isolés
		int ng = graphe.size();
		for (int i = 0; i < ng; i++) {
			if (graphe.get(i).getNbVoisins() == 0) {
				graphe.supprimer(i);
				i--;
				ng--;
			}
		}
		// int nbCouvrants = 0 ;
		int max, imax;
		while (graphe.size() > 0) {
			// nbCouvrants++ ;
			max = graphe.get(0).getNbVoisins();
			imax = 0;
			for (int i = 1; i < graphe.size(); i++) {
				if (max < graphe.get(i).getNbVoisins()) {
					max = graphe.get(i).getNbVoisins();
					imax = i;
				}
			}
			nodes.get(graphe.get(imax).getNumber()).setAlgoSelect(true);
			graphe.supprimerAvecVoisins(imax);
		}
		// System.out.println("Nombre de capteurs couvrants = "+nbCouvrants+"/"+n);
		// int k = 0 ;
		// for(int i=0; i<capteurs.size(); i++) {
		// if(capteurs.get(i).isCouvrant())
		// System.out.println(++k+" : "+i);
		// }
		// return nbCouvrants;
		Layer.getMapViewer().repaint();
	}

	public static void sensorTargetSetCover() {
		Graph graphe = null;
		List<Device> nodes = DeviceList.getNodes();
		graphe = toSensorTargetGraph(nodes, DeviceList.size());
		int ng = graphe.size();
		for (int i = 0; i < ng; i++) {
			if (graphe.get(i).getNbNeignbors() == 0) {
				graphe.remove(i);
				i--;
				ng--;
			}
		}
		int max, imax;
		while (graphe.size() > 0) {
			max = graphe.get(0).getNbNeignbors();
			imax = 0;
			for (int i = 1; i < graphe.size(); i++) {
				if (max < graphe.get(i).getNbNeignbors()) {
					max = graphe.get(i).getNbNeignbors();
					imax = i;
				}
			}
			nodes.get(graphe.get(imax).getNumber()).setAlgoSelect(true);
			graphe.removeWithNeighbors(imax);
		}
		Layer.getMapViewer().repaint();
	}

}
