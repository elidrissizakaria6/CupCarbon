/**
		 * @author Zakaria
		 */
package perso;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import device.Device;
import device.DeviceList;
import device.SensorNode;

public class ExampleClass extends Thread {
	public int compteurCC;
	List<SensorNode> CC=new ArrayList<SensorNode>(); 
	public void run() {
		List<SensorNode> capteurs;
		long debut,fin;
		debut=System.currentTimeMillis();
		capteurs = DeviceList.getSensorNodes();

		algorithme(capteurs);
		
		fin=System.currentTimeMillis();
		System.out.println("Le temps d'execution de L'heuristique, en Milliseconde = "+(fin-debut));
		
		final JFrame parent = new JFrame();
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale(capteurs));

	}


	public List<SensorNode> algorithme(List<SensorNode> capteurs) {
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
		Device v=new SensorNode();
		for( SensorNode a : capteurs )
		{
			for(int i=0;i<a.getSensorNodeNeighborsZakaria().size();i++){
				if(a.getSensorNodeNeighborsZakaria().get(i).Consommation(a)<min){
					min=a.getSensorNodeNeighborsZakaria().get(i).Consommation(a);
					v=a.getSensorNodeNeighborsZakaria().get(i);
				}
			}
			if(min<100){
			a.setValue(min);
			v.setValue(min);
			}
			min=Double.MAX_VALUE;
		}
		for( SensorNode a : capteurs )
		{
			a.setVisited(false);
			a.setRadioRadius(a.getValue());
		}
		//ne laisse pas de CC composé d'un seul capteur
		for( SensorNode a : capteurs ){
			a.setVisited(false);
			if(a.getComposantescnx().size()==1){
				a.getSensorNodeNeighbors().get(0).setRadioRadius(Math.max(a.getRadioRadius(), a.getSensorNodeNeighbors().get(0).getRadioRadius()));
			}
		}
		 //Mettre à jour les composantes connexes
		for( SensorNode a : capteurs )
		{
			CC.clear();
			if(a.isVisited()==false) {
			DFSCC(a);
			a.getComposantescnx().addAll(CC);
			for(SensorNode b : CC){
				b.getComposantescnx().addAll(CC);
			}
			}
		}
		for( SensorNode a : capteurs ){
			a.setVisited(false);
		}
		for( SensorNode a : capteurs ){
//			System.out.println("La compo cx de "+a.getId()+" est "+ a.getComposantescnx().toString());
			if(a.isVisited()==false) {
				DFS(a);
				compteurCC++;
			}
		}
		System.out.println(compteurCC);
		SensorNode S1 = null,S2 = null;
		while(compteurCC!=1){
			
			for( SensorNode a : capteurs ){
				for(SensorNode b : capteurs ){
					if(!a.getComposantescnx().equals(b.getComposantescnx())){
						if(((a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue()))<valeurMin){
							S1=a;S2=b;
							valeurMin=(a.Consommation(b)-a.getValue())+(b.Consommation(a)-b.getValue());
						}
					}
				}
			}
//			System.out.println(S1.getId()+" "+S2.getId());
			S1.setRadioRadius(Math.max(S1.distance(S2), S1.getRadioRadius()));
			S2.setRadioRadius(Math.max(S2.distance(S1), S2.getRadioRadius()));
			S2.setValue(S2.Consommation(S1));
			S1.setValue(S1.Consommation(S2));
			majComposantecnx(S1,S2);
			compteurCC=compteurCC-1;
			valeurMin=Double.MAX_VALUE;
			Layer.getMapViewer().repaint();
		}
		return capteurs;
	}
private void DFS( SensorNode capteur){
		
		capteur.setVisited(true);
		for(SensorNode voisin : capteur.getSensorNodeNeighborsZakaria()){
				if(voisin.isVisited()==false){
					DFS(voisin);
				}
		}
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
	public double calculerPuissanceGlobale(List<SensorNode> capteurs)
	{
		double puissance=0;
		for(SensorNode s : capteurs)
		{
			 puissance+=s.getRadioRadius();
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
