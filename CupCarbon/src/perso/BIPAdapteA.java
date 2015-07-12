/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import map.Layer;
import device.SensorNode;

public class BIPAdapteA extends Thread {

	public List<SensorNode> algorithme(List<SensorNode> capteurs, SensorNode capteur) {
		double valeurMin= Double.MAX_VALUE;
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		//initialisation des noeuds
		capteur.setMarked(true);
		for( SensorNode a : capteurs )
		{
			if(a.isMarked()){
				noeudsMarques.add(a);
			}
			else {
				a.setRadioRadius(0);	
				noeudsNonMarques.add(a);
			}
		}
		SensorNode NoeudNonMarque=null;
		SensorNode NoeudMarque=null;
		SensorNode NoeudMarqueChoisi=null;
		while(noeudsMarques.size()<capteurs.size()){	
			for(int i=0;i<capteurs.size();i++){
				if(capteurs.get(i).isMarked()==true){
					NoeudMarque=capteurs.get(i);
					for(int j=0;j<capteurs.size();j++){
						
						if(capteurs.get(j).isMarked()==false){
							if(((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue()))<valeurMin)
								{	
								NoeudNonMarque=capteurs.get(j);
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue());
								}
						}
					}
				}
			}
			if((NoeudMarqueChoisi!=null)&&(noeudsNonMarques!=null)){
			//Si on met pas le max, on va voir un noeud marqué diminuer sa valeur par rapport à un nouveau noeud non marqué 
			NoeudMarqueChoisi.setRadioRadius(Math.max(NoeudMarqueChoisi.distance(NoeudNonMarque), NoeudMarqueChoisi.getRadioRadius()));
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.getConsommation());
			NoeudNonMarque.setRadioRadius(NoeudMarqueChoisi.distance(NoeudNonMarque));
			NoeudNonMarque.setValue(NoeudMarqueChoisi.getConsommation());

			NoeudNonMarque.setMarked(true);
			noeudsMarques.add(NoeudNonMarque);
			noeudsNonMarques.remove(NoeudNonMarque);
			valeurMin=Double.MAX_VALUE;
			Layer.getMapViewer().repaint();
			}
			else{
				noeudsMarques.add(NoeudMarqueChoisi);
			}
//			try {
//				sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			CupCarbonMap.saveHImage(k++);
		}
		return capteurs;
	}
		


	public double calculerPuissanceGlobale(List<SensorNode> capteurs)
	{
		double puissance=0;
		for(SensorNode s : capteurs)
		{
			if(s.isMarked()==true) puissance+=s.getRadioRadius();
		}
		return puissance;
	}
	public double calculerComsommationGlobale(List<SensorNode> capteurs)
	{
		double Conso=0;
		for(SensorNode s : capteurs)
		{
//			if(s.isMarked()==true) Conso+=s.getValue(); valeurs différente avec l'algo BIPConso
			if(s.isMarked()==true) Conso+=s.getConsommation();

		}
		for(SensorNode s : capteurs)
		{
//			if(s.isMarked()==true) Conso+=s.getValue(); valeurs différente avec l'algo BIPConso
			if(s.isMarked()==true) s.setMarked(false);

		}
		
		return Conso;
	}
	
}
