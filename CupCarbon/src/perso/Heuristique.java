/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import map.Layer;
import device.Device;
import device.DeviceList;
import device.SensorNode;

public class Heuristique extends Thread {

	public void run() {
		boolean marker=false;
		int l=0;
		double valeurMin= Double.MAX_VALUE;
//		ArrayList<arete> aretes = new ArrayList<arete>();
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setValue(100);
			a.getComposantescnx().clear();
			a.ajouterComposantescnx(a);
			a.setRadioRadius(100);
		}
		double min=Double.MAX_VALUE;
		for( SensorNode a : capteurs )
		{
			for(int i=0;i<a.getNeighborsZakaria().size();i++){
//				System.out.println(" je suis voisin ");
				System.out.println(a.getNeighborsZakaria());
				if(a.getNeighborsZakaria().get(i).Consommation(a)<min){
					min=a.getNeighborsZakaria().get(i).Consommation(a);
//					System.out.println(i);
				}
			}
			a.setValue(min);
			a.setRadioRadius(min);
			min=Double.MAX_VALUE;
			

//			if(a.isSelected()){marker=true; a.setMarked(true);noeudsMarques.add(a);System.out.println(a.getRadioRadius());}
//			else {a.setMarked(false);noeudsNonMarques.add(a);}
		}
		// Mettre à jour les composantes connexes
		l=capteurs.size();
		for( SensorNode a : capteurs )
		{
			a.getComposantescnx().add(a);
			for(Device b : a.getNeighborsZakaria()){
				a.getComposantescnx().add(b);
				if(a.getComposantescnx().equals(b.getComposantescnx())){System.out.println(a.getComposantescnx()+"=="+b.getComposantescnx()); l--;}
			}
		}
		System.out.println(l);
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SensorNode S1 = null,S2 = null;
		while(l!=1){
			for( SensorNode a : capteurs )
			{
				for(int i=0;i<capteurs.size();i++ ){
					SensorNode b=(SensorNode) capteurs.get(i);
					if(!a.getComposantescnx().equals(b.getComposantescnx())){
						System.out.println("toto");
						if(((a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue()))<valeurMin){
							S1=a;S2=b;
							System.out.println("titi");
							valeurMin=(a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue());
						}
					}
				}
			}
			S1.setRadioRadius(Math.max(S1.distance(S2), S1.getRadioRadius()));
			S2.setRadioRadius(Math.max(S2.distance(S1), S2.getRadioRadius()));
			S2.setValue(S2.Consommation(S1));
			S1.setValue(S1.Consommation(S2));
			majComposantecnx(S1,S2);
			l=l-1;
			valeurMin=Double.MAX_VALUE;
			Layer.getMapViewer().repaint();
		}
		// Marquer un noeud au hasard si aucun noeud n'est marqué
//		if(marker==false)
//		{
//			capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1)))).setMarked(true);
//		}
//		SensorNode NoeudNonMarque=new SensorNode();
//		SensorNode NoeudMarque=new SensorNode();
//		SensorNode NoeudMarqueChoisi=new SensorNode();
//		while(noeudsMarques.size()<capteurs.size()){	
//			for(int i=0;i<capteurs.size();i++){
//				System.out.println("apres le permier for");
//				if(capteurs.get(i).isMarked()==true){
//					System.out.println("mama");
//					NoeudMarque=capteurs.get(i);
//					for(int j=0;j<capteurs.size();j++){
//						
//						if(capteurs.get(j).isMarked()==false){
//							System.out.println("papa");
//							System.out.println("hana "+(NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue()));
//							if(((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue()))<valeurMin)
//								{	
//								System.out.println(NoeudMarque +" veut marqué " + capteurs.get(j));
//								NoeudNonMarque=capteurs.get(j);
//								NoeudMarqueChoisi=NoeudMarque;
//								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(capteurs.get(j).Consommation(NoeudMarque)-capteurs.get(j).getValue());
//								}
//						}
//					}
//				}
//			}
//			System.out.println(NoeudMarqueChoisi +" a marqué " + NoeudNonMarque);
//			//Si on met pas le max, on va voir un noeud marqué diminuer sa valeur par rapport à un nouveau noeud non marqué 
//			NoeudMarqueChoisi.setRadioRadius(Math.max(NoeudMarqueChoisi.distance(NoeudNonMarque), NoeudMarqueChoisi.getRadioRadius()));
//			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.getConsommation());
//			NoeudNonMarque.setRadioRadius(NoeudMarqueChoisi.distance(NoeudNonMarque));
//			NoeudNonMarque.setValue(NoeudMarqueChoisi.getConsommation());
//
//			NoeudNonMarque.setMarked(true);
//			System.out.println("je suis la");
//			noeudsMarques.add(NoeudNonMarque);
//			noeudsNonMarques.remove(NoeudNonMarque);
//			valeurMin=Double.MAX_VALUE;
//			Layer.getMapViewer().repaint();
//		}
//
//		final JFrame parent = new JFrame();
//			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
//					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));


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
