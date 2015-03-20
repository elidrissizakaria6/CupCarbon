package simulation;

import device.DeviceList;

public class FaultInjector extends Thread {

	private boolean loop = true; 
	
	@Override
	public void run() {
		int n = DeviceList.size();
		double p ;
		while(loop) {
			for (int i = 0; i < n; i++) {
				p=Math.random();				
				if(p<0.00005) {
					DeviceList.getNodes().get(i).setFaulty(true);
				}
//				p=Math.random();				
//				if(p<0.00001) {
//					DeviceList.getNodes().get(i).setFaulty(false);
//				}
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopInjection() {
		loop = false;
	}
	
}
