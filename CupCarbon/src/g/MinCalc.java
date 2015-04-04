package g;

public final class MinCalc {

	public static int min = 10000000;
	public static UnThread th = null;
	
	public static void init() {
		min = 1000000;
		th = null;
	}
	
	public synchronized static void valeur(int v, UnThread th) {
		if(v < min) {
			MinCalc.th = th ;			
			min = v ;
		}
	}
	
	public static int getMin() {
		return min;
	}
	
	public static UnThread getThread() {
		return th;
	}
	
}
