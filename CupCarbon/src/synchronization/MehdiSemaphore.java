/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013 Ahcene Bounceur
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

package synchronization;

public class MehdiSemaphore {

	private int resourcesNumber;

	// =============================================================
	public MehdiSemaphore(int resourcesnumber) {
		resourcesNumber = resourcesnumber;
	}

	// =============================================================
	synchronized public void P() {
		boolean Retour;
		resourcesNumber--;
		if (resourcesNumber < 0) {
			do {
				Retour = false;
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Oups : error");
					Retour = true;
				}
			} while (Retour);
		}
	}

	// =============================================================
	synchronized public void V() {
		resourcesNumber++;
		if (resourcesNumber <= 0) {
			notify();
		}
	}
	// =============================================================
}
