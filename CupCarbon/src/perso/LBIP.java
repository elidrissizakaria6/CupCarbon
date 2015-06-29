/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cupcarbon.CupCarbonMap;
import device.DeviceList;
import device.SensorNode;
public class LBIP extends Thread {
	public static int k=100;
	long temps;
	BIPAdapteA bipAdapte=new BIPAdapteA();
	public void run() {

		
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		algorithme(capteurs);
		
		
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));


	}



	public List<SensorNode> algorithme(List<SensorNode> capteurs) {
		long debut,fin;
		debut=System.currentTimeMillis();
		
		SensorNode capteur=null;
		boolean marker=false;
		for( SensorNode a : capteurs )
		{
			if(a.isSelected()){
				capteur=a;
				marker=true;
			}
		}

		try{
				if(marker==false) capteur=capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1))));
			}catch(IndexOutOfBoundsException e){
				if(capteurs.size()!=0) capteur=capteurs.get(0);
				}
		
			//initialisation des noeuds
			InitialiserNoeuds(capteurs);
			
			try {
			LocalBIP(capteur);
			}catch(Exception e){System.out.println("LBIP Exception");}
			for(SensorNode s : capteurs)
			{
				if(s.isMarked()==true) s.setMarked(false);

			}
		
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution de l'algo avec la nouvelle procedure, en Milliseconde = "+(temps));
		return capteurs;
	}



	public void InitialiserNoeuds(List<SensorNode> capteurs) {
		for( SensorNode a : capteurs )
		{
			a.setValue(0);
			a.setRadioRadius(0);
			a.setMarked(false);
			a.setSelection(false);
			a.setVisited(false);
		}
	}
	private void LocalBIP(SensorNode capteur){
		double tmp=capteur.getRadioRadius();
		capteur.setRadioRadius(100);
		capteur.setVisited(true);
		List<SensorNode> union=new ArrayList<SensorNode>();
		union.addAll(capteur.getSensorNodeNeighbors());
		union.add(capteur);
		capteur.setRadioRadius(tmp);
//		try {
//			sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		CupCarbonMap.saveHImage(k++);
		bipAdapte.algorithme(union, capteur);
		for(SensorNode voisin : union){
				if((!Verifierfeuille(capteur, voisin))&&(!voisin.isVisited())){
					LocalBIP(voisin);
				}
		}
	}
	private boolean Verifierfeuille(SensorNode racine, SensorNode voisin){
		double tmp=voisin.getRadioRadius();
		voisin.setRadioRadius(100);
		if(voisin.getSensorNodeNeighbors().size()==1){
			if(voisin.getSensorNodeNeighbors().get(0)==racine){
				voisin.setRadioRadius(tmp);
				return true;
			}
		}
		voisin.setRadioRadius(tmp);
		return false;
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
