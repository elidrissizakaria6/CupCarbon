package wisen_simulation;

public class WisenPong extends Thread {
	
	private WisenSemaphore wisenSemaphorePing = null;
	private WisenSemaphore wisenSemaphorePong = null;
	private WisenSimulation wisenSimulation = null;

	public WisenPong(WisenSemaphore wisenSemaphorePing,
			WisenSemaphore wisenSemaphorePong, WisenSimulation wisenSimulation) {
		this.wisenSemaphorePing = wisenSemaphorePing;
		this.wisenSemaphorePong = wisenSemaphorePong;
		this.wisenSimulation = wisenSimulation;
	}

	@Override
	public void run() {
		//
		boolean b=true;
		while (b) {
			wisenSemaphorePong.P();			
			b = wisenSimulation.eventExecutor();
			wisenSemaphorePing.V();			
		}
		wisenSemaphorePing.V();
		wisenSemaphorePong.V();
	}
}
