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

package device;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import map.Layer;
import utilities.MapCalc;


public abstract class MobileG extends DeviceWithoutRadio {

	protected int gpsId = 0 ;
	protected static int gpsNbr = 0 ; 
	
	public MobileG() {
	}
	
	public MobileG(double x, double y, double rayon, String gpsFileName) {
		super(x,y,rayon);
		mobile = true ;		
//		if(gpsFileName.equals("")) {
//			gpsId = ++ gpsNbr ;
//			this.gpsFileName = "gps"+gpsId+".gps";
//		}
//		else
		this.gpsFileName = gpsFileName ;
	}
	
	public void setGPSFileName(String fileName) {
		gpsFileName = fileName ;
	}
	
	@Override
	public String getGPSFileName() {
		return gpsFileName ;
	}
	
	public abstract void draw(Graphics g) ;
	
	@Override
	public void run() {
		state = this.getState();
		selected = false;
		underSimulation = true;
		// ------ Mobile -----
		double totalDistance = 0;
		boolean firstTime = true;
		FileInputStream fis;
		BufferedReader b = null;
		String[] ts;
		String s;
		double x2, y2;
		try {
			fis = new FileInputStream(gpsFileName);
			b = new BufferedReader(new InputStreamReader(fis));
			underSimulation = true;
			String desc_str = b.readLine();
			String from_str = b.readLine();
			String to_str = b.readLine();
			System.out.println("Description : " + desc_str);
			System.out.println("From : " + from_str);
			System.out.println("To : " + to_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ------ END Mobile ----
		long tmpTime = -3600000;
		long cTime = 0;
		long toWait = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

		try {
			while (((s = b.readLine()) != null)) {
				x2 = x;
				y2 = y;
				ts = s.split(" ");
				cTime = simpleDateFormat.parse(ts[0]).getTime();
				toWait = cTime - tmpTime;
				tmpTime = cTime;
				x = Double.parseDouble(ts[1]);
				y = Double.parseDouble(ts[2]);
				if (firstTime)
					firstTime = false;
				else {
					// System.out.println((int) MapCalc.distance(x, y, x2,
					// y2));
					totalDistance += MapCalc.distance(x, y, x2, y2);
				}
				try {
					Thread.sleep(toWait / 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Layer.getMapViewer().repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(totalDistance);
		underSimulation = false;
		thread = null;
		try {
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public void run2() {		
		selected = false ;
		try {			
			FileInputStream fis;
			fis = new FileInputStream(gpsFileName);
			BufferedReader b = new BufferedReader(new InputStreamReader(fis));
			underSimulation = true ;
			String [] ts ;
			String s ;
			System.out.println("Description : "+b.readLine());
			System.out.println("From : "+b.readLine());
			System.out.println("To : "+b.readLine());
			while((s=b.readLine())!=null) {
			//int k = 0;
			//while((k++)<3000) {
				ts = s.split(" ");
				x = Double.parseDouble(ts[0]);
				y = Double.parseDouble(ts[1]);
				//x+=.0001;
				//y-=.0001;
				//SensorSetCover.sensorTargetSetCover() ;
				Layer.getMapViewer().repaint();
				try {
					//Thread.sleep(10);
					Thread.sleep(400);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			Layer.getMapViewer().repaint();
			underSimulation = false ;
			thread = null ;
			b.close() ;
			fis.close() ;
		} catch (Exception e1) {}
	}

	@Override
	public double getCaptureUnitRadius() {
		return 0 ;
	}
	
	@Override
	public double getRadioRadius() {
		return 0 ;
	}
	
}
