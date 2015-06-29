/**
		 * @author Zakaria
		 */
package perso;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import device.Device;
import device.DeviceList;
import device.SensorNode;

public class BorneInf extends Thread {
	long temps;
	public void run() {
	
		List<SensorNode> capteurs = algorithme();
		
		
		
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));

	}



	public List<SensorNode> algorithme() {
		
		
		long debut,fin;
		debut=System.currentTimeMillis();
		
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setValue(0);
			a.getComposantescnx().clear();
			a.ajouterComposantescnx(a);
			a.setRadioRadius(100);
		}
		double min=Double.MAX_VALUE;
		for( SensorNode a : capteurs )
		{
			for(int i=0;i<a.getNeighborsZakaria().size();i++){
				if(a.getNeighborsZakaria().get(i).distance(a)<min){
					min=a.getNeighborsZakaria().get(i).distance(a);
				}
			}
			if(min<100){
			a.setValue(min);
			}
			min=Double.MAX_VALUE;
		}
		for( SensorNode a : capteurs )
		{
			a.setRadioRadius(a.getValue());
		}
				
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution de L'heuristique, en Milliseconde = "+(fin-debut));
		
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
	public void majComposantecnx(SensorNode S1, SensorNode S2)
	{
		S1.getComposantescnx().addAll(S2.getComposantescnx());
		S2.getComposantescnx().addAll(S1.getComposantescnx());
		for(Device s : S1.getComposantescnx())
		{
			s.getComposantescnx().addAll(S1.getComposantescnx());
		}
	}
	
}
