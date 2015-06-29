/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import device.DeviceList;
import device.SensorNode;

public class BIPAdapte extends Thread {
	long temps;
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
		
		double valeurMin= Double.MAX_VALUE;
		boolean marker=false;
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setMarked(false);
			a.setRadioRadius(0);
			a.setValue(0);
			if(a.isSelected()){
				marker=true; 
				a.setMarked(true);
				noeudsMarques.add(a);
			}
			else {
				a.setMarked(false);
				noeudsNonMarques.add(a);
			}
		}
		try{
		if(marker==false) capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1)))).setMarked(true);
		}catch(IndexOutOfBoundsException e){capteurs.get(0).setMarked(true);}
		SensorNode NoeudNonMarque=null;
		SensorNode NoeudMarque=null;
		SensorNode NoeudMarqueChoisi=null;
		while(noeudsMarques.size()<capteurs.size()){
			for(int i=0;i<capteurs.size();i++){
				if(capteurs.get(i).isMarked()==true){
					NoeudMarque=capteurs.get(i);						
					for(int j=0;j<capteurs.size();j++){
						if(capteurs.get(j).isMarked()==false){
							if((((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue()))<valeurMin)&&(NoeudMarque.distance(capteurs.get(j))<=100))
								{	
								NoeudNonMarque=capteurs.get(j);
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue());
								}
						}
					}
				}
			}
			//Si on ne met pas le max, on va voir un noeud marqué diminuer sa valeur par rapport à un nouveau noeud non marqué 
			if((NoeudMarqueChoisi!=null)&&(noeudsNonMarques!=null)){
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
		
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution de BIP ADAPTE, en Milliseconde = "+(fin-debut));
		
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
			Conso+=s.getConsommation();

		}
		for(SensorNode s : capteurs)
		{
			s.setMarked(false);

		}
		
		return Conso;
	}
	
}
