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

public class BIPBidirectionnel extends Thread {

	public void run() {
		boolean marker=false;
		double valeurMin= Double.MAX_VALUE;
		long debut,fin;
		debut=System.currentTimeMillis();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setRadioRadius(100);
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
		if(marker==false)
		{
			capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1)))).setMarked(true);
		}
		SensorNode NoeudNonMarque=new SensorNode();
		SensorNode NoeudMarque=new SensorNode();
		SensorNode NoeudMarqueChoisi=new SensorNode();
		while(noeudsMarques.size()<capteurs.size()){	
			for(int i=0;i<capteurs.size();i++){
				if(capteurs.get(i).isMarked()==true){
					NoeudMarque=capteurs.get(i);
					for(int j=0;j<NoeudMarque.getNeighbors().size();j++){
						
						if(NoeudMarque.getNeighbors().get(j).isMarked()==false){
							if(((NoeudMarque.Consommation(NoeudMarque.getNeighbors().get(j))-NoeudMarque.getValue())+(NoeudMarque.getNeighbors().get(j).Consommation(NoeudMarque)-NoeudMarque.getNeighbors().get(j).getValue()))<valeurMin)
								{	
								NoeudNonMarque=(SensorNode) NoeudMarque.getNeighbors().get(j);
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(NoeudNonMarque.Consommation(NoeudMarque)-NoeudNonMarque.getValue());
								}
						}
					}
				}
			}
			//Si on met pas le max, on va voir un noeud marqué diminuer sa valeur par rapport à un nouveau noeud non marqué 
			NoeudMarqueChoisi.setRadioRadius(Math.max(NoeudMarqueChoisi.distance(NoeudNonMarque), NoeudMarqueChoisi.getValue()));
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.getConsommation());
			NoeudNonMarque.setRadioRadius(NoeudMarqueChoisi.distance(NoeudNonMarque));
			NoeudNonMarque.setValue(NoeudMarqueChoisi.getConsommation());

			NoeudNonMarque.setMarked(true);
			noeudsMarques.add(NoeudNonMarque);
			noeudsNonMarques.remove(NoeudNonMarque);
			valeurMin=Double.MAX_VALUE;
			Layer.getMapViewer().repaint();
		}
		fin=System.currentTimeMillis();
		System.out.println("Le temps d'execution de l'algo avec la nouvelle procedure, en Milliseconde = "+(fin-debut));
		
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));


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
