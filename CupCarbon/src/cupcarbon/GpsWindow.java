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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import project.Project;
import device.MarkerList;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class GpsWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtFileName;
	private JTextField txtTitle;
	private JTextField txtFrom;
	private JTextField txtTo;
	private static GpsWindow frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GpsWindow();
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
	public GpsWindow() {
		super("GPS File Name");
		setRootPaneCheckingEnabled(false);

		this.setName("GPS Coords");
		setIconifiable(true);
		setClosable(true);
		setBounds(6, 95, 447, 230);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.WEST);
		panel_5.setLayout(new GridLayout(3, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Title");
		panel_5.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("From");
		panel_5.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("To");
		panel_5.add(lblNewLabel_2);

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		panel_6.setLayout(new GridLayout(3, 1, 0, 0));

		txtFrom = new JTextField();
		panel_6.add(txtFrom);
		txtFrom.setColumns(20);

		txtTitle = new JTextField();
		panel_6.add(txtTitle);
		txtTitle.setColumns(20);

		txtTo = new JTextField();
		panel_6.add(txtTo);
		txtTo.setColumns(20);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.WEST);

		JLabel lblFileName = new JLabel("File Name");
		lblFileName.setDisplayedMnemonic('a');
		panel_4.add(lblFileName);

		JPanel panel_7 = new JPanel();
		panel_2.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new BorderLayout(0, 0));

		txtFileName = new JTextField();
		txtFileName.setColumns(15);
		panel_7.add(txtFileName);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_8.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_7.add(panel_8, BorderLayout.EAST);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
			}
		});
		button_1.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		panel_8.add(button_1);

		JButton button = new JButton("");
		panel_8.add(button);
		button.setIcon(new ImageIcon(Parameters.IMGPATH + "Ouvrir.png"));
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
				int val = fc.showDialog(fc, "Save");
				if (val == 0)
					txtFileName.setText(fc.getSelectedFile().toString());
			}
		});

		JSeparator separator = new JSeparator();
		panel_2.add(separator, BorderLayout.SOUTH);

		JSeparator separator_1 = new JSeparator();
		panel_2.add(separator_1, BorderLayout.NORTH);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JSeparator separator_2 = new JSeparator();
		panel_3.add(separator_2, BorderLayout.NORTH);

		JPanel panel_9 = new JPanel();
		panel_3.add(panel_9, BorderLayout.CENTER);

		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(txtFileName.getText());
				MarkerList.open(txtFileName.getText());
			}
		});
		btnLoad.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		panel_9.add(btnLoad);

		JButton gpxButton = new JButton("Generate a route");
		gpxButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MarkerList.generateGpxFile();
			}
		});
		gpxButton.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		panel_9.add(gpxButton);

		JButton btnNewButton = new JButton("Save");
		panel_9.add(btnNewButton);
		btnNewButton.setIcon(new ImageIcon(Parameters.IMGPATH + "stylo.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtFileName.setText(Project.getGpsFileExtension(txtFileName
						.getText()));
				MarkerList.saveGpsCoords(txtFileName.getText(), txtTitle.getText(), txtFrom.getText(), txtTo.getText());
			}
		});
		setVisible(false);
	}

}
