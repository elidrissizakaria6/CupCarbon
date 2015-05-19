package util;

import java.util.Vector;

import javax.swing.JTable;

public class myJtable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public myJtable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public myJtable(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		// TODO Auto-generated constructor stub
	}


	public myJtable(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}


	public myJtable(Vector data, Vector columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}

	public boolean isCellEditable(int x, int y) {
		return false;
		}


}
