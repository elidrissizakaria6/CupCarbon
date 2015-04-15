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

public class BIPConsom extends Thread {

	public void run() {
//		int i=0;
		final int DEFAULTRADIOVALUE=100;
		double valeurMin= 100000000;
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
							System.out.println("hana "+(NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue()));
							if((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())<valeurMin)
								{	
								System.out.println(NoeudMarque +" veut marqué " + capteurs.get(j));
								NoeudNonMarque=capteurs.get(j);
								numSommetNMCandidat=j;
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue());
								}
						}
					}
				}
			}
			System.out.println(NoeudMarqueChoisi +" a marqué " + NoeudNonMarque);
			NoeudMarqueChoisi.setRadioRadius(NoeudMarqueChoisi.distance(capteurs.get(numSommetNMCandidat)));
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.Consommation(capteurs.get(numSommetNMCandidat)));
			capteurs.get(numSommetNMCandidat).setMarked(true);
//			NoeudNonMarque.setMarked(true);//je me demandais si je dois marque dans la liste capteurs ou comme ici
			System.out.println("je suis la");
			noeudsMarques.add(NoeudNonMarque);
			noeudsNonMarques.remove(NoeudNonMarque);
			valeurMin=100000000;
		}

		final JFrame parent = new JFrame();
		try {
			sleep(5);
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));
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
		return Conso;
	}
	
}
