package perso;

import java.util.List;

import device.Device;
import device.DeviceList;
import device.SensorNode;

public class ExampleClass extends Thread {
		
	protected List<Device> nodes; 
	
	@Override
	public void run() {
		// Get the node list
		nodes = DeviceList.getNodes();
		SensorNode s1 = new SensorNode();
		try{
		s1 = (SensorNode) nodes.get(0);
		}catch(IndexOutOfBoundsException e){System.out.println("une erreur pas tres grave");}
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
		for(Device node : nodes) {
			System.out.print(node.getId()+" : " );
			System.out.println(node.getX()+" "+node.getY());
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
