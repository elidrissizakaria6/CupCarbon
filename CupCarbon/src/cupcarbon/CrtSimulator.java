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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import map.WorldMap;
import project.Project;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class CrtSimulator extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtSimulationName;
	private JTextField txtLogFileName;
	private JComboBox ProtocolSimulation;
	private JTextField txtSimulationDelay;
	private JTextField txtSimulationLogicDelay;
	private JTextField txtSimulationPas;

	// private JPanel Options;
	// private JTextArea txtArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// frame = new ComWindow();
					// frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 */
	public CrtSimulator() {
		super("Create simulation");

		this.setName("SIM");
		setIconifiable(true);
		setClosable(true);
		setResizable(true);

		getContentPane().setLayout(new BorderLayout());

		JPanel PNorth = new JPanel(new BorderLayout());
		PNorth.add(new JLabel("<html>&nbsp;<br/><html>"), BorderLayout.NORTH);
		getContentPane().add(PNorth, BorderLayout.NORTH);

		JPanel PNorthWest = new JPanel(new GridLayout(3, 1, 0, 0));
		PNorth.add(PNorthWest, BorderLayout.WEST);

		PNorthWest.add(new JLabel(
				"<html>&nbsp;&nbsp;Simulation name&nbsp;&nbsp;<html>"));
		PNorthWest.add(new JLabel(
				"<html>&nbsp;&nbsp;Simulation protocol&nbsp;&nbsp;<html>"));
		PNorthWest.add(new JLabel(
				"<html>&nbsp;&nbsp;Log file name&nbsp;&nbsp;<html>"));

		JPanel PNorthCenter = new JPanel(new GridLayout(3, 1, 0, 3));
		PNorth.add(PNorthCenter, BorderLayout.CENTER);

		txtSimulationName = new JTextField();
		PNorthCenter.add(txtSimulationName);

		String [] items = { "", "Standard", "Leach", "Extern plugin" };
		ProtocolSimulation = new JComboBox(items);
		ProtocolSimulation.setBackground(txtSimulationName.getBackground());
		PNorthCenter.add(ProtocolSimulation);

		txtLogFileName = new JTextField();
		PNorthCenter.add(txtLogFileName);

		txtSimulationDelay = new JTextField("360000000");
		txtSimulationLogicDelay = new JTextField("60000");
		txtSimulationPas = new JTextField("3600000");

		// txtSimulationDelay.setMinimumSize(new Dimension(100,100));
		JPanel Opt = new JPanel(new BorderLayout());
		JPanel OptW = new JPanel(new GridLayout(5, 1, 0, 0));
		JPanel OptC = new JPanel(new GridLayout(5, 1, 0, 0));

		Opt.setBackground(Color.RED);

		OptW.add(new JLabel("Simulation delay"));
		OptC.add(txtSimulationDelay);

		OptW.add(new JLabel("Simulation logic delay"));
		OptC.add(txtSimulationLogicDelay);

		OptW.add(new JLabel("Simulation Pas"));
		OptC.add(txtSimulationPas);

		// OptW.add(new JLabel("Simulation delay"));
		// OptC.add(txtSimulationDelay);

		Opt.add(OptW, BorderLayout.WEST);
		Opt.add(OptC, BorderLayout.CENTER);

		JPanel PCenterWest = new JPanel(new GridLayout(1, 1, 0, 3));
		PCenterWest.add(new JLabel("<html>&nbsp;&nbsp;Options<html>"),
				BorderLayout.NORTH);
		getContentPane().add(PCenterWest, BorderLayout.WEST);

		// Border loweredetched =
		// BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		JPanel PCenter = new JPanel();
		PCenter.add(Opt, BorderLayout.NORTH);
		// PCenter.setBackground(Color.RED);
		getContentPane().add(PCenter, BorderLayout.CENTER);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtSimulationName.setText(Project
						.getGpsFileExtension(txtSimulationName.getText()));
			}
		});
		button_1.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));

		JButton button = new JButton("");

		button.setIcon(new ImageIcon(Parameters.IMGPATH + "Ouvrir.png"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".com"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "COM files";
					}
				};

				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ff);
				int val = fc.showDialog(fc, "Save");
				if (val == 0)
					txtSimulationName.setText(fc.getSelectedFile().toString());
			}
		});

		JPanel PSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton Cancel = new JButton("Cancel");
		// CrtSimulator this_ = this;
		Cancel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// setVisible(false);
			}
		});

		PSouth.add(Cancel);

		JButton Save = new JButton(
				"<html>&nbsp;&nbsp;&nbsp; Create &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html>");
		Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		Save.addMouseListener(new MouseListener() {

			// String name = txtSimulationName.getText();
			// String log = txtLogFileName.getText();
			// long V1 = Long.parseLong(txtSimulationDelay.getText(), 10);
			// long V2 = Long.parseLong(txtSimulationLogicDelay.getText(), 10);
			// long V3 = Long.parseLong(txtSimulationPas.getText(), 10);

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if ((txtSimulationName.getText().equals(""))
						|| (txtLogFileName.getText().equals(""))) {
					JOptionPane
							.showMessageDialog(null,
									"Veillez préciser le nom de la simulation et du fichier log svp !  ");
				} else {
					if (ProtocolSimulation.getSelectedIndex() == 0) {
						JOptionPane
								.showMessageDialog(null,
										"Veillez préciser la méthode de simulation svp ");
					} else if (ProtocolSimulation.getSelectedIndex() == 0) {
						JOptionPane
								.showMessageDialog(null,
										"Veillez préciser la méthode de simulation svp ");
					} else if (ProtocolSimulation.getSelectedIndex() > 1) {
						JOptionPane
								.showMessageDialog(null,
										"Cette version ne prend pas en compte cette méthode ");
					} else {

						JOptionPane.showMessageDialog(null,
								"Simulation created ! ");
//						if (CupCarbon.simulationNumber == 0) {
//							CupCarbon.simulations.add(new JSeparator(), 0);
//						}

						JMenuItem mntmComSimulate = new JMenuItem(
								txtSimulationName.getText());
						mntmComSimulate.addActionListener(new ActionListener() {
							String name = txtSimulationName.getText();
							String log = txtLogFileName.getText();
							long V1 = Long.parseLong(
									txtSimulationDelay.getText(), 10);
							long V2 = Long.parseLong(
									txtSimulationLogicDelay.getText(), 10);
							long V3 = Long.parseLong(
									txtSimulationPas.getText(), 10);

							public void actionPerformed(ActionEvent e) {
								WorldMap.comSimulate(name, log, V1, V2, V3);
							}
						});
						mntmComSimulate.setIcon(new ImageIcon(Parameters.IMGPATH
								+ "flag_green.png"));
//						CupCarbon.simulations.add(mntmComSimulate, 0);
						CupCarbon.simulationNumber++;

						txtSimulationName.setText("");
						txtLogFileName.setText("");
						ProtocolSimulation.setSelectedIndex(0);

					}
				}

			}
		});
		Save.setIcon(new ImageIcon(Parameters.IMGPATH + "Enregistrer.png"));
		PSouth.add(Save);
		getContentPane().add(PSouth, BorderLayout.SOUTH);

		setVisible(false);
	}

}
