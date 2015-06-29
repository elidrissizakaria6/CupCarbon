/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import cupcarbon.CupCarbonMap;
import device.Device;
import device.DeviceList;
import device.SensorNode;

public class Heuristique extends Thread {
	public int compteurCC=0;
	long temps;
	List<SensorNode> CC=new ArrayList<SensorNode>(); 

	
	public void run() {
		
		List<SensorNode> capteurs=DeviceList.getSensorNodes();
		algorithme(capteurs);
		
		
		
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));

	}



	public List<SensorNode> algorithme(List<SensorNode> capteurs) {
		long debut,fin;
		debut=System.currentTimeMillis();
		
		double valeurMin= Double.MAX_VALUE;
		//initialisation des noeuds
		for( SensorNode a : capteurs )
		{
			a.setValue(0);
			a.getComposantescnx().clear();
			a.ajouterComposantescnx(a);
			a.setRadioRadius(100);
		}
		double min=Double.MAX_VALUE;
		SensorNode v=null;
		for( SensorNode a : capteurs )
		{
			for(SensorNode voisin : a.getSensorNodeNeighborsZakaria()){
				if(voisin.Consommation(a)<min){
					min=voisin.Consommation(a);
					v=voisin;
				}
			}
			a.setValue(a.distance(v));
			min=Double.MAX_VALUE;
		}
		for( SensorNode a : capteurs )
		{
			a.setVisited(false);
			a.setRadioRadius(a.getValue());
			a.setValue(a.getConsommation());
		}
		
//		CupCarbonMap.saveHImage(1);
//		 Mettre à jour les composantes connexes 
		for( SensorNode a : capteurs )
		{
			CC.clear();
			if(a.isVisited()==false) {
				DFSCC(a);
				compteurCC++;
				a.getComposantescnx().addAll(CC);
				for(SensorNode b : CC){
					b.getComposantescnx().addAll(CC);
				}
			}
		}
		SensorNode S1 = null,S2 = null,a=null,b=null; int k=2;
		while(compteurCC!=1){
			for(int i=0;i<capteurs.size();i++){
				a=capteurs.get(i);
				for(int j=i;j<capteurs.size();j++){
					b=capteurs.get(j);
					if((!a.getComposantescnx().equals(b.getComposantescnx()))&&(a.distance(b)<=100)){
						if(((a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue()))<valeurMin){
							S1=a;
							S2=b;
							valeurMin=(a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue());
						}
					}
				}
			}
			if((S1!=null)&&(S2!=null)){
				S1.setRadioRadius(Math.max(S1.distance(S2), S1.getRadioRadius()));
				S1.setValue(S1.getConsommation());
				S2.setRadioRadius(Math.max(S2.distance(S1), S2.getRadioRadius()));
				S2.setValue(S2.getConsommation());
				majComposantecnx(S1,S2);
				compteurCC=compteurCC-1;
				valeurMin=Double.MAX_VALUE;
				Layer.getMapViewer().repaint();
		}
			else compteurCC=1;
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
		System.out.println("Le temps d'execution de L'heuristique, en Milliseconde = "+(temps));
		
		return capteurs;
	}

private void DFSCC( SensorNode capteur){
	
	capteur.setVisited(true);
	CC.add(capteur);
	for(SensorNode voisin : capteur.getSensorNodeNeighborsZakaria()){
			if(voisin.isVisited()==false){
				DFSCC(voisin);
			}
	}
}
	public double calculerPuissanceGlobale(List<SensorNode> capteurs){
		double puissance=0;
		for(SensorNode s : capteurs){
			 puissance+=s.getRadioRadius();
		}
		return puissance;
	}
	public double calculerComsommationGlobale(List<SensorNode> capteurs){
		double Conso=0;
		for(SensorNode s : capteurs){
			Conso+=s.getConsommation();
		}
		return Conso;
	}
	public void majComposantecnx(SensorNode S1, SensorNode S2){
		S1.getComposantescnx().addAll(S2.getComposantescnx());
		S2.getComposantescnx().addAll(S1.getComposantescnx());
		for(Device s : S1.getComposantescnx()){
			s.getComposantescnx().addAll(S1.getComposantescnx());
		}
	}
}
