package g;

import java.util.Random;

public class UnThread extends Thread {
	
	private SemaRdv s = null;
	private int id = 0;
	private Random r = new Random();
	private int x = -1;
	
	public UnThread(SemaRdv s, int id) {
		this.id = id ;
		this.s = s ;
	}
	
	public void run() {
		for(int i=0; i<10; i++) {
			x = r.nextInt(10);
			MinCalc.valeur(x, this);
			System.out.println("Th"+id+" : "+x);
			s.a();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		return "Th"+id+" ("+x+")";
	}
	
}
