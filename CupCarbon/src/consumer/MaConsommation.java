/**
* @author Zakaria
**/
package consumer;

import device.Device;

public class MaConsommation extends AConsumption {
	protected static int coefficientAtten=1;
	protected static int Cte=0;
	@Override
	public double getConsumedUnit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getConsumedUnit(double n) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static double getConsommation(Device device){
		if(device.getRadioRadius()!=0){
			if(Cte==0) return Math.pow(device.getRadioRadius(),coefficientAtten);
			else return (Math.pow(device.getRadioRadius(),coefficientAtten)+Math.pow(10,Cte));
		}
		else return 0D;
	}
	public static double Consommation(double distance){
		if(distance!=0){
			if(Cte==0) return Math.pow(distance,coefficientAtten);
			else return (Math.pow(distance,coefficientAtten)+Math.pow(10,Cte));
		}
		else return 0D;
	}

	public static int getCoefficientAtten() {
		return coefficientAtten;
	}

	public static void setCoefficientAtten(int coefficientAtten) {
		MaConsommation.coefficientAtten = coefficientAtten;
	}

	public static int getCte() {
		return Cte;
	}

	public static void setCte(int double1) {
		Cte = double1;
	}
	
	
}
