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

package captureunit;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import map.Layer;
import utilities.MapCalc;
import utilities.UColor;
import consumer.AConsumption;
import device.Device;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class CaptureUnit extends AConsumption implements KeyListener, Cloneable {

	protected double radius = 10;
	protected double x;
	protected double y;
	protected Device node;
	protected int service = 14;

	/**
	 * Constructor 1 : radius is equal to 10 meter
	 * @param x Position of the capture unit on the map
	 * @param y Position of the capture unit on the map
	 * @param node which is associated to this capture unit
	 */
	public CaptureUnit(double x, double y, Device node) {
		this.x = x;
		this.y = y;
		this.node = node;
		Layer.getMapViewer().addKeyListener(this);
	}

	/**
	 * Constructor 3 : radius is equal to 10 meter
	 * @param x Position of the capture unit on the map
	 * @param y Position of the capture unit on the map
	 * @param cuRadius the value of the radius 
	 * @param node which is associated to this capture unit
	 */
	public CaptureUnit(double x, double y, double cuRadius, Device node) {
		this(x, y, node);
		radius = cuRadius;
	}

	/**
	 * @return radius of the capture unit
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Change the position of the capture unit
	 */
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Draw the capture unit
	 */
	public void draw(Graphics g, int mode, boolean detection) {
		if (!detection)
			g.setColor(UColor.WHITE_TRANSPARENT);
		if (detection)
			g.setColor(UColor.JAUNE_SENSOR);
		if (mode == 0)
			g.fillOval((int) x - MapCalc.radiusInPixels(radius), (int) y
					- MapCalc.radiusInPixels(radius),
					MapCalc.radiusInPixels(radius) * 2,
					MapCalc.radiusInPixels(radius) * 2);
		g.drawOval((int) x - MapCalc.radiusInPixels(radius),
				(int) y - MapCalc.radiusInPixels(radius),
				MapCalc.radiusInPixels(radius) * 2,
				MapCalc.radiusInPixels(radius) * 2);
	}

	/**
	 * @return the value of the unit consumed 
	 */
	@Override
	public double getConsumedUnit() {
		return consomUnit;
	}

	@Override
	public double getConsumedUnit(double unit) {
		return consomUnit * unit;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (node.isSelected()) {
			if (key.getKeyChar() == ')') {
				radius += 5;
				Layer.getMapViewer().repaint();
			}
			if (key.getKeyChar() == '(') {
				radius -= 5;
				Layer.getMapViewer().repaint();
			}
		}
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	/**
	 * Coming soon
	 */
	public void setNode(Device node) {
		this.node = node;
	}

	/**
	 * Clone the capture unit
	 */
	@Override
	public CaptureUnit clone() throws CloneNotSupportedException {
		CaptureUnit newCU = (CaptureUnit) super.clone();
		Layer.getMapViewer().addKeyListener(newCU);
		return newCU;
	}
}
