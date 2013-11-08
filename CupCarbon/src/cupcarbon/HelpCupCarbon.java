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
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
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
		setBounds(100, 100, 532, 300);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollBar scrollBar = new JScrollBar();
		panel.add(scrollBar, BorderLayout.WEST);

		JTextArea txtrAMenu = new JTextArea();
		getContentPane().add(txtrAMenu, BorderLayout.NORTH);
		txtrAMenu.setFont(new Font("Arial", Font.PLAIN, 13));
		txtrAMenu.setEditable(false);
		txtrAMenu.setColumns(30);
		txtrAMenu.setWrapStyleWord(true);
		txtrAMenu.setLineWrap(true);
		txtrAMenu.setRows(10);
		txtrAMenu
				.setText("A. Menu\n1. Add nodes\n1.1. Sensors\n1.2. Gasses\n1.3. Insects\n1.4. Routeurs\n1.5. Base Stations\n1.6. Mobile\n1.7. Mobile with radio\n1.8. Markers\n\n\nB. Touches :\nClick : Select/Deselect\n1 : Add Sensors\n2 : Add Gasses\n3 : Add Insects\n4 : Add Bridges\n5 : Add Base Stations\n6 : Add Mobile\n7 : Add Mobile with radio\n8 : Add Markers\na : Select all\nz : Deselect all\ne : Display radiuses\nr : Hide radiuses\nt : Transform markers to sensors\nu : Divide selected markers\ni : Invert selection\nq : Stop simulation\ns : Start simulation\nf : Display details of all nodes\ng : Hide details of all nodes\nh : Hide/Hide all/Display selected nodes\nj : Display all nodes\nk : Stop moving selected nodes\nm : Moving selected nodes\nw : Select nodes by classes \nc : Duplicate selected nodes\nv : Afficher/Cacher les liens entre les noeuds\nx : Afficher les distances entre les noeuds li\u00E9s\n, : Reduce radiuses of the selected nodes\n; : Increase  radiuses of the selected nodes\n- : Reduce radio radiuses of the selected nodes (with radio)\n+ : Increase  radio radiuses of the selected nodes (with radio)\n( : Reduce radiuses of the capture unit of the selected nodes\n) : Increase  radiuses of the capture unit of the selected nodes\nShift+drag : Multiple selection\n\n\n------\nC. Utilisation :\n1. Ajouter un noeud\n2. Supprimer un ou plusieurs noeuds\n3. S\u00E9lectionner/D\u00E9-s\u00E9lectionner un neoud\n4. S\u00E9lection/D\u00E9-s\u00E9lection multiple\n  4.1. Par la souris\n  4.2. Par la touche CTRL\n  4.3. S\u00E9lectionner/D\u00E9-s\u00E9lectionner tout\n  4.4. S\u00E9lection inverse\n5. S\u00E9lection par cat\u00E9gorie\n6. D\u00E9placer un noeud\n7. D\u00E9placer plusieurs noeuds\n8. Afficher/Cacher les rayons des noeuds\n9. Modifier le rayon d'un ou plusieurs noeuds\n10. Modifier le rayon radio d'un ou plusieurs noeuds\n11. Modifier le rayon de l'unit\u00E9 de capture d'un ou de plusieurs noeuds\n12. Afficher/Cacher les liens entre les noeuds\n13. Afficher/Cacher les distances entre les noeuds\n14. Dupliquer les noeuds\n15. Simuler un ou plusieurs noeuds\n16. Afficher les param\u00E8tres d'un noeud\n17. Modifier les param\u00E8tres d'un noeud\n18. Cr\u00E9er des marqueurs (trac\u00E9)\n19. Cr\u00E9er et enregistrer un parcours GPS\n20. Simuler un mobile et les insectes \u00E0 base d'un trac\u00E9\n21. Transformer un trac\u00E9 en neouds (capteurs)\n22. Cr\u00E9er un nouveau projet\n23. Enregistrer un nouveau projet\n24. Charger un nouveau projet\n25. Quitter un projet\n26. Aide");

	}

}
