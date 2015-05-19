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

	Set<SensorNode> noeudsMarques = new HashSet<SensorNode>();
	public void run() {
		long debut,fin;
		debut=System.currentTimeMillis();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		for(Device capteur : capteurs){
			capteur.setVisited(false);
		}
		DFS(capteurs, capteurs.get(0));
		fin=System.currentTimeMillis();
		System.out.println("Le temps d'execution de l'algo avec la nouvelle procedure, en Milliseconde = "+(fin-debut));
		if(TesterConnexite(capteurs)==true){
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Le graphe est connexe");
		}
		else{
			final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent,"Le graphe n'est pas connexe");
		}
		
		
//		final JFrame parent = new JFrame();
//			JOptionPane.showMessageDialog();


	}
	private void DFS(List<SensorNode> capteurs, Device capteur){
		
		capteur.setVisited(true);
		noeudsMarques.add((SensorNode) capteur);
		for(Device voisin : capteur.getNeighbors()){
			if(voisin.isVisited()==false){
				DFS(capteurs,voisin);
			}
		}
	}
	private boolean TesterConnexite(List<SensorNode> capteurs){
		
		if(noeudsMarques.size()==capteurs.size()) return true;
		else System.out.println(noeudsMarques.size());return false;
	}
		



	
}
