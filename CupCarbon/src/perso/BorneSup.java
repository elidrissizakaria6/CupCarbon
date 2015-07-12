/**
		 * @author Zakaria
		 */
package perso;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import map.Layer;
import device.DeviceList;
import device.SensorNode;

public class BorneSup extends Thread {
	long temps;
	public void run() {
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
		
		algorithme(capteurs);
		
		
		
		DecimalFormat df=new DecimalFormat("####.##" );
		final JFrame parent = new JFrame();
		JOptionPane.showMessageDialog(parent, "La puissance globale = "+df.format(calculerComsommationGlobale(capteurs)));
		Layer.mapViewer.repaint();
	}

	public List<SensorNode> algorithme(List<SensorNode> capteurs) {
		
		long debut,fin;
		debut=System.currentTimeMillis();
		
		int portee=100;
		while((TesterLaConnexite.algorithme(capteurs)==true)&&(portee>0)&&(capteurs.size()>1)){
			for( SensorNode a : capteurs )
			{
				a.setRadioRadius(portee);
				Layer.mapViewer.repaint();
			}
			portee=portee-1;
			
		}
		for( SensorNode a : capteurs ){
			if(capteurs.size()>1){
				a.setValue(0);
				a.setRadioRadius(portee+2);
			}
		}
		Layer.mapViewer.repaint();
		for(SensorNode s : capteurs){
			List<SensorNode> union=s.getSensorNodeNeighborsZakaria();
			union.add(s);
			while((TesterLaConnexite.algorithme(union)&&(capteurs.size()>1))){
				s.setRadioRadius(s.getRadioRadius()-1);
				Layer.mapViewer.repaint();
			}
			if(capteurs.size()>1) s.setRadioRadius(s.getRadioRadius()+1);
				
		}
		
		Layer.mapViewer.repaint();
		fin=System.currentTimeMillis();
		temps=fin-debut;
		System.out.println("Le temps d'execution de la borne sup, en Milliseconde = "+(fin-debut));
		
		return capteurs;
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
}
