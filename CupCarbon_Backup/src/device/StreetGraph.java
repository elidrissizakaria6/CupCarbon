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

package device;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import map.Layer;

public class StreetGraph {

	private static List<StreetVertex> verticesList = new ArrayList<StreetVertex>();
	// private boolean liens = true;
	private static int size = 0;

	public StreetGraph() {
	}

	public static void save(String fileName) {
		try {
			PrintStream fos = new PrintStream(new FileOutputStream(fileName));
			fos.println(verticesList.size());
			for (StreetVertex streetVertex : verticesList) {
				fos.print(streetVertex.getX());
				fos.print(" " + streetVertex.getY());
				fos.print(" " + streetVertex.getRadius());
				fos.println(" " + streetVertex.isBusStation());
			}
			for (StreetVertex streetVertex : verticesList) {
				for (StreetVertex sv : streetVertex.getNeighbors()) {
					fos.print(verticesList.indexOf(sv) + " ");
				}
				fos.println();
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void open(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			String[] str;
			int n = Integer.valueOf(br.readLine());
			for (int i = 0; i < n; i++) {
				line = br.readLine();
				str = line.split(" ");
				addNodeByType(str[0], str[1], str[2], str[3]);
			}
			for (StreetVertex sv : verticesList) {
				line = br.readLine();
				str = line.split(" ");
				for (int i = 0; i < str.length; i++) {
					sv.add(verticesList.get(Integer.parseInt(str[i])));
				}
			}
			br.close();
			Layer.getMapViewer().repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addNodeByType(String... type) {
		add(new StreetVertex(type[0], type[1], type[2], type[3]));
	}

	public static void add(StreetVertex streetVertex) {
		verticesList.add(streetVertex);
		size++;
	}

	public void add(int index, StreetVertex streetVertex) {
		verticesList.add(index, streetVertex);
		size++;
	}

	public void init() {
		for (StreetVertex streetVertex : verticesList)
			streetVertex.init();
	}

	public void dessiner(Graphics g) {
		for (StreetVertex streetVertex : verticesList)
			streetVertex.draw(g);
		// try {
		// for (Iterator<StreetVertex> iterator = verticesList.iterator();
		// iterator.hasNext();) {
		// iterator.next().dessiner(g);
		// }
		// if (liens && verticesList.size() > 0) {
		// double x1 = 0;
		// double y1 = 0;
		// int lx1 = 0;
		// int ly1 = 0;
		// StreetVertex streetVertex;
		// for (Iterator<StreetVertex> iterator = verticesList.iterator();
		// iterator
		// .hasNext();) {
		// streetVertex = iterator.next();
		// x1 = streetVertex.getX();
		// y1 = streetVertex.getY();
		// lx1 = MapCalc.geoToIntPixelMapX(x1, y1);
		// ly1 = MapCalc.geoToIntPixelMapY(x1, y1);
		// }
		// }
		// } catch (Exception e) {
		// }
	}

	public static StreetVertex get(int idx) {
		return verticesList.get(idx);
	}

	public static int getIndex(StreetVertex streetVertex) {
		for (int i = 0; i < verticesList.size(); i++) {
			if (verticesList.get(i) == streetVertex)
				return i;
		}
		return -1;
	}

	public void setLiens(boolean b) {
		// liens = b;
	}

	public static int size() {
		return size;
	}

	public void delete(int idx) {
		StreetVertex streetVertex = verticesList.get(idx);
		Layer.getMapViewer().removeMouseListener(streetVertex);
		Layer.getMapViewer().removeMouseMotionListener(streetVertex);
		Layer.getMapViewer().removeKeyListener(streetVertex);
		verticesList.remove(idx);
		streetVertex = null;
		size--;
	}

	public static void saveGpsCoords(String fileName, String title,
			String from, String to) {
		try {
			PrintStream ps;
			if (fileName.endsWith(".gps"))
				ps = new PrintStream(new FileOutputStream(fileName));
			else
				ps = new PrintStream(new FileOutputStream("gps/" + fileName
						+ ".gps"));
			//
			ps.println(title);
			ps.println(from);
			ps.println(to);
			StreetVertex streetVertex;
			for (Iterator<StreetVertex> iterator = verticesList.iterator(); iterator
					.hasNext();) {
				streetVertex = iterator.next();
				ps.println(streetVertex.getX() + " " + streetVertex.getY());
			}
			ps.close();
			JOptionPane.showMessageDialog(null, "File saved !");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void selectInNodeSelection(int cadreX1, int cadreY1, int cadreX2,
			int cadreY2) {
		StreetVertex streetVertex;
		for (Iterator<StreetVertex> iterator = verticesList.iterator(); iterator
				.hasNext();) {
			streetVertex = iterator.next();
			streetVertex.setMove(false);
			streetVertex.setSelection(false);
			if (Layer.inMultipleSelection(streetVertex.getX(),
					streetVertex.getY(), cadreX1, cadreX2, cadreY1, cadreY2)) {
				streetVertex.setSelection(true);
			}
		}
	}

	public void deleteIfSelected() {
		StreetVertex streetVertex;
		for (Iterator<StreetVertex> iterator = verticesList.iterator(); iterator
				.hasNext();) {
			streetVertex = iterator.next();
			if (streetVertex.isSelected()) {
				Layer.getMapViewer().removeMouseListener(streetVertex);
				Layer.getMapViewer().removeMouseMotionListener(streetVertex);
				Layer.getMapViewer().removeKeyListener(streetVertex);
				streetVertex.removeAll();
				iterator.remove();
				size--;
				streetVertex = null;
			}
		}
	}

	public void setSelectionOfAllVertices(boolean selection, int type,
			boolean addSelect) {
		for (StreetVertex sv : verticesList) {
			if (!addSelect)
				sv.setSelection(false);
			if (sv.getType() == type || type == -1)
				sv.setSelection(selection);
		}
		Layer.getMapViewer().repaint();
	}

	public void invertSelection() {
		for (StreetVertex sv : verticesList) {
			sv.invSelection();
		}
		Layer.getMapViewer().repaint();
	}

	public List<StreetVertex> getGraph() {
		return verticesList;
	}

}
