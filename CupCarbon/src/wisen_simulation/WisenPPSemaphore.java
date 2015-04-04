/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2014 Ahcene Bounceur
 * ----------------------------------------------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package wisen_simulation;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @version 1.0
 */
public class WisenPPSemaphore {

	private boolean b = false;

	public WisenPPSemaphore(boolean b) {
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
