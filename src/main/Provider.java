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

	// views
	private JButton patientView;

	// panels
	private JPanel textPanel;
	private JPanel resultPanel;

	// result table
	private JTable resultTable;

	// field texts
	private JTextField field1;
	private JTextField field2;
	private JTextField field3;
	private JTextField field4;
	private JTextField field5;
	private JTextField field6;

	private String field1text = "fail";
	private String field2text = "fail";

	private String field3text = "fail";
	private String field4text = "fail";
	private String field5text = "fail";
	private String field6text = "fail";

	public Provider(ConnectionService connection, JFrame frame) {
		System.out.println("made an provider");
		this.connection = connection;
		this.frame = frame;
		initializeUserScreen();
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
		
	
		//init panels
		resultPanel = new JPanel();
		textPanel = new JPanel();

		// intilize JTextFields
		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();
		field5 = new JTextField();
		field6 = new JTextField();

		textPanel.add(field1);
		textPanel.add(field2);
		field1.setVisible(false);
		field2.setVisible(false);

		// initlaize tables
		try {
			Statement stmt = this.connection.getConnection().createStatement();
			//TODO: make it so that they can only see their own patients
			ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");
			

			frame.add(textPanel, BorderLayout.SOUTH);

			initalizeTable(rs, resultTable, resultPanel, frame);

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		// set buttons visible
		System.out.println("setting buttons");
		logoutButton.setVisible(true);

		textPanel.add(patientView);
		patientView.setVisible(true);
		textPanel.add(addPatientButton);
		addPatientButton.setVisible(true);
		textPanel.add(logoutButton);
		
		deleteMedicationButton.setVisible(true);
		textPanel.add(deleteMedicationButton);
		

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
					throw new RuntimeException(er);
				}

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");

//	                frame.add(textPanel, BorderLayout.SOUTH);
				initalizeTable(rs, resultTable, resultPanel, frame);
//				

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			patientView.setVisible(true);
//	            success.setVisible(true);
			addPatientButton.setVisible(true);
			confirmAddPatientButton.setVisible(false);
			resultPanel.setVisible(true);
			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);
			logoutButton.setVisible(true);

		});

		addPatientButton.addActionListener(e -> {

			logoutButton.setVisible(false);
			patientView.setVisible(false);
			addPatientButton.setVisible(false);
			deleteMedicationButton.setVisible(false);
			confirmDeleteMedicationButton.setVisible(false);
			confirmAddPatientButton.setVisible(true);

			field1.setText("First Name");

			field1.setVisible(true);

			field2.setText("Last Name");
			field2.setVisible(true);

			field3.setText("Middle Initial");

			textPanel.add(field3);
			field3.setVisible(true);

			field4.setText("DOB as yyyy-MM-dd");
			textPanel.add(field4);
			field4.setVisible(true);

			textPanel.add(confirmAddPatientButton);

		});

		patientView.addActionListener(e -> {

			try {

				Statement stmt = connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
				initalizeTable(rs, resultTable, resultPanel, frame);
//	                frame.add(textPanel, BorderLayout.SOUTH);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

			logoutButton.setVisible(true);
			addPatientButton.setVisible(true);

		});
		
		deleteMedicationButton.addActionListener(e -> {

			logoutButton.setVisible(false);
//			patientView.setVisible(false);
			addPatientButton.setVisible(false);
			confirmAddPatientButton.setVisible(false);

			field1.setText("MedicineName");
			field1.setVisible(true);

			field2.setText("PatientID");
			field2.setVisible(true);
			
//			field3.setText("ProviderID");
//			field3.setVisible(true);
			
//			textPanel.add(field3);
			

			textPanel.add(confirmDeleteMedicationButton);
			confirmDeleteMedicationButton.setVisible(true);
			
			addPatientButton.setVisible(false);
            confirmAddPatientButton.setVisible(false);
            deleteMedicationButton.setVisible(false);
            patientView.setVisible(false);
            
         

		});
		
		confirmDeleteMedicationButton.addActionListener(e -> {

            try {
                String storedProcedureCall = "{? = call deleteMedicine(?, ?)}";
                field1text = field1.getText();
                field2text = field2.getText();
                field3text = field3.getText();
                int field2int = Integer.parseInt(field2text);
                //if you need it to go with the provider id..someting we should add?
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
                            JOptionPane.showMessageDialog(null, "Error: medicineName or patient id or provider id is null");
                        } else if(returnCode == 2){
                            JOptionPane.showMessageDialog(null, "Error: Medicine does not exist.");
                        } else if(returnCode == 3) {
                            JOptionPane.showMessageDialog(null, "Error: Patient is not taking this medicine.");
                        }
                        else {
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
                throw new RuntimeException(ex);
            }
//            doctorView.setVisible(true);
//            patientView.setVisible(true);
//            success.setVisible(true);
//            addPatientButton.setVisible(false);
//            confirmAddPatientButton.setVisible(false);
//            deleteMedicationButton.setVisible(false);
//            patientView.setVisible(false);
//            resultPanel.setVisible(true);
//            field1.setVisible(false);
//            field2.setVisible(false);
//            field3.setVisible(false);
//            field4.setVisible(false);
//            cancelButton.setVisible(true);


        });


		// repaint the frame

		frame.repaint();
		this.frame.setVisible(true);

	}

}
