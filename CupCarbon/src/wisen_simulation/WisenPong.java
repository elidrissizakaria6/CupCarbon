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
		int i=0;
		while (i<500) {
			wisenSemaphorePong.P(i++);			
			wisenSimulation.eventExecutor();			
			wisenSemaphorePing.V();			
		}
	}
}
