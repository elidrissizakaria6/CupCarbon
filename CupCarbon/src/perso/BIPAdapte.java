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

	public void run() {
		boolean marker=false;
		double valeurMin= Double.MAX_VALUE;
//		ArrayList<arete> aretes = new ArrayList<arete>();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		System.out.println("je fais un truc");
//		for (i=0;i<capteurs.size();i++) {
//			SensorNode capteur=capteurs.get(i);
//			
//			for (int j=1;j<capteurs.size();j++) {
//				SensorNode voisin=capteurs.get(j);
//						aretes.add(new arete(capteur, voisin, capteur.distance(voisin)));		
//			}
//						
//		}
		
//		Collections.sort(aretes);
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setRadioRadius(0);
			a.setValue(0);
			if(a.isSelected()){marker=true; a.setMarked(true);noeudsMarques.add(a);System.out.println(a.getRadioRadius());}
			else {a.setMarked(false);noeudsNonMarques.add(a);}
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
				System.out.println("apres le permier for");
				if(capteurs.get(i).isMarked()==true){
					System.out.println("mama");
					NoeudMarque=capteurs.get(i);
					for(int j=0;j<capteurs.size();j++){
						
						if(capteurs.get(j).isMarked()==false){
							System.out.println("papa");
							System.out.println("hana "+(NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue()));
							if(((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue()))<valeurMin)
								{	
								System.out.println(NoeudMarque +" veut marqué " + capteurs.get(j));
								NoeudNonMarque=capteurs.get(j);
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue());
								}
						}
					}
				}
			}
			System.out.println(NoeudMarqueChoisi +" a marqué " + NoeudNonMarque);
			//Si on met pas le max, on va voir un noeud marqué diminuer sa valeur par rapport à un nouveau noeud non marqué 
			NoeudMarqueChoisi.setRadioRadius(Math.max(NoeudMarqueChoisi.distance(NoeudNonMarque), NoeudMarqueChoisi.getRadioRadius()));
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.getConsommation());
			NoeudNonMarque.setRadioRadius(NoeudMarqueChoisi.distance(NoeudNonMarque));
			NoeudNonMarque.setValue(NoeudMarqueChoisi.getConsommation());

			NoeudNonMarque.setMarked(true);
			System.out.println("je suis la");
			noeudsMarques.add(NoeudNonMarque);
			noeudsNonMarques.remove(NoeudNonMarque);
			valeurMin=Double.MAX_VALUE;
			Layer.getMapViewer().repaint();
//			try {
//				sleep(1000);
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

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
