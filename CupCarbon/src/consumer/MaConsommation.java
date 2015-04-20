/**
* @author Zakaria
**/
package consumer;

public class MaConsommation extends AConsumption {
	protected static double coefficientAtten=1;
	protected static double Cte=0;
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

	public static double getCoefficientAtten() {
		return coefficientAtten;
	}

	public static void setCoefficientAtten(double coefficientAtten) {
		MaConsommation.coefficientAtten = coefficientAtten;
	}

	public static double getCte() {
		return Cte;
	}

	public static void setCte(double cte) {
		Cte = cte;
	}
	
	
}
