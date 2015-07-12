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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
	public static WorldMap map = null ;
	
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
		map = new WorldMap();
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
		//zakria
		//lancement de l'application en mode White Plan
		CacherZoomEtMiniMap();
		//zakaria
	}
	
	public static WorldMap getMap() {
		return map ;
	}
	
	public void infos() {
		System.out.println("->"+this.getSize());
	}
	
	public static void saveHImage(int i) {
		WorldMap map= CupCarbonMap.map;
	    BufferedImage img = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
	    map.paint(img.getGraphics());
	    try {
	        ImageIO.write(img, "png", new File("C:/Users/Public/Documents/sample"+i+".png"));
	        System.out.println("panel saved as image");

	    } catch (Exception e) {
	        System.out.println("panel not saved" + e.getMessage());
	    }
	}
	public void saveHDImage(WorldMap map2,String pathFile) {
		WorldMap map= map2;
		Dimension original = map.getSize();

		System.out.println("Original = " + original);

		int width = map.getWidth() ;
		int height = map.getHeight() ;
		map.setSize(width, height);
		map.doLayout();
	    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    
	    Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		map.print(g2d);
		g2d.dispose();
	    
	    map.paint(img.getGraphics());
	    map.setSize(original);
		map.doLayout();
	    try {
	        ImageIO.write(img, "png", new File(pathFile));
	        System.out.println("panel saved as image");

	    } catch (Exception e) {
	        System.out.println("panel not saved" + e.getMessage());
	    }
	    
	}
	public void saveImage(WorldMap map2){
		WorldMap map= map2;
		Dimension original = map.getSize();

		System.out.println("Original = " + original);

		int width = map.getWidth() ;
		int height = map.getHeight() ;

		System.out.println("Target = " + width + "x" + height);
		map.setSize(width, height);
		map.doLayout();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		map.print(g2d);
		g2d.dispose();

		map.setSize(original);
		map.doLayout();

		try {
		    ImageIO.write(img, "png", new File("C:/Users/Public/Documents/sample.png"));
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
	//zakaria
	public static void CacherZoomEtMiniMap(){
		map.setZoom(2);
		map.getMiniMap().setVisible(false);
		map.getZoomSlider().setVisible(false);
		map.getZoomInButton().setVisible(false);
		map.getZoomOutButton().setVisible(false);
	}
	public static void AfficherZoomEtMiniMap(){
		map.getMiniMap().setEnabled(true);
		map.getZoomSlider().setEnabled(true);
		map.getMiniMap().setVisible(true);
		map.getZoomSlider().setVisible(true);
		map.getZoomInButton().setVisible(true);
		map.getZoomOutButton().setVisible(true);
	}
}


