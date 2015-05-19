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
public class RandomDeviceParametersWindows extends JInternalFrame {



	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int n=0;
	private int x=0;
	private int y=0;
	public static JTextField NBNoeuds;
    public static JTextField X ;
    public static JTextField Y ;
    public static JLabel Error;
    public boolean error=false;
    
    /**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RandomDeviceParametersWindows frame = new RandomDeviceParametersWindows();
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
	public RandomDeviceParametersWindows() {
		// TODO Auto-generated constructor stub
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		setTitle("Random");
		setBounds(100, 100, 550, 150);
//	    JPanel panel = new JPanel(new GridLayout(0, 1));
		JPanel panel = new JPanel();
	    getContentPane().add(panel,BorderLayout.SOUTH);
	    
	   
        JButton btnNewButton = new JButton("Générer");
		btnNewButton.setIcon(new ImageIcon(CupCarbonParameters.IMGPATH
				+ "loopnone-1.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					n=Integer.parseInt(NBNoeuds.getText());
					x=Integer.parseInt(X.getText());
					y=Integer.parseInt(Y.getText());
					if(n!=0&&x!=0&&y!=0) RandomDevices.addRandomSensors(n,x,y);
				}catch (NumberFormatException e2){
					e2.printStackTrace();
					error=true;
					Error.setVisible(true);
					
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
		panel.add(btnNewButton);
		Error=new JLabel("Vieullez Remplir Tous Les Champs");
		Error.setVisible(false);
		panel.add(Error);
		JPanel panel1=new JPanel(); 
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new BorderLayout(0, 0));
		JPanel panel2 = new JPanel();
		panel1.add(panel2, BorderLayout.WEST);
		panel2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel label_1 =new JLabel("Nombre De Capteurs:");
		label_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel2.add(label_1);
		
		JLabel label_2 =new JLabel("Nombre De Colonnes:");
		label_2.setFont(new Font("Arial", Font.PLAIN, 12));
		panel2.add(label_2);
		
		JLabel label_3 =new JLabel("Nombre De Lignes:");
		label_3.setFont(new Font("Arial", Font.PLAIN, 12));
		panel2.add(label_3);
		
		JPanel panel_3 = new JPanel();
		panel1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		NBNoeuds = new JTextField();
	    X = new JTextField();
	    Y = new JTextField();
	    panel_3.add(NBNoeuds);
	    panel_3.add(X);
	    panel_3.add(Y);
	    NBNoeuds.setColumns(2);
	    X.setColumns(2);
	    Y.setColumns(2);
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


