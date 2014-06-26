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

package tracking;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import device.Device;
import device.Sensor;

/**
 * 
 * @author Khaoula
 * 
 * KH
 *
 */
public class TrackingTasksManager {

	private List<TrackingTask> tasks = new ArrayList<TrackingTask>();
	
	private boolean underSimulation = false;
	
	private boolean realTracking = false;
	
	public TrackingTasksManager(){
	}
	
	public TrackingTasksManager(boolean realTracking){
		this.realTracking = realTracking;
	}

	public void draw(Graphics g) {
		if(underSimulation){
			for(TrackingTask task : tasks){
				task.draw(g);
			}
		}
	}
	
	public void addTask(Sensor tracker , Device target){
		String targetId = target.getNodeIdName().concat("");
		String trackerId = tracker.getNodeIdName().concat("");
		addTask(trackerId, targetId);
	}
	
	public void addTask(String trackerId , String targetId){
		TrackingTask task = new TrackingTask(trackerId, targetId,realTracking);
		tasks.add(task);	
	}

	public void startTraking() {
		underSimulation = true;
		for(TrackingTask task: tasks){
			task.start();
		}
	}

	public void stopTracking() {
		underSimulation = false;
		for(TrackingTask task: tasks){
			task.stop();
		}
		clearTasks();
	}
	
	public void clearTasks(){
		tasks.clear();
	}

}