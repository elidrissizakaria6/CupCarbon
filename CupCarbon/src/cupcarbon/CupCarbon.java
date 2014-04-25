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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import map.WorldMap;
import project.Project;
import solver.OmnetPp;
import solver.SensorColoring;
import solver.SensorSetCover;
import solver.SensorTargetCoverageRun;
import utilities.GraphViewer;
import device.Device;
import device.DeviceList;
import device.MarkerList;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @author Arezki Laga
 * @version 1.0
 */
public class CupCarbon {

	private JFrame mainFrame;
	private AboutCupCarbon aboutBox = null;
	private JDesktopPane desktopPane = new JDesktopPane();
	private JLabel lblNodesNumber;
	private static JLabel label;
	private static JLabel sspeedLabel;
	private JCheckBoxMenuItem chckbxmntmAddSelection;
	private ComScriptWindow comScriptWindow = new ComScriptWindow();
	private CupCarbonMap cupCarbonMap;
	private GpsWindow gpsWindow = new GpsWindow();
	private DeviceParametersWindow deviceParametersWindow = new DeviceParametersWindow();
	private FlyingObjParametersWindow flyingObjParametersWindow = new FlyingObjParametersWindow();
	private InformationWindow infoWindow = new InformationWindow();
	private WsnSimulationWindow wsnSimWindow = new WsnSimulationWindow();

	public static int simulationNumber = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// try
				// {
				// //UIManager.put("Panel.background", new Color(230,210,250));
				// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				// //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				// }
				// catch(Exception e)
				// {
				// //e.printStackTrace();
				// }

				try {
					FileInputStream licenceFile = new FileInputStream(
							"cupcarbon_licence.txt");
					int c;
					while ((c = licenceFile.read()) != -1) {
						System.out.print((char) c);
					}
					System.out.println();
					licenceFile.close();
					CupCarbon window = new CupCarbon();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// Runtime.getRuntime().addShutdownHook(new Thread() {
		// public void run() {
		// Project.saveProject();
		// }
		// });
	}

	/**
	 * Create the application.
	 */
	public CupCarbon() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		mainFrame.setFont(new Font("Arial", Font.PLAIN, 12));
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/cupcarbon_logo_small.png"));
		mainFrame.setTitle("CupCarbon");
		mainFrame.setBounds(100, 0, 1000, 700);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Arial", Font.PLAIN, 12));
		mainFrame.setJMenuBar(menuBar);

