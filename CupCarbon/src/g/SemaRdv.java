package g;

public class SemaRdv {

	public int k = 0;
	private int m = 0;
	
	public SemaRdv(int v) {
		m = v;
		MinCalc.init();
	}
	
	public synchronized void a() {
		try {
			k++;
			if(k<m) wait();	
			System.out.println("-> "+MinCalc.getThread());
			MinCalc.init();
			k=0;
			notifyAll();			
		} catch (InterruptedException e) {e.printStackTrace();}
	}
		
}
