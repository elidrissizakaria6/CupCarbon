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

	public void run() {
		int i=0;
		//List<Device> noeuds = DeviceList.getNodes();
		ArrayList<arete> aretes = new ArrayList<arete>();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		
		
		for( SensorNode a : capteurs )
		{
			a.setValue(100);
			a.getComposantescnx().clear();
			a.ajouterComposantescnx(a);
			a.setRadioRadius(100);
		}
		
		for (i=0;i<capteurs.size();i++) {
			SensorNode capteur=capteurs.get(i);
//			capteur.setRadioRadius(5000);
			
			for (int j=i;j<capteurs.size();j++) {
				SensorNode voisin=capteurs.get(j);
				if(capteur.radioDetectZakaria(voisin))
					{
						aretes.add(new arete(capteur, voisin, capteur.distance(voisin)));
					}
					
			}
			
			System.out.println();
			
		}
		Collections.sort(aretes);
		System.out.println("Le nombre d'aretes"+aretes.size());

		
		
		for( arete a : aretes ){	
//			System.out.println(a.getDistance());
			if(isCycle(a)==false)
			{
			a.getDevice1().setRadioRadius(a.getDistance());
			a.getDevice2().setRadioRadius(a.getDistance());
			majComposantecnx(a);
			System.out.println("entrer");
			}
			
		}
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n La consommation globale = "+calculerComsommationGlobale(capteurs));
			Layer.mapViewer.repaint();
	}
		


	public boolean isCycle(arete ar)
	{
			if((ar.getDevice1().getComposantescnx().equals(ar.getDevice1()))||(ar.getDevice2().getComposantescnx().equals(ar.getDevice2())))
			{
//				System.out.println("cas superflu");
				return false;
			}
			else if (ar.getDevice1().getComposantescnx().equals(ar.getDevice2().getComposantescnx()))
			{
//				System.out.println("cas �galit� "+ar.getDevice1().getComposantescnx()+"="+ar.getDevice2().getComposantescnx());

				return true;
			}
			else{
//				System.out.println("pas de cycle normal");
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
