package perso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import device.Device;
import device.DeviceList;
import device.SensorNode;

public class TesterLaConnexite extends Thread {
	private static  boolean connexe=false;
	private static  Set<SensorNode> noeudsMarques = new HashSet<SensorNode>();
	public void run() {
		
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		try{
		algorithme(capteurs);
		}catch(IndexOutOfBoundsException e){final JFrame parent = new JFrame();
		JOptionPane.showMessageDialog(parent,"Aucun capteur sur la carte");}
		if(connexe==true){
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Le graphe est connexe");
		}
		else{
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Le graphe n'est pas connexe");
		}
		


	}
	public static boolean algorithme(List<SensorNode> capteurs) {
		for(Device capteur : capteurs){
			capteur.setVisited(false);
		}
		noeudsMarques.clear();
		try{
			if(capteurs.size()!=0){
				DFS(capteurs, capteurs.get(0));
			}
		}catch(IndexOutOfBoundsException e){System.out.println("c'est pas grave");;}
		connexe=TesterConnexite(capteurs);
		return connexe;
	}
	private static void DFS(List<SensorNode> capteurs, SensorNode capteur){
		capteur.setVisited(true);
		noeudsMarques.add((SensorNode) capteur);
		for(SensorNode voisin : capteur.getSensorNodeNeighborsZakaria()){
				if(voisin.isVisited()==false){
					DFS(capteurs,voisin);
				}
		}
	}
	private static boolean TesterConnexite(List<SensorNode> capteurs){
		
		if(noeudsMarques.size()==capteurs.size()) return true;
		else return false;
	}
		



	
}
