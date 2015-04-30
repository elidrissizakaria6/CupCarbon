package map;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import device.Device;


	 public class Grille extends JPanel 
	   {
		 private static Layer layer;
		 public Grille()
		 {
			 System.out.println("je suis appelé");
		 }
	   
	      public JFrame laGrille()
	      { 
	         try
	         {
	            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	         }
	         
	            catch (UnsupportedLookAndFeelException e)
	            {
	               System.out.println(e);
	            }
	             
	            catch (ClassNotFoundException e)
	            {
	               System.out.println(e);
	            }
	             
	            catch (InstantiationException e)
	            {
	               System.out.println(e);
	            }
	             
	            catch (IllegalAccessException e)
	            {
	               System.out.println(e);
	            } 
	      
	      
	         JFrame fenetre = new JFrame(); 
	         fenetre.setSize(400, 400); 
	         fenetre.setTitle("Grille"); 
	         JPanel panneau = new Grille(); 
	         panneau.setBackground(Color.white); 
	         fenetre.add(panneau); 
	         fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	         fenetre.setVisible(true); 
	         
	         return fenetre;
	         
	      } 

	  	
	  	public static void deSimulation() {
	  		System.out.println("DE simulation");
	  	}
	  	
	  	public static void gpuSimulation() {
	  		System.out.println("GPU simulation");
	  	}
	  	
	  	public static void simulate() {
	  		layer.simulate();
	  	}

//	  	public static void comSimulate(String name, String log, long v1, long v2,
//	  			long v3) {
//	  		MehdiSimulation simulation = new MehdiSimulation(name, log);
//	  		simulation.setSimulationDelay(v1);
//	  		simulation.setSimulationLogicDelay(v2);
//	  		simulation.setStep(v3);
//	  		simulation.startSimulation();
//	  	}	
	  //	
//	  	public static void comSimulate(String name, String log) {
//	  		MehdiSimulation simulation = new MehdiSimulation(name, log);
//	  		simulation.startSimulation();
//	  	}

	  	public static void simulateAll() {
	  		layer.simulateAll();
	  	}
	  	
	  	public static void simulateSensors() {
	  		layer.simulateSensors();
	  	}
	  	
	  	public static void simulateMobiles() {
	  		layer.simulateMobiles();
	  	}

	  	public static void addNodeInMap(char c) {
	  		layer.addNodeInMap(c);
	  	}

	  	public static void loadNodes() {
	  		layer.loadNodes();
	  		// (new Layer()).loadNodes();
	  	}

	  	public static void loadCityNodes() {
	  		layer.loadCityNodes();
	  	}

	  	public static void setSelectionOfAllNodes(boolean selection, int type,
	  			boolean addSelection) {
	  		layer.setSelectionOfAllNodes(selection, type, addSelection);
	  	}

	  	public static void invertSelection() {
	  		layer.invertSelection();
	  	}

	  	public static void setSelectionOfAllMarkers(boolean selection, int type,
	  			boolean addSelection) {
	  		layer.setSelectionOfAllMarkers(selection, type, addSelection);
	  	}
	  	
	  	
	  }
	      
	    
	   