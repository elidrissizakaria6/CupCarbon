package wisen_simulation;

public class WisenPing extends Thread {
	private WisenSemaphore wisenSemaphorePing = null;
	private WisenSemaphore wisenSemaphorePong = null;
	private WisenSimulation wisenSimulation = null;
	

	public WisenPing(WisenSemaphore wisenSemaphorePing,
			WisenSemaphore wisenSemaphorePong, WisenSimulation wisenSimulation) {
		this.wisenSemaphorePing = wisenSemaphorePing;
		this.wisenSemaphorePong = wisenSemaphorePong;
		this.wisenSimulation = wisenSimulation;
	}

	@Override
	public void run() {
		while (true) {			
			wisenSemaphorePing.P();
			wisenSimulation.eventGenerator();			
			wisenSemaphorePong.V();
		}
	}
}
