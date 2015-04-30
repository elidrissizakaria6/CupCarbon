package cupcarbon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import project.Project;
import utilities.MapCalc;
import map.Layer;
import consumer.MaConsommation;
import device.DeviceList;

public class EasyDeviceParametersWindow extends JInternalFrame {

	private static double fenetrex,fenetrey;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public EasyDeviceParametersWindow(){
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setAutoscrolls(false);
		int[] coord = MapCalc.geoToIntPixelMapXY(fenetrex, fenetrey);
		int lx1 = coord[0];
		int ly1 = coord[1];
		System.out.println(lx1);
		Point2D p2d=MapCalc.geoXYToPixelMap(fenetrex, fenetrey);
		setBounds(100,100 , 200, 150);
		setBorder(null);
		setVisible(false);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1,BorderLayout.NORTH);
		
		JButton btnNewButton1 = new JButton();
		btnNewButton1.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bplus.png"));
		btnNewButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DeviceList.updateRadioRadiusFromMap(5);
			}
		});
		panel_1.add(btnNewButton1);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2,BorderLayout.CENTER);
		
		JButton btnNewButton2 = new JButton();
		btnNewButton2.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "bmoins.png"));
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DeviceList.updateRadioRadiusFromMap(-5);
			}
		});
		panel_2.add(btnNewButton2);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3,BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Plus de"
				+ "\n Parametres");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CupCarbon.openDeviceParemeterWindow();
				toBack();
			}
		});
		panel_3.add(btnNewButton);
		
	}

	public static double getFenetrex() {
		return fenetrex;
	}

	public static void setFenetrex(double fenetrex) {
		EasyDeviceParametersWindow.fenetrex = fenetrex;
	}

	public static double getFenetrey() {
		return fenetrey;
	}

	public static void setFenetrey(double fenetrey) {
		EasyDeviceParametersWindow.fenetrey = fenetrey;
	}


}
