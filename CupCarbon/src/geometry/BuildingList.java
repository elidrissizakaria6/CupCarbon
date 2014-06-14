package geometry;

import java.awt.Graphics;
import java.util.LinkedList;

public class BuildingList {

	private LinkedList<Building> buildingList;
	
	public BuildingList() {
		buildingList = new LinkedList<Building>();
	}
	
	public void add(Building building) {
		buildingList.add(building);
	}
	
	public void draw(Graphics g) {
		for(Building building : buildingList) {
			building.draw(g);
		}
	}
	
}
