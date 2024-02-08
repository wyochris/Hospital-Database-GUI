package main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Provider extends User {
	// frame
	private JFrame frame;
	// connection
	private ConnectionService connection;

	// buttons
	private JButton confirmAddPatientButton;
	private JButton addPatientButton;
	private JButton logoutButton;
	private JButton confirmDeleteMedicationButton;
	private JButton deleteMedicationButton;
	private JButton addMedicationButton;
	private JButton confirmAddMedicationButton;
	private JButton updateMedicationButton;
	private JButton confirmUpdateMedicationButton;
	private JButton goBackButton; // work on making this work
	private JButton addSymptomButton;
	private JButton confirmAddSymptomButton;
	private JButton deleteSymptomButton;
	private JButton confirmDeleteSymptomButton;

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
	
	private int id;

	public Provider(ConnectionService connection, JFrame oldFrame) {
		System.out.println("made an provider");
		this.connection = connection;
//		this.id = ID;
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

	@Override
	public void initializeUserScreen() {
		System.out.println("init provider screen");
		// initlize buttons
		patientView = new JButton("Patient View");
		confirmAddPatientButton = new JButton("Confirm Add Patient");
		addPatientButton = new JButton("Add Patient");
		logoutButton = new JButton("Logout");
		confirmDeleteMedicationButton = new JButton("Confirm Delete Medication");
		deleteMedicationButton = new JButton("Delete Patient Medication");
		addMedicationButton = new JButton("Add Patient Medication");
		confirmAddMedicationButton = new JButton("Confirm Add Medicine");
		updateMedicationButton = new JButton("Update Patient Medication");
		confirmUpdateMedicationButton = new JButton("Confirm Update Medication");
		goBackButton = new JButton("Go Back");
		addSymptomButton = new JButton("Add Symptom");
		confirmAddSymptomButton = new JButton("Confirm Add Symptom");
		deleteSymptomButton = new JButton("Delete Symptom");
		confirmDeleteSymptomButton = new JButton("Confirm Delete Symptom");

		// init panels
		resultPanel = new JPanel();
		buttonPanel = new JPanel();
		procedurePanel = new JPanel();

		// intilize JTextFields
		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();
		field5 = new JTextField();
		field6 = new JTextField();

//		
		//add to panel
		buttonPanel.add(logoutButton);
		buttonPanel.add(addPatientButton);
		buttonPanel.add(addMedicationButton);
		buttonPanel.add(updateMedicationButton);
		buttonPanel.add(deleteMedicationButton);
		buttonPanel.add(addSymptomButton);
		buttonPanel.add(deleteSymptomButton);
		
		buttonPanel.setVisible(true);
		
		// initlaize tables
		try {
			Statement stmt = this.connection.getConnection().createStatement();
			// TODO: make it so that they can only see their own patients
			ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");
			initalizeTable(rs, resultTable, resultPanel, frame);
			frame.add(buttonPanel, BorderLayout.SOUTH);
			frame.repaint();
			
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
		goBackButton.addActionListener(e ->{
			new Provider(this.connection, this.frame);
		});
		
		logoutButton.addActionListener(e -> {
			try {
				// makes a new frame and reinitalizes the program
				this.frame.dispose();
				Main.main(null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		addPatientButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("First Name");
			field2.setText("Last Name");
			field3.setText("Middle Initial");
			field4.setText("DOB as yyyy-MM-dd");
			
			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(confirmAddPatientButton);

		});

		confirmAddPatientButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call AddPatient(?, ?, ?, ?)}";
				field1text = field1.getText();

				field2text = field2.getText();
				field3text = field3.getText();

				field4text = field4.getText();
				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setString(4, field3text);
					java.sql.Date date = java.sql.Date.valueOf(field4text);

					cs.setDate(5, date);
//	                    cs.setDate(5, date);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Patient added!");
//	                        rests.add(restName);
//	                        return true;
					} else {
//	                        if (returnCode == 1) {
//	                            JOptionPane.showMessageDialog(null, "Error: Duplicate restaurant name.");
//	                        } else {
//	                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
//	                        }
//	                        return false;
					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Could not add patient");

				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

//	                frame.add(textPanel, BorderLayout.SOUTH);
				initalizeTable(rs, resultTable, resultPanel, frame);
//				

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});

		deleteMedicationButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("MedicineName");
			field2.setText("PatientID");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(confirmDeleteMedicationButton);

		});

		confirmDeleteMedicationButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call deleteMedicine(?, ?)}";
				field1text = field1.getText();
				field2text = field2.getText();
				field3text = field3.getText();
				int field2int = Integer.parseInt(field2text);
				// if you need it to go with the provider id..someting we should add?
