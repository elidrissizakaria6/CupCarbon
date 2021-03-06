/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import arete.arete;
import device.Device;
import device.DeviceList;
import device.SensorNode;

public class MonAlgoClass extends Thread {

	public void run() {
		int i=0;
		//List<Device> noeuds = DeviceList.getNodes();
		ArrayList<arete> aretes = new ArrayList<arete>();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		
		for (i=0;i<capteurs.size();i++) {
			SensorNode capteur=capteurs.get(i);
//			capteur.setRadioRadius(5000);
			
			for (int j=i;j<capteurs.size();j++) {
				SensorNode voisin=capteurs.get(j);
//				if(capteur.radioDetect(voisin)&&(!capteur.equals(voisin))) 
//					{
						aretes.add(new arete(capteur, voisin, capteur.distance(voisin)));
//					}
					
			}
			
			System.out.println();
			
		}
		Collections.sort(aretes);
				
		for( SensorNode a : capteurs )
		{a.setMarked(false);a.ajouterComposantescnx(a);}
		for( SensorNode a : capteurs ){
			
			System.out.println(a.getComposantescnx());
		}
		
		for( arete a : aretes ){	
			System.out.println(a.getDistance());
			if(isCycle(a)==false)
			{
			a.getDevice1().setRadioRadius(a.getDistance());
			a.getDevice2().setRadioRadius(a.getDistance());
			majComposantecnx(a);
			System.out.println("entrer");
			}
			
		}
		
		

	}

	public boolean isCycle(arete ar)
	{
			if((ar.getDevice1().getComposantescnx().equals(ar.getDevice1()))||(ar.getDevice2().getComposantescnx().equals(ar.getDevice2())))
			{
				System.out.println("fchkel");
				return false;
			}
			else if (ar.getDevice1().getComposantescnx().equals(ar.getDevice2().getComposantescnx()))
			{
				System.out.println("hana dkhelt "+ar.getDevice1().getComposantescnx()+"="+ar.getDevice2().getComposantescnx());

				return true;
			}
			else{
				System.out.println("normal");
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
	
}
