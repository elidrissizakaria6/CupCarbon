/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: OSM based Wireless Sensor Network design and simulation tool
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2015 Ahcene Bounceur
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

package solver;

import java.io.BufferedReader;
import java.io.FileReader;

import map.Layer;
import device.Device;
import device.DeviceList;
import device.Sensor;


/**
 * @author Ahcene Bounceur
 * @author Ali Benzerbadj
 * @version 1.0
 */
public class GpsrB extends Thread {

	@Override
	public void run() {
		try {
			
			//FileInputStream fis = new FileInputStream("gpsrb.txt");
			FileReader fr = new FileReader("gpsrb.txt");
			BufferedReader br = new BufferedReader(fr);
			String s ;	
			double x ;
			double y ;
			while((s = br.readLine()) != null) {
			//int k = 0;
			//while(((s = br.readLine()) != null) && (k++<2000)) {
				String [] ss = s.split(" ");
				if(ss.length>30) {					
					if(ss[30].equals("initial")) {
						x = (Double.parseDouble(ss[33].split(":")[0])/8000.)-4.44945216178894;
						y = (Double.parseDouble(ss[33].split(":")[1])/8000.)+48.389923740704795;
						//System.out.println("("+x+", "+y+")");
						DeviceList.add(new Sensor(y,x,20,50));
						Layer.getMapViewer().repaint();
					}
					
				}
				if(ss.length>20) {
					if(ss[17].equals("hop")) {
						int nextn = Integer.parseInt(ss[18].split(",")[0]);
						//System.out.println(nextn);
						Device d = Layer.getDeviceList().get(nextn);
						d.setAlgoSelect(true);
						Layer.getMapViewer().repaint();
						Thread.sleep(500);
					}
				}
			}
			fr.close();
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
