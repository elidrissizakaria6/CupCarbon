package map;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import cupcarbon.CupCarbonParameters;

public class Fenetre extends JInternalFrame {

	Grille grille=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre frame = new Fenetre();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Fenetre()
	{
		super("Grille");
		grille=new Grille();
		grille.laGrille();
		setContentPane(grille);
		setBounds(0, 0, 800, 500);
		setMaximizable(true);
		setFrameIcon(new ImageIcon(CupCarbonParameters.IMGPATH + "logo_cap_carbon.png"));
		setResizable(true);
		setIconifiable(true);
	}
}
