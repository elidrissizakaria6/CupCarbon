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

package cupcarbon;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import map.WorldMap;

/**
 * @author Ahcene Bounceur
 * @author Kamal Mehdi
 * @author Lounis Massinissa
 * @version 1.0
 */
public class CupCarbonMap extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CupCarbonMap frame = new CupCarbonMap();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CupCarbonMap() {
		super("CupCarbon Map");
		WorldMap map = new WorldMap();

		map.getMainMap().setLoadingImage(
				Toolkit.getDefaultToolkit()
						.getImage(CupCarbonParameters.IMGPATH + "mer.png"));
		map.getZoomSlider().setSnapToTicks(false);
		map.getZoomSlider().setPaintTicks(false);
		map.getZoomInButton().setBackground(Color.LIGHT_GRAY);
		map.getMiniMap().setLoadingImage(
				Toolkit.getDefaultToolkit()
						.getImage(CupCarbonParameters.IMGPATH + "mer.png"));
		setContentPane(map);
		setBounds(0, 0, 800, 500);
		map.setBorder(BorderFactory.createLoweredBevelBorder());
		setMaximizable(true);
		setFrameIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "logo_cap_carbon.png"));
		setResizable(true);
		setIconifiable(true);
	}

}
