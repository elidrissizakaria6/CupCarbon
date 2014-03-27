package wisen_simulation;

public class WisenSemaphore {

	private boolean b = false;

	public WisenSemaphore(boolean b) {
		this.b = b;
	}

	public synchronized void P() {
		boolean waitNonExe;
		b = !b;
		if (b) {
			do {
				waitNonExe = false;
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Oups : error");
					waitNonExe = true;
				}
			} while (waitNonExe);
		}
	}

	public synchronized void V() {
		b = !b;
		if (!b) {
			notify();
		}
	}
}
