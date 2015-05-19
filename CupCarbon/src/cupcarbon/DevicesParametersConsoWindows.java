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
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import consumer.MaConsommation;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @author Zakaria El Idrissi
 * @version 1.0
 */
public class DevicesParametersConsoWindows extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	public static JTextField textField_1;
	public static JTextField textField_2;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DevicesParametersConsoWindows frame = new DevicesParametersConsoWindows();
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
	public DevicesParametersConsoWindows() {
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Paramètres de Consommation d'énergie");
		setBounds(100, 100, 550, 150);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Apply");
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH
				+ "loopnone-1.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MaConsommation.setCoefficientAtten(Integer.valueOf(textField_1.getText()));
				MaConsommation.setCte(Integer.valueOf(textField_2.getText()));
				try {
					setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_2.add(btnNewButton);

		JPanel panel_8 = new JPanel();
		getContentPane().add(panel_8, BorderLayout.NORTH);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.WEST);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel label = new JLabel("Coefficient d'attenuation");
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_9.add(label);

		JLabel label_1 = new JLabel("Constante");
		label_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_9.add(label_1);

		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new GridLayout(0, 1, 0, 0));

		textField_1 = new JTextField();
		panel_10.add(textField_1);
		textField_1.setColumns(2);

		textField_2 = new JTextField();
		panel_10.add(textField_2);
		textField_2.setColumns(2);
		
		textField_1.setText(String.valueOf(MaConsommation.getCoefficientAtten()));
		textField_2.setText(String.valueOf(MaConsommation.getCte()));

		JPanel panel_11 = new JPanel();
		panel_8.add(panel_11, BorderLayout.EAST);
		panel_11.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_16.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_11.add(panel_16);

		JButton button_18 = new JButton("");
		button_18.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bPrev.png"));
		button_18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(Project.getProjectScriptPath()+File.separator+ scriptComboBox.getSelectedItem());
				//DeviceList.setScriptFileName(//Project.getProjectScriptPath()
						//+ File.separator + 
				textField_1.setText(String.valueOf(Integer.parseInt(textField_1.getText())-1));
			}
		});
		panel_16.add(button_18);

		JButton button_19 = new JButton("");
		button_19.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bNext.png"));
		button_19.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(Project.getProjectScriptPath()+File.separator+ scriptComboBox.getSelectedItem());
				//DeviceList.setScriptFileName(//Project.getProjectScriptPath()
						//+ File.separator + 
				textField_1.setText(String.valueOf(Integer.parseInt(textField_1.getText())+1));
			}
		});
		panel_16.add(button_19);		
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_7 = (FlowLayout) panel.getLayout();
		flowLayout_7.setVgap(0);
		flowLayout_7.setHgap(0);
		panel_11.add(panel);
		
		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bPrev.png"));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(Project.getProjectScriptPath()+File.separator+ scriptComboBox.getSelectedItem());
				//DeviceList.setScriptFileName(//Project.getProjectScriptPath()
						//+ File.separator + 
				textField_2.setText(String.valueOf(Integer.parseInt(textField_2.getText())+1));
			}
		});
		panel.add(button_2);
		
		JButton button_3 = new JButton("");
		button_3.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bNext.png"));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(Project.getProjectScriptPath()+File.separator+ scriptComboBox.getSelectedItem());
				//DeviceList.setScriptFileName(//Project.getProjectScriptPath()
						//+ File.separator + 
				textField_2.setText(String.valueOf(Integer.parseInt(textField_2.getText())+1));
			}
		});
		panel.add(button_3);

		
	}

}
