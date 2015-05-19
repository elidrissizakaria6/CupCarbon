package window;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import map.Layer;
import util.myJtable;
import device.DeviceList;
import device.SensorNode;

public class showSensorsListWindow extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SensorNode>SensorNodes=DeviceList.getSensorNodes();
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	private boolean isSensorNodeSelected(SensorNode sn,ArrayList<Integer> l){
		for(int i=0;i<l.size();i++){
			if(l.get(i)==sn.getId()){
				return true;
			}
		}
		
		return false;
		
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					showSensorsListWindow frame = new showSensorsListWindow();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public showSensorsListWindow() {

		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Sensors");
		setBounds(100, 100, 310, 340);
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		 Vector<String> columnNames = new Vector<String>();
		    columnNames.addElement("Identifiant");
		    columnNames.addElement("Portée");
		    columnNames.addElement("Consommation");
		Vector<Vector<String>> rowData = new Vector<Vector<String>>();
		for(SensorNode sensorNode : SensorNodes){
			
			 Vector<String> rowOne = new Vector<String>();
			 rowOne.addElement(String.valueOf(sensorNode.getId()));
			 rowOne.addElement(String.valueOf(sensorNode.getRadioRadius()));
			 rowOne.addElement(String.valueOf(sensorNode.getConsommation()));
			 rowData.addElement(rowOne);
		}
	    final myJtable table = new myJtable(rowData, columnNames);
	    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);   
	      table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	    	    @Override
	    	    public void valueChanged(ListSelectionEvent event) {
	    	        if (table.getSelectedRow() > -1) {
	    	        	int SelectedRowTab[]=table.getSelectedRows();
	    	        	
	    	        	ArrayList<Integer> numdata = new ArrayList<Integer>();

	    	        	  for(int count = 0; count < SelectedRowTab.length; count++){
	    	        	      numdata.add(Integer.valueOf(table.getValueAt(SelectedRowTab[count], 0).toString()));
	    	        	  }
	    	        	
	    	        	for(int i=0;i<numdata.size();i++){
//	    	        		SensorNodes.get(SelectedRowTab[i]).setSelection(true);
	    	        		DeviceList.getNodeById(numdata.get(i)).setSelection(true);
	    	        		Layer.mapViewer.repaint();
	    	        	}
	    	        	for(int i=0;i<SensorNodes.size();i++){
	    	        		if(!isSensorNodeSelected(SensorNodes.get(i),numdata)){
		    	        			SensorNodes.get(i).setSelection(false);
		    	        			Layer.mapViewer.repaint();
	    	        		}
	    	        	}
	    	        	
	    	        }
	    	    }
	    	});
		table.getColumnModel().getColumn(0).setPreferredWidth(5);
	    JScrollPane scrollPane = new JScrollPane(table);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    scrollPane.getViewport().setViewPosition(new Point(0,0));
	    scrollPane.setPreferredSize(new Dimension(300, 300));
	   


	}

}
