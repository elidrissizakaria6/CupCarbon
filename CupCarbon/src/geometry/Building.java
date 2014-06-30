package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import utilities.MapCalc;
import utilities.UColor;

public class Building implements MouseMotionListener {

	private Polygon shape ;
	private int[] coordX ;
	private int[] coordY ;
	private double [] vertexListX;
	private double [] vertexListY;
	private int n = 0 ;
	
	public Building(int n) {
		this.n=n;
		vertexListX = new double [n];
		vertexListY = new double [n];
		coordX = new int [n] ;
		coordY = new int [n] ;
	}

	public void set(double x, double y, int i) {
		int[] coord = null ;		
		coord = MapCalc.geoToIntPixelMapXY(x, y);
		coordX[i]=coord[0];
		coordY[i]=coord[1];
		//vertexListX[i] = x;
		//vertexListY[i] = y;

	}
	
	public void set(String x, String y, int i) {
		int[] coord = null ;		
		coord = MapCalc.geoToIntPixelMapXY(Double.valueOf(x), Double.valueOf(y));
		coordX[i]=coord[0];
		coordY[i]=coord[1];
		
		//vertexListX[i] = Double.valueOf(x);
		//vertexListY[i] = Double.valueOf(y);
	}
	
	public void generateShape() {
		shape = new Polygon(coordX, coordY, n);
	}

	public void draw2(Graphics g) {
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
	
	public void draw(Graphics g) {
		g.setColor(UColor.BLEU_TRANSPARENT);
		g.fillPolygon(shape);
		g.setColor(Color.gray);
		g.drawPolygon(shape);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		System.out.println(arg0.getX());
		System.out.println(arg0.getY());
		System.out.println(shape);
	}

}
