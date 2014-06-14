package geometry;

import java.awt.Color;
import java.awt.Graphics;

import utilities.MapCalc;
import utilities.UColor;

public class Building {

	private double [] vertexListX;
	private double [] vertexListY;
	private int n = 0 ;
	
	public Building(int n) {
		this.n=n;
		vertexListX = new double[n];
		vertexListY = new double[n];
	}

	public void set(double x, double y, int i) {
		vertexListX[i] = x;
		vertexListY[i] = y;
	}
	
	public void set(String x, String y, int i) {
		vertexListX[i] = Double.valueOf(x);
		vertexListY[i] = Double.valueOf(y);
	}

	public void draw(Graphics g) {
		int[] coord = null ;
		int[] coordX = new int [n] ;
		int[] coordY = new int [n] ;
		for (int i = 0; i < n; i++) {
			coord = MapCalc.geoToIntPixelMapXY(vertexListX[i], vertexListY[i]);
			coordX[i]=coord[0];
			coordY[i]=coord[1];
		}
		g.setColor(UColor.BLEU_TRANSPARENT);
		g.fillPolygon(coordX, coordY, n);
		g.setColor(Color.gray);
		g.drawPolygon(coordX, coordY, n);
	}

}
