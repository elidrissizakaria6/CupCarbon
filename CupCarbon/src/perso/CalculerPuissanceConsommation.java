package perso;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import device.DeviceList;
import device.SensorNode;

public class CalculerPuissanceConsommation extends Thread {

	public void run() {
		List<SensorNode> capteurs = DeviceList.getSensorNodes();
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