		JMenu mnProject = new JMenu("Project");
		mnProject.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "projects_folder_badged.png"));
		menuBar.add(mnProject);
		mainFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int n = JOptionPane.showConfirmDialog(mainFrame,
						"Would you like to quit ?", "Quit",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					Project.saveProject();
					System.exit(0);
				}
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		JMenuItem mntmNewProject = new JMenuItem("New Project");
		mntmNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileFilter projectFilter = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".cup"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "CupCarbon Project";
					}
				};

				JFileChooser fc = new JFileChooser("New CupCarbon Project");
				fc.setFileFilter(projectFilter);
				int val = fc.showDialog(fc, "New Project");
				if (val == 0) {
					Project.newProject(fc.getSelectedFile().getParent()
							+ Parameters.SEPARATOR
							+ fc.getSelectedFile().getName(), fc
							.getSelectedFile().getName());
				}
			}
		});
		mntmNewProject.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "folder_new.png"));
		mnProject.add(mntmNewProject);

		JMenuItem mntmOpenProject = new JMenuItem("Open Project");
		mntmOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileFilter projectFilter = new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						else if (f.getName().endsWith(".cup"))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "CupCarbon files";
					}
				};

				JFileChooser fc = new JFileChooser("Open CupCarbon Project");
				fc.setFileFilter(projectFilter);
				int val = fc.showDialog(fc, "Open Project");
				if (val == 0) {
					Project.openProject(fc.getSelectedFile().getParent(), fc
							.getSelectedFile().getName());
					cupCarbonMap.setTitle("CupCarbon Map : "+fc.getSelectedFile().getName());
				}
			}
		});
		mntmOpenProject
				.setIcon(new ImageIcon(Parameters.IMGPATH + "folder.png"));
		mnProject.add(mntmOpenProject);

		JMenuItem mntmSaveProject = new JMenuItem("Save Project");
		mntmSaveProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Project.saveProject();
			}
		});
		mntmSaveProject.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "Enregistrer.png"));
		mnProject.add(mntmSaveProject);

		JMenuItem mntmSaveProjectAs = new JMenuItem("Save Project As");
		mntmSaveProjectAs.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "Enregistrer sous.png"));
		mnProject.add(mntmSaveProjectAs);

		JMenuItem mntmDuplicateProject = new JMenuItem("Duplicate Project");
		mntmDuplicateProject.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "gnome_panel_window_menu-1.png"));
		mnProject.add(mntmDuplicateProject);

		JSeparator separator = new JSeparator();
		mnProject.add(separator);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setIcon(new ImageIcon(Parameters.IMGPATH + "gnome_logout.png"));
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = JOptionPane.showConfirmDialog(mainFrame,
						"Would you like to quit ?", "Quit",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					// Project.saveProject();
					System.exit(0);
				}
			}
		});
		mnProject.add(mntmQuit);

		JMenu mnEdition = new JMenu("Edition");
		mnEdition
				.setIcon(new ImageIcon(Parameters.IMGPATH + "blockdevice.png"));
		menuBar.add(mnEdition);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Cancel");
		mntmNewMenuItem_2.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "arrow_rotate_clockwise.png"));
		mnEdition.add(mntmNewMenuItem_2);

		JMenuItem mntmRetry = new JMenuItem("Retry");
		mntmRetry.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "arrow_rotate_anticlockwise.png"));
		mnEdition.add(mntmRetry);

		JSeparator separator_2 = new JSeparator();
		mnEdition.add(separator_2);

		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.setIcon(new ImageIcon(Parameters.IMGPATH + "cut.png"));
		mnEdition.add(mntmCut);

		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.setIcon(new ImageIcon(Parameters.IMGPATH + "copy.png"));
		mnEdition.add(mntmCopy);

		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.setIcon(new ImageIcon(Parameters.IMGPATH + "paste.png"));
		mnEdition.add(mntmPaste);

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setIcon(new ImageIcon(Parameters.IMGPATH + "Supprimer.png"));
		mnEdition.add(mntmDelete);

		JSeparator separator_3 = new JSeparator();
		mnEdition.add(separator_3);

		JMenuItem mntmMove = new JMenuItem("Move");
		mntmMove.setIcon(new ImageIcon(Parameters.IMGPATH + "move2red.png"));
		mnEdition.add(mntmMove);

		JMenu mnSelection = new JMenu("Selection");
		mnSelection.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "shape_square_select.png"));
		menuBar.add(mnSelection);

		JMenuItem mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, -1, true);
				WorldMap.setSelectionOfAllMarkers(true, -1, true);
			}
		});
		mntmSelectAll.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "shapes_many_select.png"));
		mnSelection.add(mntmSelectAll);

		JMenuItem mntmDeselectAll = new JMenuItem("Deselect All");
		mntmDeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, -1, true);
				WorldMap.setSelectionOfAllMarkers(false, -1, true);
			}
		});
		mntmDeselectAll.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "selection.png"));
		mnSelection.add(mntmDeselectAll);

		JMenuItem mntmInvertSelection = new JMenuItem("Invert Selection");
		mntmInvertSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.invertSelection();
			}
		});
		mntmInvertSelection.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "stock_filters_invert.png"));
		mnSelection.add(mntmInvertSelection);

		JSeparator separator_1 = new JSeparator();
		mnSelection.add(separator_1);

		chckbxmntmAddSelection = new JCheckBoxMenuItem("Add Selection");
		chckbxmntmAddSelection.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "layer_select.png"));
		mnSelection.add(chckbxmntmAddSelection);

		JSeparator separator_10 = new JSeparator();
		mnSelection.add(separator_10);

		JMenu mnSelectAll = new JMenu("Select All ...");
		mnSelection.add(mnSelectAll);

		JMenuItem mntmSelectAllSensors = new JMenuItem("Select All Sensors");
		mnSelectAll.add(mntmSelectAllSensors);
		mntmSelectAllSensors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, Device.SENSOR,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllSensors.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllGasses = new JMenuItem("Select All Gasses");
		mnSelectAll.add(mntmSelectAllGasses);
		mntmSelectAllGasses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, Device.GAS,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllGasses.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllMobiles = new JMenuItem("Select All Mobiles");
		mnSelectAll.add(mntmSelectAllMobiles);
		mntmSelectAllMobiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.setSelectionOfAllNodes(true, Device.MOBILE,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllMobiles.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllMobiles_1 = new JMenuItem(
				"Select All Mobiles WR");
		mnSelectAll.add(mntmSelectAllMobiles_1);
		mntmSelectAllMobiles_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.setSelectionOfAllNodes(true, Device.MOBILE_WR,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllMobiles_1.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllRouters = new JMenuItem("Select All Routers");
		mnSelectAll.add(mntmSelectAllRouters);
		mntmSelectAllRouters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, Device.BRIDGE,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllRouters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllFlyingObjects = new JMenuItem(
				"Select All Flying Objects");
		mnSelectAll.add(mntmSelectAllFlyingObjects);
		mntmSelectAllFlyingObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, Device.FLYING_OBJECT,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllFlyingObjects.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllBase = new JMenuItem("Select All Base Stations");
		mnSelectAll.add(mntmSelectAllBase);
		mntmSelectAllBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(true, Device.BASE_STATION,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllBase.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenuItem mntmSelectAllMarkers = new JMenuItem("Select All Markers");
		mnSelectAll.add(mntmSelectAllMarkers);
		mntmSelectAllMarkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllMarkers(true, Device.MARKER,
						chckbxmntmAddSelection.getState());
			}
		});
		mntmSelectAllMarkers.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "select_node.png"));

		JMenu mnDeselectAll = new JMenu("Deselect All ...");
		mnSelection.add(mnDeselectAll);

		JMenuItem mntmDeselectAllSensors = new JMenuItem("Deselect All Sensors");
		mnDeselectAll.add(mntmDeselectAllSensors);
		mntmDeselectAllSensors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.SENSOR, true);
			}
		});
		mntmDeselectAllSensors.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllGasses = new JMenuItem("Deselect All Gasses");
		mnDeselectAll.add(mntmDeselectAllGasses);
		mntmDeselectAllGasses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.GAS, true);
			}
		});
		mntmDeselectAllGasses.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllMobiles = new JMenuItem("Deselect All Mobiles");
		mnDeselectAll.add(mntmDeselectAllMobiles);
		mntmDeselectAllMobiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.MOBILE, true);
			}
		});
		mntmDeselectAllMobiles.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllMobiles_1 = new JMenuItem(
				"Deselect All Mobiles WR");
		mnDeselectAll.add(mntmDeselectAllMobiles_1);
		mntmDeselectAllMobiles_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.MOBILE_WR, true);
			}
		});
		mntmDeselectAllMobiles_1.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllRouters = new JMenuItem("Deselect All Routers");
		mnDeselectAll.add(mntmDeselectAllRouters);
		mntmDeselectAllRouters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.BRIDGE, true);
			}
		});
		mntmDeselectAllRouters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllFlyingObjects = new JMenuItem(
				"Deselect All Flying Objects");
		mnDeselectAll.add(mntmDeselectAllFlyingObjects);
		mntmDeselectAllFlyingObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.FLYING_OBJECT,
						true);
			}
		});
		mntmDeselectAllFlyingObjects.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllBase = new JMenuItem(
				"Deselect All Base Stations");
		mnDeselectAll.add(mntmDeselectAllBase);
		mntmDeselectAllBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllNodes(false, Device.BASE_STATION,
						true);
			}
		});
		mntmDeselectAllBase.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JMenuItem mntmDeselectAllMarkers = new JMenuItem("Deselect All Markers");
		mnDeselectAll.add(mntmDeselectAllMarkers);
		mntmDeselectAllMarkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.setSelectionOfAllMarkers(false, Device.MARKER, true);
			}
		});
		mntmDeselectAllMarkers.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "deselect_node.png"));

		JSeparator separator_9 = new JSeparator();
		mnSelection.add(separator_9);

		JMenu mnNodes = new JMenu("Nodes");
		mnNodes.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_green.png"));
		menuBar.add(mnNodes);

		JMenuItem mntmAddSensor = new JMenuItem("Add Sensor");
		mntmAddSensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('1');
			}
		});

		JMenuItem mntmLoadSensors = new JMenuItem("Load Sensors/Targets");
		mntmLoadSensors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.loadCityNodes();
			}
		});
		mnNodes.add(mntmLoadSensors);

		JSeparator separator_5 = new JSeparator();
		mnNodes.add(separator_5);
		mntmAddSensor.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_mauve.png"));
		mnNodes.add(mntmAddSensor);

		JMenuItem mntmAddRouter = new JMenuItem("Add Router");
		mntmAddRouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('2');
			}
		});
		mntmAddRouter.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_blue_ciel.png"));
		mnNodes.add(mntmAddRouter);

		JMenuItem mntmAddMobile = new JMenuItem("Add Mobile");
		mntmAddMobile.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_noir.png"));
		mnNodes.add(mntmAddMobile);

		JMenuItem mntmAddMobileWr = new JMenuItem("Add Mobile WR");
		mntmAddMobileWr.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_noir_radio.png"));
		mnNodes.add(mntmAddMobileWr);

		JMenuItem mntmAddBaseStation = new JMenuItem("Add Base Station");
		mntmAddBaseStation.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_orange.png"));
		mnNodes.add(mntmAddBaseStation);

		JMenuItem mntmAddFlyingObjects = new JMenuItem("Add Flying Objects");
		mntmAddFlyingObjects.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "insects.png"));
		mnNodes.add(mntmAddFlyingObjects);

		JMenuItem mntmAddGas = new JMenuItem("Add Gas");
		mntmAddGas.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "circle_orange.png"));
		mnNodes.add(mntmAddGas);

		JMenuItem mntmAddMarkers = new JMenuItem("Add Markers");
		mntmAddMarkers.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "marker_rounded_light_blue.png"));
		mnNodes.add(mntmAddMarkers);

		JSeparator separator_6 = new JSeparator();
		mnNodes.add(separator_6);

		JMenuItem mntmParameters = new JMenuItem("Device Parameters");
		mntmParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openDeviceParemeterWindow();
			}
		});
		mntmParameters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "ui_menu_blue.png"));
		mnNodes.add(mntmParameters);

		JMenuItem mntmRouteFromMarkers = new JMenuItem("Route from markers");
		mntmRouteFromMarkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MarkerList.generateGpxFile();
			}
		});

		JMenuItem mntmFlyingObjectParameters = new JMenuItem(
				"Flying Object Parameters");
		mntmFlyingObjectParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFlyingObjectParemeterWindow();
			}
		});
		mntmFlyingObjectParameters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "ui_menu_blue.png"));
		mnNodes.add(mntmFlyingObjectParameters);
		mntmRouteFromMarkers.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "route.png"));
		mnNodes.add(mntmRouteFromMarkers);

		JSeparator separator_8 = new JSeparator();
		mnNodes.add(separator_8);

		JMenuItem mntmInitialize_1 = new JMenuItem("Initialize");
		mntmInitialize_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.initSelectedActivation();
			}
		});

		JMenuItem mntmToOmnet = new JMenuItem("To OMNeT");
		mntmToOmnet
				.setIcon(new ImageIcon(Parameters.IMGPATH + "loopnone-1.png"));
		mntmToOmnet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OmnetPp.omnetFileGeneration();
			}
		});
		mnNodes.add(mntmToOmnet);

		JSeparator separator_11 = new JSeparator();
		mnNodes.add(separator_11);
		mntmInitialize_1.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "circle_grey.png"));
		mnNodes.add(mntmInitialize_1);

		JMenuItem mntmInitializeAll = new JMenuItem("Initialize All");
		mntmInitializeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceList.initActivation();
			}
		});
		mntmInitializeAll.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "circle_grey.png"));
		mnNodes.add(mntmInitializeAll);

		JMenu mnGraph = new JMenu("Graph");
		mnGraph.setIcon(new ImageIcon(Parameters.IMGPATH + "tree_diagramm.png"));
		menuBar.add(mnGraph);

		JMenuItem mntmSensorGraph = new JMenuItem("Sensor Graph");
		mntmSensorGraph
				.setIcon(new ImageIcon(Parameters.IMGPATH + "graph.png"));
		mntmSensorGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!infoWindow.isVisible()) {
					infoWindow.setVisible(true);
					desktopPane.add(infoWindow);
				}
				infoWindow.toFront();
				infoWindow.getTextPane().setText(
						DeviceList.displaySensorGraph().toString());
			}
		});
		mnGraph.add(mntmSensorGraph);

		JMenuItem mntmSensortargetGraph = new JMenuItem("Sensor/Target Graph");
		mntmSensortargetGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!infoWindow.isVisible()) {
					infoWindow.setVisible(true);
					desktopPane.add(infoWindow);
				}
				infoWindow.toFront();
				infoWindow.getTextPane().setText(
						DeviceList.displaySensorTargetGraph().toString());
			}
		});
		mntmSensortargetGraph.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "graph.png"));
		mnGraph.add(mntmSensortargetGraph);

		JMenu mnResolution = new JMenu("Solver");
		mnResolution.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "edu_mathematics-1.png"));
		menuBar.add(mnResolution);

		JMenuItem mntmMinSetCover = new JMenuItem("Sensor Coverage");
		mntmMinSetCover.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "edu_mathematics-1.png"));
		mntmMinSetCover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SensorSetCover.sensorSetCover();
			}
		});
		mnResolution.add(mntmMinSetCover);

		JMenuItem mntmNewMenuItem = new JMenuItem("Target Coverage");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SensorSetCover.sensorTargetSetCover() ;				
			}
		});
		mntmNewMenuItem.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "edu_mathematics-1.png"));
		mnResolution.add(mntmNewMenuItem);
		
		JMenuItem mntmTargetCoverageth = new JMenuItem("Target Coverage (Th)");
		mntmTargetCoverageth.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "edu_mathematics-1.png"));
		mntmTargetCoverageth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new SensorTargetCoverageRun()).start();
			}
		});
		mnResolution.add(mntmTargetCoverageth);
		
		JMenuItem mntmChannelColoring = new JMenuItem("Channel Coloring");
		mntmChannelColoring.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "edu_mathematics-1.png"));
		mntmChannelColoring.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SensorColoring sc = new SensorColoring() ;
				sc.executeColoring();
			}
		});
		mnResolution.add(mntmChannelColoring);

		JSeparator separator_7 = new JSeparator();
		mnResolution.add(separator_7);

		JMenuItem mntmInitialize = new JMenuItem("Initialize All");
		mntmInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.initActivation();
			}
		});
		mntmInitialize.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "circle_grey.png"));
		mnResolution.add(mntmInitialize);

		JMenu mnSimulation = new JMenu("Simulation");
		mnSimulation.setIcon(new ImageIcon(Parameters.IMGPATH + "run.png"));
		menuBar.add(mnSimulation);

		JMenuItem mntmSimulate = new JMenuItem("Simulate Agent");
		mntmSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.simulate();
			}
		});
		mntmSimulate.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "flag_green.png"));
		mnSimulation.add(mntmSimulate);

		JMenuItem mntmSimulateAll = new JMenuItem("Simulate All Agents");
		mntmSimulateAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.simulateAll();
			}
		});
		mntmSimulateAll.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "flag_green2.png"));
		mnSimulation.add(mntmSimulateAll);

		JMenuItem mntmStopSimulation = new JMenuItem("Stop simulation");
		mntmStopSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeviceList.stopSimulation();
			}
		});
		mntmStopSimulation.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "flag_red.png"));
		mnSimulation.add(mntmStopSimulation);
		mnSimulation.add(new JSeparator());

		JMenuItem mntmCreateComScenario = new JMenuItem("Communication script");
		mntmCreateComScenario.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "stylo.png"));
		mnSimulation.add(mntmCreateComScenario);
		mntmCreateComScenario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File comFiles = new File(Project.getProjectScriptPath());
				String[] c = comFiles.list();
				ComScriptWindow.txtLoadFileName.removeAllItems();
				ComScriptWindow.txtLoadFileName.addItem("New script ...");
				for (int i = 0; i < c.length; i++) {
					ComScriptWindow.txtLoadFileName.addItem(c[i]);
				}

				if (!comScriptWindow.isVisible()) {
					desktopPane.add(comScriptWindow);
					comScriptWindow.show();
				}
				comScriptWindow.toFront();

				// //if (!comWindow.isVisible()) {
				// desktopPane.add(comWindow);
				// comWindow.setVisible(true);
				// //}
				// comWindow.toFront();
			}
		});

		JMenuItem mntmSimulation = new JMenuItem("WSN Simulation");
		mntmSimulation.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "settings_right_rest.png"));
		mntmSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.deSimulation();
				if (!wsnSimWindow.isVisible()) {
					desktopPane.add(wsnSimWindow);
					wsnSimWindow.setVisible(true);
				}
				wsnSimWindow.toFront();
			}
		});

		JSeparator separator_12 = new JSeparator();
		mnSimulation.add(separator_12);
		mnSimulation.add(mntmSimulation);

		JSeparator separator_4 = new JSeparator();
		mnSimulation.add(separator_4);

		JMenuItem mntmSimulationParameters = new JMenuItem(
				"Simulation parameters");
		mntmSimulationParameters.setEnabled(false);
		mntmSimulationParameters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "ui_menu_blue.png"));
		mnSimulation.add(mntmSimulationParameters);

		JMenuItem mntmEnergyGraph = new JMenuItem("Energy Graph");
		mntmEnergyGraph.setEnabled(false);
		mntmEnergyGraph.setIcon(new ImageIcon("images/curve.png"));
		mntmEnergyGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Project.getProjectResultsPath());
				GraphViewer graphViewer = new GraphViewer();
				if (!graphViewer.isVisible()) {
					desktopPane.add(graphViewer);
					graphViewer.setVisible(true);
					graphViewer.draw();
				}
				graphViewer.toFront();
			}
		});
		mnSimulation.add(mntmEnergyGraph);

		JMenu mnWindow = new JMenu("Window");
		mnWindow.setIcon(new ImageIcon(Parameters.IMGPATH + "frame_chart.png"));
		menuBar.add(mnWindow);

		JMenuItem mntmSensors = new JMenuItem("Sensors");
		mntmSensors.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmSensors);

		JMenuItem mntmGasses = new JMenuItem("Gasses");
		mntmGasses.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmGasses);

		JMenuItem mntmFlyingObjects = new JMenuItem("Flying Objects");
		mntmFlyingObjects.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmFlyingObjects);

		JMenuItem mntmMobile = new JMenuItem("Mobile");
		mntmMobile.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmMobile);

		JMenuItem mntmMobileWr = new JMenuItem("Mobile WR");
		mntmMobileWr.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmMobileWr);

		JMenuItem mntmRouter = new JMenuItem("Router");
		mntmRouter.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmRouter);

		JMenuItem mntmBaseStation = new JMenuItem("Base station");
		mntmBaseStation.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmBaseStation);

		JMenuItem mntmMarkers = new JMenuItem("Markers");
		mntmMarkers.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "activity_window.png"));
		mnWindow.add(mntmMarkers);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setIcon(new ImageIcon(Parameters.IMGPATH + "symbol_help.png"));
		menuBar.add(mnHelp);

		JMenuItem mntmAboutCupcarbon = new JMenuItem("About CupCarbon");
		mntmAboutCupcarbon.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "symbol_information.png"));
		mntmAboutCupcarbon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (aboutBox == null) {
					aboutBox = new AboutCupCarbon();
					aboutBox.setLocationRelativeTo(mainFrame);
				}
				aboutBox.setVisible(true);
			}
		});
		mnHelp.add(mntmAboutCupcarbon);

		JToolBar toolBar = new JToolBar();
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton btnSensor = new JButton("1 Sensor");
		btnSensor.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_mauve.png"));
		btnSensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.addNodeInMap('1');
			}
		});
		toolBar.add(btnSensor);

		JButton btnRouter = new JButton("4 Router");
		btnRouter.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_blue_ciel.png"));
		btnRouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('4');
			}
		});

		JButton btnGas = new JButton("2 Gas");
		btnGas.setIcon(new ImageIcon(Parameters.IMGPATH + "circle_orange.png"));
		btnGas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.addNodeInMap('2');
			}
		});
		toolBar.add(btnGas);

		JButton btnFlyingObjects = new JButton("3 Flying Objects");
		btnFlyingObjects.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "insects.png"));
		btnFlyingObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('3');
			}
		});
		toolBar.add(btnFlyingObjects);
		toolBar.add(btnRouter);

		JButton btnMobile = new JButton("6 Mobile");
		btnMobile.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_noir.png"));
		btnMobile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('6');
			}
		});

		JButton btnBaseStation = new JButton("5 Base Station");
		btnBaseStation.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_orange.png"));
		btnBaseStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('5');
			}
		});
		toolBar.add(btnBaseStation);
		toolBar.add(btnMobile);

		JButton btnMobileWr = new JButton("7 Mobile WR");
		btnMobileWr.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "blank_badge_noir_radio.png"));
		btnMobileWr.setToolTipText("Mobile With Radio");
		btnMobileWr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WorldMap.addNodeInMap('7');
			}
		});
		toolBar.add(btnMobileWr);

		JButton btnMarker = new JButton("8 Marker");
		btnMarker.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "marker_rounded_light_blue.png"));
		btnMarker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WorldMap.addNodeInMap('8');
			}
		});
		toolBar.add(btnMarker);

		JButton btnSensorParameters = new JButton("Device Parameters");
		btnSensorParameters.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "ui_menu_blue.png"));
		btnSensorParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDeviceParemeterWindow();
			}
		});

		JButton btnNewButton = new JButton("Marker Parameters");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!gpsWindow.isVisible()) {
					desktopPane.add(gpsWindow);
					gpsWindow.setVisible(true);
				}
				gpsWindow.toFront();
			}
		});
		btnNewButton.setIcon(new ImageIcon(Parameters.IMGPATH
				+ "ui_menu_blue.png"));
		toolBar.add(btnNewButton);
		toolBar.add(btnSensorParameters);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(2, 2, 2, 2));
		toolBar.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JPanel panel = new JPanel();
		panel_1.add(panel);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		lblNodesNumber = new JLabel(" N : ");
		lblNodesNumber.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(lblNodesNumber);

		label = new JLabel("0");
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		panel.add(label);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("  Agent Speed:  ");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_2.add(lblNewLabel);

		sspeedLabel = new JLabel("100  ");
		sspeedLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_2.add(sspeedLabel);

		cupCarbonMap = new CupCarbonMap();
		cupCarbonMap.setLocation(280, 91);
		cupCarbonMap.setFrameIcon(new ImageIcon(
				"images/cupcarbon_logo_small.png"));
		cupCarbonMap.setVisible(true);

		mainFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);
		desktopPane.setBackground(new Color(173, 216, 230));
		desktopPane.add(cupCarbonMap);

		try {

			cupCarbonMap.setMaximum(true);

		} catch (PropertyVetoException ex) {

			ex.printStackTrace();

		}
		// moveToFront(cupCarbonMap);
	}

	private void openDeviceParemeterWindow() {
		File gpsFiles = new File(Project.getProjectGpsPath());
		String[] s = gpsFiles.list();
		if (s == null)
			s = new String[1];
		DeviceParametersWindow.gpsPathNameComboBox.removeAllItems();
		DeviceParametersWindow.gpsPathNameComboBox.addItem("");
		for (int i = 0; i < s.length; i++) {
			DeviceParametersWindow.gpsPathNameComboBox.addItem(s[i]);
		}

		File comFiles = new File(Project.getProjectScriptPath());
		s = comFiles.list();
		if (s == null)
			s = new String[1];
		DeviceParametersWindow.scriptComboBox.removeAllItems();
		DeviceParametersWindow.scriptComboBox.addItem("");
		for (int i = 0; i < s.length; i++) {
			DeviceParametersWindow.scriptComboBox.addItem(s[i]);
		}

		if (!deviceParametersWindow.isVisible()) {
			deviceParametersWindow.setVisible(true);
			desktopPane.add(deviceParametersWindow);
		}
		deviceParametersWindow.toFront();
	}

	private void openFlyingObjectParemeterWindow() {
		if (!flyingObjParametersWindow.isVisible()) {
			flyingObjParametersWindow.setVisible(true);
			desktopPane.add(flyingObjParametersWindow);
		}
		flyingObjParametersWindow.toFront();
	}

	public static void updateInfos() {
		label.setText("" + DeviceList.size() + "  ");
		sspeedLabel.setText("" + Device.moveSpeed + "  ");
	}
}
