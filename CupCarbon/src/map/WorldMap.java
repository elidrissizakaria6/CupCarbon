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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *----------------------------------------------------------------------------------------------------------------*/

package map;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

import wisen_simulation2.Simulation;

public class WorldMap extends JXMapKit {

	private static final long serialVersionUID = 1L;
	public static Layer layer;
	final int max = 19;

	public WorldMap() {
		// TileFactoryInfo info = new TileFactoryInfo(0,max,max,256, true,
		// true,"file:/myLocalMapServer/tiles","x","y","z") {
		TileFactoryInfo info = new TileFactoryInfo(0, max, max, 256, true,
				true, "http://tile.openseamap.org", "x", "y", "z") {
			// TileFactoryInfo info = new TileFactoryInfo(0,max,max,256, true,
			// true,"http://www.openseamap.org/map","x","y","z") {
			// TileFactoryInfo info = new TileFactoryInfo(0,max,max,256, true,
			// true,"file:/myLocalMapServer/tiles","x","y","z") {
			
			public String getTileUrl(int x, int y, int zoom) {
				zoom = max - zoom;
				 //return this.baseURL +"/"+zoom+"/"+x+"/"+y+".png";
				 //return "http://otile1.mqcdn.com/tiles/1.0.0/sat/" + zoom + "/" + x + "/" + y + ".jpg";
				 return "http://otile1.mqcdn.com/tiles/1.0.0/osm/" + zoom + "/" + x + "/" + y + ".jpg";
				// return "http://a.tile.openstreetmap.org/"+zoom+"/"+x+"/"+y+".png";
				//return "http://localhost:8888/cupcarbon/tiles/" + zoom + "/"
				//		+ x + "/" + y + ".png";
				// return
				// "http://tile.stamen.com/terrain-background/"+zoom+"/"+x+"/"+y+".png";
				// return
				// "http://a.tile2.opencyclemap.org/transport/"+zoom+"/"+x+"/"+y+".png";
				//return
				//"http://tiles.openseamap.org/seamark/"+zoom+"/"+x+"/"+y+".png";
				// return this.baseURL + x+"_"+y+"_"+"z"+".jpg";
				// return
				// "http://a.tile.opencyclemap.org/cycle/"+zoom+"/"+x+"/"+y+".png";
				// return
				// "http://a.tile2.opencyclemap.org/transport/"+zoom+"/"+x+"/"+y+".png";
			}
		};

		
		
		info.setDefaultZoomLevel(19);

		// WMSService wms = new WMSService();
		// wms.setLayer("BMNG");
		// wms.setBaseUrl("http://wms.jpl.nasa.gov/wms.cgi?");
		// wms.setBaseUrl("http://www.geobasisdaten.niedersachsen.de/bestand?");

		// TileFactory tf = new WMSTileFactory(wms);

		TileFactory tf = new DefaultTileFactory(info);

		setTileFactory(tf);

		
		
		// setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);

		// setDefaultProvider(org.jdesktop.swingx.JXMapKit.DefaultProviders.OpenStreetMaps);
		setDataProviderCreditShown(true);
		setName("CupCarbon Map");
		// setCenterPosition(new
		// GeoPosition(43.461142978339005,-3.830108642578125));//Santander
		//setCenterPosition(new GeoPosition(36.75140145092604, 5.055642127990723));// Bejaia
		// setCenterPosition(new GeoPosition(25.14, 55.2)); //Dubai
		// setCenterPosition(new GeoPosition(47.720520033704954,
		// -3.3709144592285156));//Lorient
		//setCenterPosition(new GeoPosition(48.39188295873048, -4.44371223449707)); // Brest
		setCenterPosition(new GeoPosition(48.40467657851382,-4.501540660858154)); // Brest 2
		// setCenterPosition(new
		// GeoPosition(48.58273977037357,-3.8297653198242188));//Morlaix
		setZoom(2);
		layer = new Layer(getMainMap());
	}

	public static Layer getLayer() {
		return layer;
	}
	
	public static void deSimulation() {
		System.out.println("DE simulation");
	}
	
	public static void gpuSimulation() {
		System.out.println("GPU simulation");
	}
	
	public static void simulate() {
		layer.simulate();
	}

	public static void comSimulate(String name, String log, long v1, long v2,
			long v3) {
		Simulation simulation = new Simulation(name, log);
		simulation.setSimulationDelay(v1);
		simulation.setSimulationLogicDelay(v2);
		simulation.setStep(v3);
		simulation.startSimulation();
	}	
	
	public static void comSimulate(String name, String log) {
		Simulation simulation = new Simulation(name, log);
		simulation.startSimulation();
	}

	public static void simulateAll() {
		layer.simulateAll();
	}
	
	public static void simulateSensors() {
		layer.simulateSensors();
	}
	
	public static void simulateMobiles() {
		layer.simulateMobiles();
	}

	public static void addNodeInMap(char c) {
		layer.addNodeInMap(c);
	}

	public static void loadNodes() {
		layer.loadNodes();
		// (new Layer()).loadNodes();
	}

	public static void loadCityNodes() {
		layer.loadCityNodes();
	}

	public static void setSelectionOfAllNodes(boolean selection, int type,
			boolean addSelection) {
		layer.setSelectionOfAllNodes(selection, type, addSelection);
	}

	public static void invertSelection() {
		layer.invertSelection();
	}

	public static void setSelectionOfAllMarkers(boolean selection, int type,
			boolean addSelection) {
		layer.setSelectionOfAllMarkers(selection, type, addSelection);
	}
}
