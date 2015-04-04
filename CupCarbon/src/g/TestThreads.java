package g;


public class TestThreads {

	public static void main(String[] args) {
		SemaRdv s = new SemaRdv(7);
		UnThread t1 = new UnThread(s,1) ;
		UnThread t2 = new UnThread(s,2) ;
		UnThread t3 = new UnThread(s,3) ;
		UnThread t4 = new UnThread(s,4) ;
		UnThread t5 = new UnThread(s,5) ;
		UnThread t6 = new UnThread(s,6) ;
		UnThread t7 = new UnThread(s,7) ;
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
	}
	
}
