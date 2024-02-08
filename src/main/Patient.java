package main;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Patient extends User {
	// frame
	private JFrame frame;
	// connection
	private ConnectionService connection;

	// buttons
	private JButton logoutButton;
	private JButton confirmDeleteMedicationButton;
	private JButton deleteMedicationButton;
	private JButton goBackButton; // work on making this work

	// views
	private JButton patientView;

	// panels
	private JPanel buttonPanel;
	private JPanel resultPanel;
	private JPanel procedurePanel;

	// result table
	private JTable resultTable;

	// field texts
	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	private JTextField field4;
	private JTextField field5;
	private JTextField field6;

	private String field1text;
	private String field2text;

	private String field3text;
	private String field4text;
	private String field5text;
	private String field6text;

	static final int frameWidth = 1600;
	static final int frameHeight = 800;

	public Patient(ConnectionService connection, JFrame oldFrame, int patID) {
		System.out.println("made a patient");
		this.connection = connection;
		oldFrame.dispose();
		this.frame = new JFrame();
		this.frame.setVisible(true);
		updateFrame();
		initializeUserScreen();
	}

	private void updateFrame() {
//		frame.pack();
//	        frame.setLocation(frameLocX, frameLocY);
		frame.setSize(frameWidth, frameHeight);
		frame.setLayout(new BorderLayout());
		frame.repaint();

	}
}
