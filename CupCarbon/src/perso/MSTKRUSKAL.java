/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import arete.arete;
import device.Device;
import device.DeviceList;
import device.SensorNode;

public class MSTKRUSKAL extends Thread {
	long temps;
	public void run() {
		
		List<SensorNode> Capteurs = DeviceList.getSensorNodes();
		algorithme(Capteurs);
	
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(Capteurs)+"\n La consommation globale = "+calculerComsommationGlobale(Capteurs));
			Layer.mapViewer.repaint();
	}



	public List<SensorNode> algorithme(List<SensorNode> capteurs) {
		long debut,fin;
		debut=System.currentTimeMillis();
		
		int i=0;
		ArrayList<arete> aretes = new ArrayList<arete>();
		for( SensorNode a : capteurs )
		{
			a.setValue(100);
			a.getComposantescnx().clear();
			a.ajouterComposantescnx(a);
			a.setRadioRadius(100);
		}
		
		for (i=0;i<capteurs.size();i++) {
			SensorNode capteur=capteurs.get(i);
			
			for (int j=i;j<capteurs.size();j++) {
				SensorNode voisin=capteurs.get(j);
				if(capteur.radioDetectZakaria(voisin))
					{
						aretes.add(new arete(capteur, voisin, capteur.distance(voisin)));
					}
					
			}
			
			
		}
		Collections.sort(aretes);

		for( arete a : aretes ){	
			if(isCycle(a)==false)
			{
			a.getDevice1().setRadioRadius(a.getDistance());
			a.getDevice2().setRadioRadius(a.getDistance());
			majComposantecnx(a);
			}
			
			}
		fin=System.currentTimeMillis();
		temps=fin-debut;
//		System.out.println("Le temps d'execution de Kruskal, en Milliseconde = "+(temps));
		
		return capteurs;
	}
	


	public boolean isCycle(arete ar)
	{
			if (ar.getDevice1().getComposantescnx().equals(ar.getDevice2().getComposantescnx()))
			{

				return true;
			}
			else{
				return false;
			}
		
	}
	public void majComposantecnx(arete a)
	{
		a.getDevice1().getComposantescnx().addAll(a.getDevice2().getComposantescnx());
		a.getDevice2().getComposantescnx().addAll(a.getDevice1().getComposantescnx());
		for(Device s : a.getDevice1().getComposantescnx())
		{
			s.getComposantescnx().addAll(a.getDevice1().getComposantescnx());
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