//                int field3int = Integer.parseInt(field3text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);
//                    cs.setInt(4, field3int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Medicine deleted!");
					} else {
						if (returnCode == 1) {
							JOptionPane.showMessageDialog(null,
									"Error: medicineName or patient id or provider id is null");
						} else if (returnCode == 2) {
							JOptionPane.showMessageDialog(null, "Error: Medicine does not exist.");
						} else if (returnCode == 3) {
							JOptionPane.showMessageDialog(null, "Error: Patient is not taking this medicine.");
						} else {
							JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
						}

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

//                frame.add(textPanel, BorderLayout.SOUTH);

				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Error: Could not delete medicine");
			}
		});

		// adding medication buttons
		addMedicationButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("MedicineName");
			field2.setText("Dose, include units");
			field3.setText("PatientID");
			field4.setText("ProviderID");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(confirmAddMedicationButton);

		});

		confirmAddMedicationButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call addMedicine(?, ?,?,?)}";
				field1text = field1.getText(); // medicine name
				field2text = field2.getText(); // dose
				field3text = field3.getText();// patientid
				field4text = field4.getText();// providerid
				int field3int = Integer.parseInt(field3text);
				int field4int = Integer.parseInt(field4text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setInt(4, field3int);
					cs.setInt(5, field4int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Medicine added!");

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Could Not Add Medicine");
			}

		});
		// updating medications buttons

		updateMedicationButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("MedicineName");
			field2.setText("Dose, include units");
			field3.setText("PatientID");
			
			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(confirmUpdateMedicationButton);

		});

		confirmUpdateMedicationButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call updateMedicine(?, ?,?)}";
				field1text = field1.getText(); // medicine name
				field2text = field2.getText(); // dose
				field3text = field3.getText();// patientid
				int field3int = Integer.parseInt(field3text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setInt(4, field3int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Medicine Updated!");

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Could Not Update Medicine");
			}

		});

		// adjusting symptoms

		// addsymptom
		addSymptomButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Symptom Name");
			field2.setText("Patient ID");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(confirmAddSymptomButton);

		});

		confirmAddSymptomButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call addSymptom(?, ?)}";
				field1text = field1.getText(); // symptom name
				field2text = field2.getText(); // patientId
				int field2int = Integer.parseInt(field2text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Symptom Added!");

					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
			}

		});

		// delete symptom
		deleteSymptomButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Symptom Name");
			field2.setText("Patient ID");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(confirmDeleteSymptomButton);

		});

		confirmDeleteSymptomButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call deleteSymptom(?, ?)}";
				field1text = field1.getText(); // symptom name
				field2text = field2.getText(); // patientId
				int field2int = Integer.parseInt(field2text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Symptom Deleted!");

					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
			}

		});
		

		// repaint the frame --causing it to break

//		frame.repaint();
//		this.frame.setVisible(true);

	}
		
		
	

	private void setUpFramesForActions() {
		buttonPanel.setVisible(false);
		procedurePanel.removeAll();
		procedurePanel.setVisible(true);
		frame.add(procedurePanel, BorderLayout.SOUTH);

	}

}
