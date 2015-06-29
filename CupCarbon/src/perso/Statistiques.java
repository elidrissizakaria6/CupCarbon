package perso;

import java.util.ArrayList;
import java.util.List;

import map.RandomDevices;
import device.DeviceList;

public class Statistiques extends Thread {

	MSTKRUSKAL mstkruskal=new MSTKRUSKAL();
	BIPAdapte bipAdapte=new BIPAdapte();
	Heuristique heuristique=new Heuristique();
	BorneInf borneInf=new BorneInf();
	BorneSup  borneSup=new BorneSup();
	LMST lmst=new LMST();
	GrapheRNG grapheRNG=new GrapheRNG();
	LBIP lbip=new LBIP();
	
	static List<String> NomsAlgos=new ArrayList<String>();
	static List<List<Double>> Resultats=new ArrayList<List<Double>>();
	static List<List<Long>> temps=new ArrayList<List<Long>>();
	Excel excel=new  Excel();
	public void run(int nbRepetition,int nbNoeud) {
			ExecuterAlgos(nbRepetition,nbNoeud);
	}
	public void ExecuterAlgos(int nbRepetition,int nbNoeud) {
		NomsAlgos=new ArrayList<String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
		    add("Borne Inferieure");
		    add("Heuristique");
		    add("Bip Adapté");
		    add("MST Kruskal");
		    add("LBIP");
		    add("LMST");
		    add("RNG");
		    add("Borne Superieur");
		}};
		for(int i = 0;i<nbRepetition;i++){
			DeviceList.clearNodes();
//			RandomDevices.addRandomSensorsWithoutMarkers(nbNoeud);
			RandomDevices.addRandomSensorsWithoutMarkersInGrid(nbNoeud, (int)Math.sqrt(nbNoeud*10), (int)Math.sqrt(nbNoeud*10));
			while(!TesterLaConnexite.algorithme(DeviceList.getSensorNodes())){
				try{
				DeviceList.clearNodes();
//				RandomDevices.addRandomSensorsWithoutMarkers(nbNoeud);
				RandomDevices.addRandomSensorsWithoutMarkersInGrid(nbNoeud, (int)Math.sqrt(nbNoeud*10), (int)Math.sqrt(nbNoeud*10));
				}catch(Exception e){System.out.println("Stats Exception");}
				}
			List<Double> Resultat=new ArrayList<Double>();
			List<Long> tmp=new ArrayList<Long>();
			borneInf.algorithme(); 
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			heuristique.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			bipAdapte.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			mstkruskal.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			lbip.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			lmst.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			grapheRNG.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			borneSup.algorithme(DeviceList.getSensorNodes());
			Resultat.add(CalculerPuissanceConsommation.calculerComsommationGlobale());
			Resultats.add(Resultat);
			tmp.add(borneInf.temps);
			tmp.add(heuristique.temps);
			tmp.add(bipAdapte.temps);
			tmp.add(mstkruskal.temps);
			tmp.add(lbip.temps);
			tmp.add(lmst.temps);
			tmp.add(grapheRNG.temps);
			tmp.add(borneSup.temps);
			temps.add(tmp);
		}
		excel.writeFile(NomsAlgos, Resultats,temps);
	}
}
