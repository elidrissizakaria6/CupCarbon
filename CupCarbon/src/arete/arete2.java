/**
		 * @author Zakaria
		 */
package arete;

import java.util.Random;

import device.Device;

public class arete2 implements Comparable<arete2>{

	private Device device1;
	private Device device2;
	private double distance;
	public arete2(Device device1, Device device2, double distance) {
		super();
		this.device1 = device1;
		this.device2 = device2;
		this.distance = distance;
	}
	public arete2() {
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

	public int compareTo(arete2 o) {
		// TODO Auto-generated method stub
		if(this.distance < o.distance){
			int val=Math.abs((int)this.distance-(int)o.distance)*20+1;
			System.out.println("prob="+val);
			boolean prob = new Random().nextInt(val)==0;
			if(prob==true){
				return TesterZakaria(o);
			}
			else return (-1);
		}
		else if(this.distance > o.distance){
			int val=Math.abs((int)this.distance-(int)o.distance)*20+1;
			System.out.println("prob="+val);
			boolean prob = new Random().nextInt(val)==0;
			if(prob==true){
				return TesterZakaria(o);
			}
			else return 1;
			
		}
		else{
			return TesterZakaria(o);
		}
		
	}
		
	
	private int TesterZakaria(arete2 o)
	{
		double ar1min1 = 0,ar1min2=0;
		double ar2min1 = 0,ar2min2=0;
		for(Device Capteur : this.device1.getNeighbors())
		{
			ar1min1+=this.device1.Consommation(Capteur);
		}
		for(Device Capteur : this.device2.getNeighbors())
		{
			ar1min2+=this.device1.Consommation(Capteur);
		}
		
		for(Device Capteur : o.device1.getNeighbors())
		{
			ar2min1+=o.device1.Consommation(Capteur);
		}
		for(Device Capteur : o.device2.getNeighbors())
		{
			ar2min2+=o.device1.Consommation(Capteur);
		}
		if((ar1min1+ar1min2)<(ar2min1+ar2min2))
		{
			return -1;
		}
		else return 1;
	}
	
	
}
