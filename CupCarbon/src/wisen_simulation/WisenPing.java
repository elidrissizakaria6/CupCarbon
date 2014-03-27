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
		int i=0;
		while (i<500) {
			wisenSemaphorePing.P(i++);
			wisenSimulation.eventGenerator();			
			wisenSemaphorePong.V();
		}
	}
}
