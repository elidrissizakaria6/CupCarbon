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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import project.Project;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class ScriptComWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtFileName;
	static public JComboBox txtLoadFileName;
	private JTextArea txtArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScriptComWindow frame = new ScriptComWindow();
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
	public ScriptComWindow() {
		super("Com File Name");

		this.setName("COM");
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		setBounds(300, 30, 800, 600);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JLabel("<html>&nbsp;<br/><html>"),
				BorderLayout.NORTH);

		JPanel PWZ = new JPanel(new BorderLayout());
		PWZ.add(new JLabel("<html>&nbsp;&nbsp;Laod file<html>"),
				BorderLayout.NORTH);

		JPanel PWest = new JPanel(new BorderLayout());
		PWest.add(new JLabel("<html>&nbsp;&nbsp;File name<html>"),
				BorderLayout.NORTH);
		PWest.add(
				new JLabel("<html>&nbsp;&nbsp;Instructions&nbsp;&nbsp;<html>"),
				BorderLayout.CENTER);
		PWZ.add(PWest, BorderLayout.CENTER);
		getContentPane().add(PWZ, BorderLayout.WEST);

		Border loweredetched = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		txtFileName = new JTextField();
		txtFileName.setBorder(loweredetched);

		txtLoadFileName = new JComboBox();

		txtLoadFileName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtLoadFileName.getSelectedIndex() > 0) {
					txtFileName.setText(txtLoadFileName.getSelectedItem()
							.toString());
					txtArea.setText("");
					try {

						FileInputStream in = new FileInputStream(new File(
								Project.getComFileFromName(txtLoadFileName
										.getSelectedItem().toString())));
						byte[] bytes = new byte[in.available()];
						in.read(bytes);
						txtArea.setText(new String(bytes));
						in.close();

						// BufferedReader br = new BufferedReader(new
						// FileReader(Project.getComFileFromName(txtLoadFileName.getSelectedItem().toString())));
						// String line;
						// String txt;
						// while ((line = br.readLine()) != null) {
						// txt +=
						// }

					} catch (Exception e1) {

					}

				} else {
					txtArea.setText("");
					txtFileName.setText("");
				}
			}
		});

		txtArea = new JTextArea();
		txtArea.setBorder(loweredetched);

		JPanel PCZ = new JPanel(new BorderLayout());
		JPanel PCenter = new JPanel(new BorderLayout());
		PCenter.add(txtArea, BorderLayout.CENTER);
		PCenter.add(txtFileName, BorderLayout.NORTH);
		PCZ.add(txtLoadFileName, BorderLayout.NORTH);
		PCZ.add(PCenter, BorderLayout.CENTER);
		getContentPane().add(PCZ, BorderLayout.CENTER);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
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
					txtFileName.setText(fc.getSelectedFile().toString());
			}
		});

		JPanel PSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton Save = new JButton("<html>Save  </html>");
		Save.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					PrintStream ps;
					ps = new PrintStream(
							new FileOutputStream(Project
									.getComFileFromName(Project
											.getComFileExtension(txtFileName
													.getText()))));
					ps.print(txtArea.getText());
					ps.close();
					txtArea.setText("");
					txtFileName.setText("");

					File comFiles = new File(Project.getProjectComPath());
					String[] c = comFiles.list();
					txtLoadFileName.removeAllItems();
					txtLoadFileName.addItem("New scenario ...");
					for (int i = 0; i < c.length; i++) {
						txtLoadFileName.addItem(c[i]);
					}

					JOptionPane.showMessageDialog(null, "File saved !");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		Save.setIcon(new ImageIcon(Parameters.IMGPATH + "Enregistrer.png"));
		PSouth.add(Save);
		getContentPane().add(PSouth, BorderLayout.SOUTH);

		setVisible(false);
	}

}
