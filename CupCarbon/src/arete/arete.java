/**
		 * @author Zakaria
		 */
package arete;

import device.Device;

public class arete implements Comparable<arete>{

	private Device device1;
	private Device device2;
	private double distance;
	public arete(Device device1, Device device2, double distance) {
		super();
		this.device1 = device1;
		this.device2 = device2;
		this.distance = distance;
	}
	public arete() {
		super();
	}
	public Device getDevice1() {
		return device1;
	}
	public void setDevice1(Device device1) {
		this.device1 = device1;
	}
	public Device getDevice2() {
		return device2;
	}
	public void setDevice2(Device device2) {
		this.device2 = device2;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(arete o) {
		// TODO Auto-generated method stub
		if(this.distance < o.distance) return (-1); else return 1 ;
	}
	
	public int compareTo1(arete o) {
		// TODO Auto-generated method stub idée !!
		if(((this.device1.Consommation(device2)-this.device1.getValue())+(this.device2.Consommation(device1)-this.device2.getValue()))<((o.device1.Consommation(device2)-o.device1.getValue())+ (o.device2.Consommation(device1)-o.device2.getValue()))) return (-1); else return 1 ;
	}
	
}
