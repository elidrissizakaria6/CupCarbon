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
			JOptionPane.showMessageDialog(parent, "La puissance globale = "+calculerPuissanceGlobale(capteurs)+"\n"
					+ "La consommation globale = "+calculerComsommationGlobale());
	}
	
	public static double calculerPuissanceGlobale(List<SensorNode> capteurs)
	{
		double puissance=0;
		for(SensorNode s : capteurs)
		{
			 puissance+=s.getRadioRadius();
		}
		return puissance;
	}
	public static double calculerComsommationGlobale()
	{
		List<SensorNode> capteurs=DeviceList.getSensorNodes();
		double Conso=0;
		for(SensorNode s : capteurs)
		{
			Conso+=(s.getConsommation());
		}
		return Conso;
	}
}
