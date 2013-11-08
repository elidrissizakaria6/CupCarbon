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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import project.Project;
import device.DeviceList;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class NodeParametersWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	public static JTextField textField_5;
	public static JTextField textField_6;
	public static JTextField textField_7;
	public static JTextField textField_8;
	public static JTextField textField_9;
	public static JComboBox gpsPathNameComboBox;
	public static JComboBox scriptComComboBox;

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NodeParametersWindow frame = new NodeParametersWindow();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NodeParametersWindow() {
		setClosable(true);
		setIconifiable(true);
		setTitle("Node Parameters");
		setBounds(100, 100, 525, 287);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Apply");
		btnNewButton.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.updateFromMap(textField_5.getText(),
						textField_6.getText(), textField_7.getText(),
						textField_8.getText(), textField_9.getText(),
						Project.getProjectGpsPath() + Parameters.SEPARATOR
								+ gpsPathNameComboBox.getSelectedItem());
			}
		});
		panel_2.add(btnNewButton);

		JPanel panel_8 = new JPanel();
		getContentPane().add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.WEST);
		panel_9.setLayout(new GridLayout(7, 1, 0, 0));

		JLabel label = new JLabel("Latitude");
		label.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label);

		JLabel label_1 = new JLabel("Longitude");
		label_1.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_1);

		JLabel label_2 = new JLabel("Radius");
		label_2.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_2);

		JLabel label_3 = new JLabel("Radio Radius");
		label_3.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_3);

		JLabel label_4 = new JLabel("Capture Unit Radius");
		label_4.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(label_4);

		JLabel lblcom = new JLabel("COM File");
		lblcom.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblcom);

		JLabel lblGpsFile = new JLabel("GPS File");
		lblGpsFile.setFont(new Font("Arial", Font.PLAIN, 11));
		panel_9.add(lblGpsFile);

		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new GridLayout(7, 1, 0, 0));

		textField_5 = new JTextField();
		panel_10.add(textField_5);
		textField_5.setColumns(10);

		textField_6 = new JTextField();
		panel_10.add(textField_6);
		textField_6.setColumns(10);

		textField_7 = new JTextField();
		panel_10.add(textField_7);
		textField_7.setColumns(10);

		textField_8 = new JTextField();
		panel_10.add(textField_8);
		textField_8.setColumns(10);

		textField_9 = new JTextField();
		panel_10.add(textField_9);
		textField_9.setColumns(10);

		scriptComComboBox = new JComboBox();
		panel_10.add(scriptComComboBox);

		gpsPathNameComboBox = new JComboBox();
		panel_10.add(gpsPathNameComboBox);

		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.EAST);
		panel_11.setLayout(new GridLayout(7, 1, 0, 0));

		JPanel panel_12 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_12.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_11.add(panel_12);

		JButton button_10 = new JButton("");
		button_10.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bPrev.png"));
		panel_12.add(button_10);

		JButton button_11 = new JButton("");
		button_11.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bNext.png"));
		panel_12.add(button_11);

		JPanel panel_13 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_13.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel_11.add(panel_13);

		JButton button_12 = new JButton("");
		button_12.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bPrev.png"));
		panel_13.add(button_12);

		JButton button_13 = new JButton("");
		button_13.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bNext.png"));
		panel_13.add(button_13);

		JPanel panel_14 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_14.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel_11.add(panel_14);

		JButton button_14 = new JButton("");
		button_14.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bPrev.png"));
		panel_14.add(button_14);

		JButton button_15 = new JButton("");
		button_15.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bNext.png"));
		panel_14.add(button_15);

		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_15.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		panel_11.add(panel_15);

		JButton button_16 = new JButton("");
		button_16.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bPrev.png"));
		panel_15.add(button_16);

		JButton button_17 = new JButton("");
		button_17.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bNext.png"));
		panel_15.add(button_17);

		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_16.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_11.add(panel_16);

		JButton button_18 = new JButton("");
		button_18.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bPrev.png"));
		panel_16.add(button_18);

		JButton button_19 = new JButton("");
		button_19.setIcon(new ImageIcon(Parameters.IMGPATH + "new/bNext.png"));
		panel_16.add(button_19);

		JPanel panel_1_ = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_1_.getLayout();
		flowLayout_5.setVgap(0);
		flowLayout_5.setHgap(0);
		panel_11.add(panel_1_);

		JButton button_ = new JButton("");
		button_.addActionListener(new ActionListener() {
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
				int val = fc.showDialog(fc, "Open COM File");
				if (val == 0) {
					// textField.setText(fc.getSelectedFile().toString());
				}
			}
		});

		JButton button_1_ = new JButton("");
		button_1_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setComFileName(Project.getProjectComPath()
						+ Parameters.SEPARATOR
						+ scriptComComboBox.getSelectedItem());
			}
		});
		button_1_.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		panel_1_.add(button_1_);
		button_.setIcon(new ImageIcon(Parameters.IMGPATH + "Ouvrir.png"));
		panel_1_.add(button_);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_1.getLayout();
		flowLayout_6.setVgap(0);
		flowLayout_6.setHgap(0);
		panel_11.add(panel_1);

		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileFilter ff = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".gps"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "GPS files";
					}
				};

				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(ff);
				int val = fc.showDialog(fc, "Open GPS File");
				if (val == 0) {
					// textField.setText(fc.getSelectedFile().toString());
				}
			}
		});

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.setGpsFileName(Project.getProjectGpsPath()
						+ Parameters.SEPARATOR
						+ gpsPathNameComboBox.getSelectedItem());
			}
		});
		button_1.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		panel_1.add(button_1);
		button.setIcon(new ImageIcon(Parameters.IMGPATH + "Ouvrir.png"));
		panel_1.add(button);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

	}

}
