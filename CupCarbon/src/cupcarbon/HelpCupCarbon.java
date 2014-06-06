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

package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class HelpCupCarbon extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpCupCarbon frame = new HelpCupCarbon();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HelpCupCarbon() {
		setClosable(true);
		setBounds(100, 100, 532, 520);
		JTextArea txtrAMenu = new JTextArea();
		txtrAMenu.setBackground(Color.BLACK);
		txtrAMenu.setForeground(Color.ORANGE);
		JScrollPane scrollPane = new JScrollPane(txtrAMenu);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		//getContentPane().add(txtrAMenu, BorderLayout.NORTH);
		txtrAMenu.setFont(new Font("Courier New", Font.BOLD, 12));
		txtrAMenu.setEditable(false);
		txtrAMenu.setColumns(30);
		txtrAMenu.setWrapStyleWord(true);
		txtrAMenu.setLineWrap(true);
		txtrAMenu.setRows(10);
		txtrAMenu
				.setText("Click : Select/Deselect and add objects\n 1 : Add Sensors\n 2 : Add Gasses\n 3 : Add Insects\n 4 : Add Routers\n 5 : Add Base Stations\n 6 : Add Mobile\n 7 : Add Mobile with radio\n 8 : Add Markers\n 9 : Add Vertex (Graph)\n a + [Apple or Windows] : Select all\n z : Deselect all\n e : Display radii\n r : Hide radii\n t : Transform selected markers to sensors\n u : Inserts a marker after the selected marker\n i : Invert selection\n q : Stop simulation for the selected objects\n s : Start simulation for the selected objects\n g : Display name and some details of all nodes\n f : Hide name and some details of all nodes\n h : Hide/Hide-all/Display selected nodes (if the node is not completely displayed, it can not be moved)\n j : Display all selected nodes\n l : Stop moving selected nodes\n m : Moving selected nodes\n w : Select nodes by classes (types)\n c : Duplicate selected nodes\n v : Display/Hide radio links between nodes or markers\n A : Display/Hide arrows of the markers\n b : Display/Hide detection links between sensors and targets\n x : Display/Hide distances between liked nodes\n , : Reduce radiuses of the selected nodes\n ; : Increase radiuses of the selected nodes\n - : Reduce radio radiuses of the selected nodes (with radio)\n + : Increase radio radiuses of the selected nodes (with radio)\n ( : Reduce radiuses of the capture unit of the selected nodes\n ) : Increase radiuses of the capture unit of the selected nodes\n Shift+drag : Multiple selection\n t : Add edges oriented from a source vertex to a target vertex\n y : Add edges oriented from a target vertex to a source vertex\n p : Mark a vertex as a bus station\n ");

	}

}
