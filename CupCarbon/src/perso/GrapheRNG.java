package perso;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import device.DeviceList;
import device.SensorNode;

public class GrapheRNG extends Thread {
	long temps;
	public void run() {

		List<SensorNode> capteurs = DeviceList.getSensorNodes();

		algorithme(capteurs);
		
		
		DecimalFormat df=new DecimalFormat("####.##" );
		final JFrame parent = new JFrame();
		JOptionPane.showMessageDialog(parent, "La puissance globale = "+df.format(calculerComsommationGlobale(capteurs)));
		Layer.mapViewer.repaint();

	}

	public List<SensorNode> algorithme(List<SensorNode> capteurs) {

		long debut,fin;
		debut=System.currentTimeMillis();
		
		//initialisation des noeuds
		for( SensorNode a : capteurs ){
			a.setRadioRadius(100);
			a.setValue(0);
		}

		boolean RNG=true;
		for(SensorNode u : capteurs){
			for(SensorNode v : u.getSensorNodeNeighborsZakaria()){
				for(SensorNode w : u.getSensorNodeNeighborsZakaria()){
					if((v.radioDetectZakaria(w))&&
							(u.distance(w)<u.distance(v))&&
							(v.distance(w)<v.distance(u))){
					RNG=false;
					}
				}
				if(RNG==true){
					u.getVoisinRNG().add(v);
//					System.out.println(u.getId() +" à ajouter comme voisin rng "+v.getId());
				}
				RNG=true;
			}
			
		}
		
		for(SensorNode u : capteurs){
			for(SensorNode v : u.getVoisinRNG()){
				u.setRadioRadius(Math.max(u.distance(v), u.getValue()));
				u.setValue(u.getRadioRadius());
			}
		}
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution RNG, en Milliseconde = "+(fin-debut));
		
		return capteurs;
	}
	
		


	public double calculerPuissanceGlobale(List<SensorNode> capteurs)
	{
		double puissance=0;
		for(SensorNode s : capteurs)
		{
			 puissance+=s.getRadioRadius();
		}
		return puissance;
	}
	public double calculerComsommationGlobale(List<SensorNode> capteurs)
	{
		double Conso=0;
		for(SensorNode s : capteurs)
		{
			Conso+=s.getConsommation();

		}
		
		return Conso;
	}
	
}

