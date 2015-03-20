package perso;

import java.util.List;

import device.Device;
import device.DeviceList;
import device.Sensor;

public class ExampleClass extends Thread {
		
	protected List<Device> nodes; 
	
	@Override
	public void run() {
		// Get the node list
		nodes = DeviceList.getNodes();

		Sensor s1 = (Sensor) nodes.get(0);
		s1.loadRouteFromFile();
		s1.fixori();
		int i=0;
		while(s1.hasNext()) {
			System.out.println(i++);
			s1.moveToNext(true, 50);
		}
		
		System.out.println("finish");
	}
	
	public void displayNumberOfSensors() {
		System.out.println(nodes.size());
	}
	
	public void displaySensorList() {
		for(int i=0; i<DeviceList.size(); i++) {
			System.out.println(nodes.get(i).getId());
		}
	}
	
	public void delay(int t) {
		try {
			sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
