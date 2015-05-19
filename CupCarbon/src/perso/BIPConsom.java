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
		boolean marker=false;
		double valeurMin=Double.MAX_VALUE;
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		List<SensorNode> noeudsNonMarques = new ArrayList<SensorNode>();
		List<SensorNode> noeudsMarques = new ArrayList<SensorNode>();
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
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
		if(marker==false)
		{
			capteurs.get((1 + (int)(Math.random() * ((capteurs.size() - 2) + 1)))).setMarked(true);
		}
		SensorNode NoeudNonMarque=new SensorNode();
		SensorNode NoeudMarque=new SensorNode();
		SensorNode NoeudMarqueChoisi=new SensorNode();
		int numSommetNMCandidat = 0;
		while(noeudsMarques.size()<capteurs.size()){	
			for(int i=0;i<capteurs.size();i++){
				if(capteurs.get(i).isMarked()==true){
					NoeudMarque=capteurs.get(i);
					for(int j=0;j<capteurs.size();j++){
						
						if(capteurs.get(j).isMarked()==false){
							if((NoeudMarque.Consommation(capteurs.get(j))-NoeudMarque.getValue())<valeurMin)
								{	
								NoeudNonMarque=capteurs.get(j);
								numSommetNMCandidat=j;
								NoeudMarqueChoisi=NoeudMarque;
								valeurMin=(NoeudMarque.Consommation(NoeudNonMarque)-NoeudMarque.getValue());
								}
						}
					}
				}
			}
			NoeudMarqueChoisi.setRadioRadius(NoeudMarqueChoisi.distance(capteurs.get(numSommetNMCandidat)));
			NoeudMarqueChoisi.setValue(NoeudMarqueChoisi.Consommation(capteurs.get(numSommetNMCandidat)));
			capteurs.get(numSommetNMCandidat).setMarked(true);
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
			if(s.isMarked()==true){ Conso+=s.getConsommation();s.setMarked(false);}

		}
		return Conso;
	}
	
}
