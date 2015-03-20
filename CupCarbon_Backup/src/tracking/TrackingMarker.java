package tracking;

import java.awt.Color;
import java.awt.Graphics;

import utilities.MapCalc;
import utilities.UColor;
import device.Marker;

public class TrackingMarker extends Marker{

	public TrackingMarker(double x, double y, double radius) {
		super(x, y, radius);
	}
	
	public TrackingMarker(String x, String y, String radius) {
		super(Double.valueOf(x), Double.valueOf(y), Double.valueOf(radius));
	}
	
	
	@Override
	public void draw(Graphics g) {
		if(visible) {
			initDraw(g) ;
			int[] coord = MapCalc.geoToIntPixelMapXY(x, y);
			int x = coord[0];
			int y = coord[1];
			//int x = MapCalc.geoToIntPixelMapX(this.x,this.y) ;
			//int y = MapCalc.geoToIntPixelMapY(this.x,this.y) ;		
			int rayon = MapCalc.radiusInPixels(this.radius) ;
					
			if (inside || selected) {
				g.setColor(UColor.NOIR_TRANSPARENT);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon+2, y-rayon-3);
				g.drawLine(x-rayon-3, y-rayon-3, x-rayon-3, y-rayon+2);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon+2, y+rayon+3);
				g.drawLine(x-rayon-3, y+rayon+3, x-rayon-3, y+rayon-2);			
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon-2, y-rayon-3);
				g.drawLine(x+rayon+3, y-rayon-3, x+rayon+3, y-rayon+2);			
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon-2, y+rayon+3);
				g.drawLine(x+rayon+3, y+rayon+3, x+rayon+3, y+rayon-2);
			}
			
			if (selected) {
				g.setColor(Color.gray);
				g.drawOval(x - rayon-4, y - rayon-4, (rayon+4) * 2, (rayon+4) * 2);
			}	
			
			drawMoveArrows(x,y,g) ;
			
			if(hide==0) {
				g.setColor(Color.BLUE);
				g.fillOval(x-3, y-3, 6, 6);
			}
		}
	}

}
