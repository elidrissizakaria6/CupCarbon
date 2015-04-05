package cupcarbon;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.MatteBorder;

import simbox_simulation.CpuSimulation;
import simbox_simulation.GpuSimulation;
import simbox_simulation.NetworkGenerator;
import simbox_simulation.SimulationInputs;
import wisen_simulation.WisenSimulation;
import device.Device;

public class WsnSimulationWindow extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private CpuSimulation cpuSimulation;
	private GpuSimulation gpuSimulation;
	//private WisenSimulation wisenSimulation;
	private WisenSimulation wisenSimulation;

	private JTextField iterNumberTextField;
	private JComboBox freqComboBox;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private static JProgressBar progressBar;
	private static JLabel stateLabel;
	private JRadioButton rdbtnCpuSimulation;
	private JRadioButton rdbtnGpuSimulation;
	private JRadioButton rdbtnPingPongSimulation;
	private JTextField energyMaxTextField;
	private JTextField stepTextField;
	private JTextField scriptSizeTextField;
	private JCheckBox cboxDEvent;
	private JCheckBox cboxMobility;
	private JTextField vdTextField;
	private JCheckBox cbVisual;
	private JTextField textField;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();

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
		setBounds(100, 100, 456, 558);

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
		iterNumberTextField.setText("10000");
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
		energyMaxTextField.setText("100000000");
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

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_3.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

		JLabel lblSerialFrequency = new JLabel("Data Rate");
		lblSerialFrequency.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblSerialFrequency);

		freqComboBox = new JComboBox();
		freqComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(freqComboBox);
		freqComboBox.setToolTipText("Data Rate");
		freqComboBox.setModel(new DefaultComboBoxModel(new String[] { "20000",
				"40000", "256000" }));
		freqComboBox.setSelectedIndex(2);

		JLabel lblBaudRate = new JLabel("bps");
		lblBaudRate.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_4.add(lblBaudRate);

		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.add(panel_18);
		panel_18.setLayout(new GridLayout(0, 2, 0, 0));

		JRadioButton rdbtnBits = new JRadioButton("Bits");
		rdbtnBits.setSelected(true);
		buttonGroup_1.add(rdbtnBits);
		rdbtnBits.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_18.add(rdbtnBits);

		JRadioButton rdbtnByte = new JRadioButton("Byte");
		buttonGroup_1.add(rdbtnByte);
		rdbtnByte.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_18.add(rdbtnByte);

		JPanel panel_17 = new JPanel();
		panel_3.add(panel_17);
		panel_17.setLayout(new BoxLayout(panel_17, BoxLayout.X_AXIS));

		JLabel lblCommunication = new JLabel("  Failure Probability   ");
		lblCommunication.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_17.add(lblCommunication);

		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.PLAIN, 12));
		textField.setText("0");
		panel_17.add(textField);
		textField.setColumns(10);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new MatteBorder(1, 1, 1, 1,
				(Color) new Color(0, 0, 0)));
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_7.add(panel_14);
		panel_14.setLayout(new GridLayout(3, 1, 0, 0));

		rdbtnCpuSimulation = new JRadioButton("CPU Based Simulation");
		panel_14.add(rdbtnCpuSimulation);
		rdbtnCpuSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonGroup.add(rdbtnCpuSimulation);
		rdbtnCpuSimulation.setSelected(true);

		rdbtnGpuSimulation = new JRadioButton("GPU Based Simulation");
		panel_14.add(rdbtnGpuSimulation);
		rdbtnGpuSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		buttonGroup.add(rdbtnGpuSimulation);
		
		rdbtnPingPongSimulation = new JRadioButton("PingPong Based Simulation");
		buttonGroup.add(rdbtnPingPongSimulation);
		rdbtnPingPongSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_14.add(rdbtnPingPongSimulation);

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_7.add(panel_13);
		panel_13.setLayout(new GridLayout(3, 1, 0, 0));
		
				cboxDEvent = new JCheckBox("Discrete Event");
				panel_13.add(cboxDEvent);
				cboxDEvent.setFont(new Font("Arial", Font.PLAIN, 12));
				cboxDEvent.setSelected(true);

		JPanel panel_16 = new JPanel();
		panel_13.add(panel_16);
		panel_16.setLayout(new BoxLayout(panel_16, BoxLayout.X_AXIS));

		JLabel lblSpeed = new JLabel(" Speed  ");
		lblSpeed.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblSpeed);

		vdTextField = new JTextField();
		panel_16.add(vdTextField);
		vdTextField.setToolTipText("Visual Delay");
		vdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		vdTextField.setText("10");
		vdTextField.setColumns(10);

		JLabel lblMs = new JLabel("  ms   ");
		lblMs.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_16.add(lblMs);
		
		JPanel panel_19 = new JPanel();
		panel_13.add(panel_19);
				panel_19.setLayout(new BoxLayout(panel_19, BoxLayout.X_AXIS));
		
				cbVisual = new JCheckBox("View mobiles");
				panel_19.add(cbVisual);
				cbVisual.setFont(new Font("Arial", Font.PLAIN, 12));
				
						cboxMobility = new JCheckBox("Mobility");
						panel_19.add(cboxMobility);
						cboxMobility.setFont(new Font("Arial", Font.PLAIN, 12));

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
				if (rdbtnPingPongSimulation.isSelected()) {
					simulateCallBack(1, 3);
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
				if (rdbtnPingPongSimulation.isSelected()) {
					simulateCallBack(3, 3);
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
				if (rdbtnPingPongSimulation.isSelected()) {
					simulateCallBack(2, 3);
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

		JSeparator separator_4 = new JSeparator();
		panel_1.add(separator_4);

		JPanel panel_15 = new JPanel();
		panel_1.add(panel_15);
		panel_15.setLayout(new GridLayout(0, 3, 0, 0));

		JButton btnNewButton_2 = new JButton("Stop Simulation");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnCpuSimulation.isSelected()) {
					stopSimulation(1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					stopSimulation(2);
				}
			}
		});
		btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_15.add(btnNewButton_2);

		JButton btnRestartSimulation = new JButton("Restart Simulation");
		btnRestartSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnCpuSimulation.isSelected()) {
					resumeSimulation(1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					resumeSimulation(2);
				}
			}
		});

		JButton btnSuspendSimulation = new JButton("Suspend Simulation");
		btnSuspendSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnCpuSimulation.isSelected()) {
					suspendSimulation(1);
				}
				if (rdbtnGpuSimulation.isSelected()) {
					suspendSimulation(2);
				}
			}
		});
		btnSuspendSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_15.add(btnSuspendSimulation);
		btnRestartSimulation.setFont(new Font("Arial", Font.PLAIN, 12));
		panel_15.add(btnRestartSimulation);

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
		// cpugpu = 3 : ping pong
		if (v == 1 || v == 3) {
			Device.dataRate = Integer.parseInt((String) freqComboBox.getSelectedItem());
			SimulationInputs.iterNumber = Integer.parseInt(iterNumberTextField.getText());
			SimulationInputs.energyMax = Integer.parseInt(energyMaxTextField.getText());
			SimulationInputs.scriptSize = Integer.parseInt(scriptSizeTextField.getText());
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
				cpuSimulation = new CpuSimulation();
				cpuSimulation.init();
				cpuSimulation.start();
			}
			if (cpugpu == 2) {
				gpuSimulation = new GpuSimulation();
				gpuSimulation.init();
				gpuSimulation.start();
			}			
			if (cpugpu == 3) {
				Device.dataRate = Integer.parseInt((String) freqComboBox.getSelectedItem());
				SimulationInputs.iterNumber = Integer.parseInt(iterNumberTextField.getText());
				SimulationInputs.energyMax = Integer.parseInt(energyMaxTextField.getText());
				SimulationInputs.discreteEvent = cboxDEvent.isSelected();
				SimulationInputs.mobility = cboxMobility.isSelected();
				SimulationInputs.step = Integer.parseInt(stepTextField.getText());
				SimulationInputs.visual = cbVisual.isSelected();
				SimulationInputs.visualDelay = Integer.parseInt(vdTextField.getText());
				wisenSimulation = new WisenSimulation();
				wisenSimulation.start();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void stopSimulation(int cpugpu) {
		if (cpugpu == 1) {
			cpuSimulation.stop();
		}
		if (cpugpu == 2) {
			gpuSimulation.stop();
		}
		if (cpugpu == 3) {
			wisenSimulation.stop();
		}
	}

	@SuppressWarnings("deprecation")
	public void suspendSimulation(int cpugpu) {
		if (cpugpu == 1) {
			cpuSimulation.suspend();
		}
		if (cpugpu == 2) {
			gpuSimulation.suspend();
		}
		if (cpugpu == 3) {
			wisenSimulation.suspend();
		}
	}

	@SuppressWarnings("deprecation")
	public void resumeSimulation(int cpugpu) {
		if (cpugpu == 1) {
			cpuSimulation.resume();
		}
		if (cpugpu == 2) {
			gpuSimulation.resume();
		}
		if (cpugpu == 3) {
			wisenSimulation.resume();
		}
	}

}
