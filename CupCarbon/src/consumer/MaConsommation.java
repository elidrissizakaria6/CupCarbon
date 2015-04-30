/**
* @author Zakaria
**/
package consumer;

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
	public static double getConsommation(double porte){
		return Math.pow(porte,coefficientAtten)+Cte;
	}
	public static double Consommation(double distance){
		return Math.pow(distance,coefficientAtten)+Cte;
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
