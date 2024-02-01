package main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Admin extends User {

	// frame
	private JFrame frame;
	// connection
	private ConnectionService connection;

	// buttons
	private JButton confirmAddPatientButton;
	private JButton addPatientButton;
	private JButton addProviderButton;
	private JButton confirmAddProviderButton;
	private JButton logoutButton;
	private JButton deletePatientButton;
	private JButton confirmDeletePatientButton;

	private JButton deleteProviderButton;
	private JButton confirmDeleteProviderButton;

	// views
	private JButton doctorView;
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
	

	public Admin(ConnectionService connection, JFrame frame) {
		System.out.println("made an admin");
		this.connection = connection;
		this.frame = frame;
		initializeUserScreen();
	}

	public void addPatient() {
		System.out.println("Add a patient");
	}

	public void addProvider() {
		System.out.println("add a provider");
	}

	@Override
	public void initializeUserScreen() {
		System.out.println("init admin screen");
		// initlize buttons
		doctorView = new JButton("Doctor View");
		patientView = new JButton("Patient View");
		confirmAddPatientButton = new JButton("Confirm Add Patient");
		addPatientButton = new JButton("Add Patient");
		addProviderButton = new JButton("Add Provider");
		confirmAddProviderButton = new JButton("Confirm Add Provider");
		deletePatientButton = new JButton("Delete Patient");
		confirmDeletePatientButton = new JButton("Confirm Delete Patient");
		logoutButton = new JButton("Logout");
		deleteProviderButton = new JButton("Delete Provider");
		confirmDeleteProviderButton = new JButton("Confirm Delete Provider");

//		init panels
		resultPanel = new JPanel();
		textPanel = new JPanel();
		
		//intilize JTextFields
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
//             success = new JLabel("Successfully Connected");
			Statement stmt = this.connection.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");

			frame.add(textPanel, BorderLayout.SOUTH);
			
			initalizeTable(rs, resultTable, resultPanel, frame);


		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		// set buttons visible
		System.out.println("setting buttons");
		logoutButton.setVisible(true);
		textPanel.add(doctorView);
		doctorView.setVisible(true);
		textPanel.add(patientView);
		patientView.setVisible(true);
		textPanel.add(addPatientButton);
		addPatientButton.setVisible(true);
		textPanel.add(logoutButton);
		textPanel.add(deletePatientButton);
		textPanel.add(deleteProviderButton);
		deleteProviderButton.setVisible(false);

		logoutButton.addActionListener(e -> {
			try {
				//makes a new frame and reinitalizes the program
				this.frame.dispose();
				Main.main(null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// action listeners to buttons
		doctorView.addActionListener(e -> {

			try {

				Statement stmt = this.connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");

//                frame.add(textPanel, BorderLayout.SOUTH);
				initalizeTable(rs, resultTable, resultPanel, frame);
//		
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

			logoutButton.setVisible(true);
			addPatientButton.setVisible(false);
			textPanel.add(addProviderButton);
			addProviderButton.setVisible(true);
			deletePatientButton.setVisible(false);
			deleteProviderButton.setVisible(true);
			confirmDeleteProviderButton.setVisible(false);

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

					} else {

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
			doctorView.setVisible(true);
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

		deletePatientButton.addActionListener(e -> {

//			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addPatientButton.setVisible(false);
			deletePatientButton.setVisible(false);

			field1.setText("Patient ID");

			field1.setVisible(true);



			textPanel.add(confirmDeletePatientButton);
			confirmDeletePatientButton.setVisible(true);

		});


		deleteProviderButton.addActionListener(e -> {

//			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addProviderButton.setVisible(false);
			deleteProviderButton.setVisible(false);

			field1.setText("Provider ID");

			field1.setVisible(true);



			textPanel.add(confirmDeleteProviderButton);
			confirmDeleteProviderButton.setVisible(true);

		});

		    confirmDeleteProviderButton.addActionListener(e -> {

            try {
                String storedProcedureCall = "{? = call deleteProvider(?)}";
                field1text = field1.getText();
				int field1int;
                field1int = Integer.parseInt(field1text);


                try {
                    CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
                    cs.setInt(2, field1int);


                    cs.registerOutParameter(1, java.sql.Types.INTEGER);
                    cs.executeUpdate();
                    int returnCode = cs.getInt(1);
                    if (returnCode == 0) {
                        JOptionPane.showMessageDialog(null, "Provider deleted!");
                    } else {
                        if (returnCode == 1) {
                            JOptionPane.showMessageDialog(null, "Error: Provider does not exist.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
                        }

                    }
                } catch (SQLException er) {
                    throw new RuntimeException(er);
                }

                Statement stmt = connection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");
				initalizeTable(rs, resultTable, resultPanel, frame);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
				doctorView.setVisible(true);
				patientView.setVisible(true);
//	            success.setVisible(true);
				addProviderButton.setVisible(true);
				confirmDeleteProviderButton.setVisible(false);
				resultPanel.setVisible(true);
				field1.setVisible(false);
				field2.setVisible(false);
				field3.setVisible(false);
				field4.setVisible(false);
				field5.setVisible(false);
				field6.setVisible(false);
				logoutButton.setVisible(true);
				deletePatientButton.setVisible(true);

        });

		confirmDeletePatientButton.addActionListener(e -> {

            try {
                String storedProcedureCall = "{? = call deletePatient(?)}";
                field1text = field1.getText();
				int field1int;


                field1int = Integer.parseInt(field1text);
                try {
                    CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
                    cs.setInt(2, field1int);


                    cs.registerOutParameter(1, java.sql.Types.INTEGER);
                    cs.executeUpdate();
                    int returnCode = cs.getInt(1);
                    if (returnCode == 0) {
                        JOptionPane.showMessageDialog(null, "Patient deleted!");
                    } else {
                            JOptionPane.showMessageDialog(null, "Error: Patient does not exist.");
                        }
//                        return false;

                } catch (SQLException er) {
                    throw new RuntimeException(er);
                }

                Statement stmt = connection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
				initalizeTable(rs, resultTable, resultPanel, frame);

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
			doctorView.setVisible(true);
			patientView.setVisible(true);
//	            success.setVisible(true);
			addProviderButton.setVisible(true);
			confirmAddProviderButton.setVisible(false);
			resultPanel.setVisible(true);
			field1.setVisible(false);
			logoutButton.setVisible(true);
			deleteProviderButton.setVisible(true);
			confirmDeleteProviderButton.setVisible(false);


        });

		addPatientButton.addActionListener(e -> {

//			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addPatientButton.setVisible(false);
			deletePatientButton.setVisible(false);

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
			confirmAddPatientButton.setVisible(true);

		});

		confirmAddProviderButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call AddProvider(?, ?, ?, ?, ?, ?)}";
				field1text = field1.getText();

				field2text = field2.getText();
				field3text = field3.getText();

				field4text = field4.getText();

				field5text = field5.getText();

				field6text = field6.getText();
				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setString(4, field3text);
					java.sql.Date date = java.sql.Date.valueOf(field4text);

					cs.setDate(5, date);
//	                    cs.setDate(5, date);
					cs.setString(6, field5text);
					cs.setString(7, field6text);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Provider added!");
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
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");

//	                frame.add(textPanel, BorderLayout.SOUTH);
				initalizeTable(rs, resultTable, resultPanel, frame);
//				

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			doctorView.setVisible(true);
			patientView.setVisible(true);
//	            success.setVisible(true);
			addProviderButton.setVisible(true);
			confirmAddProviderButton.setVisible(false);
			resultPanel.setVisible(true);
			field1.setVisible(false);
			field2.setVisible(false);
			field3.setVisible(false);
			field4.setVisible(false);
			field5.setVisible(false);
			field6.setVisible(false);
			logoutButton.setVisible(true);
			deleteProviderButton.setVisible(true);

		});

		addProviderButton.addActionListener(e -> {

//			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addProviderButton.setVisible(false);
			deleteProviderButton.setVisible(false);

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

			field5.setText("Specialty");

			textPanel.add(field5);
			field5.setVisible(true);

			field6.setText("canPrecribes: true or false");
			textPanel.add(field6);
			field6.setVisible(true);

			textPanel.add(confirmAddProviderButton);
			confirmAddProviderButton.setVisible(true);

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
			addProviderButton.setVisible(false);
			deleteProviderButton.setVisible(false);
			deletePatientButton.setVisible(true);
			confirmDeletePatientButton.setVisible(false);

		});

		// repaint the frame

		frame.repaint();
		this.frame.setVisible(true);

	}


	
}
