/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import device.DeviceList;
import device.SensorNode;

public class BIP extends Thread {

	public void run() {
//		int i=0;
		final int DEFAULTRADIOVALUE=100;
		double valeurMin= 500000;
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
			a.setRadioRadius(DEFAULTRADIOVALUE);
			a.setValue(a.getRadioRadius());
			if(a.isSelected()){ a.setMarked(true);noeudsMarques.add(a);System.out.println(a.getRadioRadius());}
			else {a.setMarked(false);noeudsNonMarques.add(a);}
		}
		SensorNode NoeudNonMarque=new SensorNode();
		SensorNode NoeudMarque=new SensorNode();
		SensorNode NoeudMarqueChoisi=new SensorNode();
		int numSommetNMCandidat = 0;
		while(noeudsMarques.size()<capteurs.size()){	
			for(int i=0;i<capteurs.size();i++){
				System.out.println("apres le permier for");
				if(capteurs.get(i).isMarked()==true){
					System.out.println("mama");
					NoeudMarque=capteurs.get(i);
					for(int j=0;j<capteurs.size();j++){
						
						if(capteurs.get(j).isMarked()==false){
							System.out.println("papa");
							if((NoeudMarque.distance(capteurs.get(j))-NoeudMarque.getValue())<valeurMin)
								{	
								System.out.println(NoeudMarque +" veut marqué " + capteurs.get(j));
								NoeudNonMarque=capteurs.get(j);
								numSommetNMCandidat=j;
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.distance(NoeudNonMarque)-NoeudMarque.getValue());
								}
						}
					}
				}
			}
			System.out.println(NoeudMarqueChoisi +" a marqué " + NoeudNonMarque);
			NoeudMarqueChoisi.setRadioRadius(NoeudMarqueChoisi.getValue()+valeurMin);
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.getRadioRadius());
			capteurs.get(numSommetNMCandidat).setMarked(true);
//			NoeudNonMarque.setMarked(true);//je me demandais si je dois marque dans la liste capteurs ou comme ici
			System.out.println("je suis la");
			noeudsMarques.add(NoeudNonMarque);
			noeudsNonMarques.remove(NoeudNonMarque);
			valeurMin=50000;
		}

		final JFrame parent = new JFrame();
		try {
			sleep(5);
			JOptionPane.showMessageDialog(parent, "la puissance = "+calculerPuissanceGlobale(capteurs));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
}
