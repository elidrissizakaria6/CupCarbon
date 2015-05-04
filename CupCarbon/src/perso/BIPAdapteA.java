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

public class BIPAdapteA extends Thread {

	public void run() {
		boolean marker=false;
		double valeurMin= Double.MAX_VALUE;
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		System.out.println("je fais un truc");
		for( SensorNode a : capteurs )
		{
			a.setRadioRadius(0);
			a.setValue(0);
			if(a.isSelected()){marker=true; a.setMarked(true);noeudsMarques.add(a);System.out.println(a.getRadioRadius());}
			else {a.setMarked(false);noeudsNonMarques.add(a);}
		}
		if(marker==false)
		{
			SensorNode SN=capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1))));
			SN.setMarked(true);
			noeudsMarques.add(SN);
		}
		SensorNode NoeudNonMarque=new SensorNode();
		SensorNode NoeudMarque=new SensorNode();
		SensorNode NoeudMarqueChoisi=new SensorNode();
		int i=0,j=0;
		while(noeudsMarques.size()<capteurs.size()){
//			for(int i=0;i<capteurs.size();i++){
			i=0;
			while(i<noeudsMarques.size()){
				System.out.println("apres le permier for");
				if(noeudsMarques.get(i).isMarked()==true){
					System.out.println("mama");
					NoeudMarque=noeudsMarques.get(i);
//					for(int j=0;j<capteurs.size();j++){
					j=0;
					while(j<noeudsNonMarques.size()){
						
						if(noeudsNonMarques.get(j).isMarked()==false){
							System.out.println("papa");
							System.out.println("hana "+(NoeudMarque.Consommation(noeudsNonMarques.get(j))-NoeudMarque.getValue()));
							if(((NoeudMarque.Consommation(noeudsNonMarques.get(j))-NoeudMarque.getValue())+(noeudsNonMarques.get(j).Consommation(NoeudMarque)-noeudsNonMarques.get(j).getValue()))<valeurMin)
								{	
								System.out.println(NoeudMarque +" veut marqué " + noeudsNonMarques.get(j));
								NoeudNonMarque=noeudsNonMarques.get(j);
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue())+(noeudsNonMarques.get(j).Consommation(NoeudMarque)-noeudsNonMarques.get(j).getValue());
								}
						}
						j++;
					}
				}
				i++;
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
