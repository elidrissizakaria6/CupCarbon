package cupcarbon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class InsectParametersWindow extends JInternalFrame {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InsectParametersWindow frame = new InsectParametersWindow();
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
	public InsectParametersWindow() {
		setTitle("Insect Parameters");
		setClosable(true);
		setBounds(100, 100, 605, 556);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_4.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblDispertion = new JLabel("Dispertion");
		panel_1.add(lblDispertion);
		
		JLabel lblSpeedOnX = new JLabel("Speed on X");
		panel_1.add(lblSpeedOnX);
		
		JLabel lblSpeedOnY = new JLabel("Speed on Y");
		panel_1.add(lblSpeedOnY);
		
		JLabel lblRotationSpeed = new JLabel("Rotation speed");
		panel_1.add(lblRotationSpeed);
		
		JPanel panel_2 = new JPanel();
		panel_4.add(panel_2, BorderLayout.EAST);
		
		JSeparator separator = new JSeparator();
		panel_3.add(separator);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_5.add(panel_6);
		
		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7);
		
		JButton btnOk = new JButton("Ok");
		getContentPane().add(btnOk, BorderLayout.SOUTH);

	}

}
