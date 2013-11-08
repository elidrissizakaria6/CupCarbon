package insects;

import java.awt.Color;
import java.awt.Graphics;

import map.Layer;

public class InsectCc implements Runnable {

	private Thread thread = null;
	private InsectGroup insects;	

	public InsectCc() {
		insects = new InsectGroup(200);
	}

	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		insects.draw(g);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				insects.move();
				Layer.getMapViewer().repaint();
				Thread.sleep(100); // interval between steps
			} catch (InterruptedException e) {
			}
		}
	}
	
}
