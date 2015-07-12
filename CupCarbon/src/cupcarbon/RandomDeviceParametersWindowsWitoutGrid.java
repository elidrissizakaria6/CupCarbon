package cupcarbon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import map.RandomDevices;
public class RandomDeviceParametersWindowsWitoutGrid extends JInternalFrame {



	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int n=0;
	private static JTextField NBNoeuds;
	private static JLabel ErrorVoid;
    private boolean error=false;
    
    /**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RandomDeviceParametersWindowsWitoutGrid frame = new RandomDeviceParametersWindowsWitoutGrid();
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
	public RandomDeviceParametersWindowsWitoutGrid() {
		// TODO Auto-generated constructor stub
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Random");
		setBounds(100, 100, 550, 120);
//	    JPanel panel = new JPanel(new GridLayout(0, 1));
		final JPanel panel = new JPanel();
		final JPanel panel10=new JPanel();
		final JPanel panel11=new JPanel();
	    getContentPane().add(panel,BorderLayout.SOUTH);
	   
        JButton btnNewButton = new JButton("Générer");
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH
				+ "loopnone-1.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					n=Integer.parseInt(NBNoeuds.getText());
					
						RandomDevices.addRandomSensorsWithoutMarkers(n);
						
				}catch (NumberFormatException e2){
//					e2.printStackTrace();
					error=true;
					ErrorVoid.setVisible(true);
				}
				try {
					if(error==false) setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					if(error==true){
						
					}
				}
			}
		});
		panel10.add(btnNewButton);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(panel11, BorderLayout.SOUTH);
		panel.add(panel10, BorderLayout.CENTER);
		ErrorVoid=new JLabel("Vieullez Remplir Tous Les Champs");
		ErrorVoid.setVisible(false);
		panel11.add(ErrorVoid);
		JPanel panel1=new JPanel(); 
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new BorderLayout(0, 0));
		JPanel panel2 = new JPanel();
		panel1.add(panel2, BorderLayout.WEST);
		panel2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel label_1 =new JLabel("Nombre De Capteurs:");
		label_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel2.add(label_1);
		
		JPanel panel_3 = new JPanel();
		panel1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		NBNoeuds = new JTextField();
	    panel_3.add(NBNoeuds);
	    NBNoeuds.setColumns(2);
//        int result = JOptionPane.showConfirmDialog(null, panel, "Random",
//                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//        if (result == JOptionPane.OK_OPTION) {
//                n= Integer.parseInt(NBNoeuds.getText());
//                x=Integer.parseInt(X.getText());
//                y=Integer.parseInt(Y.getText());
//                if(x*y<n){
//        			    JOptionPane.showMessageDialog(panel,"Vous pouvez pas générer "+n+" capteurs avec cette grille"
//        			    		+ "Veuillez modifier les dimentions","Erreur",
//        			        JOptionPane.WARNING_MESSAGE);
//                }
//        		
//        } else {
//            System.out.println("Cancelled");
//        }

	}
}


