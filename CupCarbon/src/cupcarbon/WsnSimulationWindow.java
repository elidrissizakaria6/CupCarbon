package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import wsn_simulation.CpuSimulation;
import wsn_simulation.GpuSimulation;
import wsn_simulation.NetworkGenerator;
import wsn_simulation.SimulationInputs;
import device.Device;

public class WsnSimulationWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField iterNumberTextField;
	private JComboBox freqComboBox;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private static JProgressBar progressBar;
	private static JLabel stateLabel;
	private JRadioButton rdbtnCpuSimulation;
	private JRadioButton rdbtnGpuSimulation;
	private JTextField energyMaxTextField;
	private JTextField stepTextField;
	private JTextField scriptSizeTextField;
	private JCheckBox cboxDEvent;
	private JCheckBox cboxMobility;
	private JTextField vdTextField;
	private JCheckBox cbVisual;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WsnSimulationWindow frame = new WsnSimulationWindow();
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
	public WsnSimulationWindow() {
		setTitle("Simulation Parameters");
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 377, 426);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(7, 7, 7, 7));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_12 = new JPanel();
		panel.add(panel_12);
		panel_12.setLayout(new GridLayout(4, 1, 5, 5));

		JPanel panel_8 = new JPanel();
		panel_12.add(panel_8);
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_8.setLayout(new BoxLayout(panel_8, BoxLayout.X_AXIS));

		JLabel lblNumberOfIterations = new JLabel("Number of Iterations");
		panel_8.add(lblNumberOfIterations);
		lblNumberOfIterations.setFont(new Font("Arial", Font.PLAIN, 12));

		iterNumberTextField = new JTextField();
		panel_8.add(iterNumberTextField);
		iterNumberTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		iterNumberTextField.setText("100");
		iterNumberTextField.setColumns(10);

		JPanel panel_9 = new JPanel();
		panel_12.add(panel_9);
		panel_9.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.X_AXIS));

		JLabel lblEnergy = new JLabel("Initial Energy ");
		panel_9.add(lblEnergy);
		lblEnergy.setFont(new Font("Arial", Font.PLAIN, 12));

		energyMaxTextField = new JTextField();
		energyMaxTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		energyMaxTextField.setText("100000");
		panel_9.add(energyMaxTextField);
		energyMaxTextField.setColumns(10);

		JPanel panel_10 = new JPanel();
		panel_12.add(panel_10);
		panel_10.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.X_AXIS));

		JLabel lblStep = new JLabel("Step");
		lblStep.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_10.add(lblStep);

		stepTextField = new JTextField();
		stepTextField.setText("1");
		stepTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		stepTextField.setColumns(10);
		panel_10.add(stepTextField);

		JPanel panel_11 = new JPanel();
		panel_12.add(panel_11);
		panel_11.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_11.setLayout(new BoxLayout(panel_11, BoxLayout.X_AXIS));

		JLabel lblScriptSize = new JLabel("Script Size");
		panel_11.add(lblScriptSize);
		lblScriptSize.setFont(new Font("Arial", Font.PLAIN, 12));

		scriptSizeTextField = new JTextField();
		scriptSizeTextField.setText("4");
		scriptSizeTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		scriptSizeTextField.setColumns(10);
		panel_11.add(scriptSizeTextField);

		JSeparator separator_3 = new JSeparator();
		panel.add(separator_3);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_3.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

		JLabel lblSerialFrequency = new JLabel("Serial Frequency");
		lblSerialFrequency.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblSerialFrequency);

		freqComboBox = new JComboBox();
		freqComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(freqComboBox);
		freqComboBox.setToolTipText("Baud Rate");
		freqComboBox.setModel(new DefaultComboBoxModel(new String[] { "110",
				"300", "1200", "2400", "4800", "9600", "19200", "38400",
				"57600", "115200", "230400", "460800", "921600", "1843200",
				"3686400" }));
		freqComboBox.setSelectedIndex(5);

		JLabel lblBaudRate = new JLabel("Baud rate");
		lblBaudRate.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblBaudRate);

		JSeparator separator_1 = new JSeparator();
		panel_1.add(separator_1);

		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_14 = new JPanel();
		panel_7.add(panel_14);
		panel_14.setLayout(new GridLayout(0, 1, 0, 0));

		rdbtnCpuSimulation = new JRadioButton("CPU Based Simulation");
		panel_14.add(rdbtnCpuSimulation);
		rdbtnCpuSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonGroup.add(rdbtnCpuSimulation);
		rdbtnCpuSimulation.setSelected(true);

		rdbtnGpuSimulation = new JRadioButton("GPU Based Simulation");
		panel_14.add(rdbtnGpuSimulation);
		rdbtnGpuSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonGroup.add(rdbtnGpuSimulation);

		JPanel panel_13 = new JPanel();
		panel_7.add(panel_13);
		panel_13.setLayout(new GridLayout(2, 2, 0, 0));

		cboxDEvent = new JCheckBox("Discrete Event");
		cboxDEvent.setFont(new Font("Arial", Font.PLAIN, 12));
		cboxDEvent.setSelected(true);
		panel_13.add(cboxDEvent);

		cboxMobility = new JCheckBox("Mobility");
		cboxMobility.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_13.add(cboxMobility);
		
		cbVisual = new JCheckBox("Visual: d=");
		cbVisual.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_13.add(cbVisual);
		
		vdTextField = new JTextField();
		vdTextField.setToolTipText("Visual Delay");
		vdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		vdTextField.setText("100");
		panel_13.add(vdTextField);
		vdTextField.setColumns(10);

		JSeparator separator = new JSeparator();
		panel_1.add(separator);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnNewButton_1 = new JButton("Check");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NetworkGenerator.check();
			}
		});
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_5.add(btnNewButton_1);

		JPanel panel_2 = new JPanel();
		panel_5.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 2, 0, 0));

		JButton btnGenerateNetwork = new JButton("Generate Network");
		btnGenerateNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnCpuSimulation.isSelected()) {
					simulateCallBack(1, 1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					simulateCallBack(1, 2);
				}
			}
		});
		panel_2.add(btnGenerateNetwork);
		btnGenerateNetwork.setFont(new Font("Arial", Font.PLAIN, 12));

		JButton button = new JButton("Run simulation ");
		panel_2.add(button);
		button.setFont(new Font("Arial", Font.PLAIN, 12));

		JButton btnNewButton = new JButton(
				"Generate Network and Run Simulation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnCpuSimulation.isSelected()) {
					simulateCallBack(3, 1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					simulateCallBack(3, 2);
				}
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_5.add(btnNewButton);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnCpuSimulation.isSelected()) {
					simulateCallBack(2, 1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					simulateCallBack(2, 2);
				}
			}
		});

		JSeparator separator_2 = new JSeparator();
		panel_1.add(separator_2);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_1.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));

		stateLabel = new JLabel("State");
		stateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		stateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_6.add(stateLabel, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
		progressBar.setStringPainted(true);
		panel_6.add(progressBar);

	}

	public static void setProgress(int v) {
		progressBar.setValue(v);
	}

	public static void setState(String s) {
		stateLabel.setText(s);
	}

	public void simulateCallBack(int v, int cpugpu) {
		// v = 1 : generate network
		// v = 2 : simulate
		// v = 3 : generate network & simulate
		// cpugpu = 1 : cpu
		// cpugpu = 2 : gpu
		if (v == 1 || v == 3) {
			Device.frequency = Integer.parseInt((String) freqComboBox
					.getSelectedItem());
			SimulationInputs.iterNumber = Integer.parseInt(iterNumberTextField
					.getText());
			SimulationInputs.energyMax = Integer.parseInt(energyMaxTextField
					.getText());
			SimulationInputs.scriptSize = Integer.parseInt(scriptSizeTextField
					.getText());
			SimulationInputs.discreteEvent = cboxDEvent.isSelected();
			SimulationInputs.mobility = cboxMobility.isSelected();
			SimulationInputs.step = Integer.parseInt(stepTextField.getText());
			SimulationInputs.visual = cbVisual.isSelected();
			SimulationInputs.visualDelay = Integer.parseInt(vdTextField.getText());
			if (cpugpu == 1)
				NetworkGenerator.generateForCpu();
			if (cpugpu == 2)
				NetworkGenerator.generateForGpu();
		}
		if (v == 2 || v == 3) {
			if (cpugpu == 1) {
				CpuSimulation simulation = new CpuSimulation();
				simulation.init();
				simulation.start();
			}
			if (cpugpu == 2) {
				GpuSimulation simulation = new GpuSimulation();
				simulation.init();
				simulation.start();
			}
		}
	}

}
