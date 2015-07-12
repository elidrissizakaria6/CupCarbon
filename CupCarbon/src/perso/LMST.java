/**
		 * @author Zakaria
		 */
package perso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import arete.arete;
import device.DeviceList;
import device.SensorNode;

public class LMST extends Thread {
	long temps;
	MSTKRUSKAL mstkruskal=new MSTKRUSKAL();
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
		
		List<SensorNode> Gprime = new ArrayList<SensorNode>();
		List<arete> Aretes=new ArrayList<arete>();
		//initialisation des noeuds
		InitialiserNoeuds(capteurs);
		for(SensorNode u : capteurs){
			List<SensorNode> union=u.getSensorNodeNeighborsZakaria();
			union.add(u);
			Gprime=mstkruskal.algorithme(union);// le changement de la portée affecte les autres
			for(SensorNode v : u.getSensorNodeNeighborsIn(Gprime)){
				Aretes.add(new arete(u, v, u.distance(v)));
			}
			InitialiserNoeuds(capteurs);// pour ca j'ai initialiser le graphe a chaque iteration
		}
		for(arete a : Aretes){
			a.getDevice1().setRadioRadius(Math.max(a.getDistance(), a.getDevice1().getValue()));
			a.getDevice2().setRadioRadius(Math.max(a.getDistance(), a.getDevice2().getValue()));
			a.getDevice1().setValue(a.getDevice1().getRadioRadius());
			a.getDevice2().setValue(a.getDevice2().getRadioRadius());
		}
		
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution de l'algo LMST, en Milliseconde = "+(fin-debut));
		
		return capteurs;
	}



	public void InitialiserNoeuds(List<SensorNode> capteurs) {
		for( SensorNode a : capteurs )
		{
			a.setValue(0);
			a.setRadioRadius(100);
		}
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
