package main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	
	private JButton searchMedsByPatientsButton;
	private JButton confirmSearchMedsByPatientsButton;

	private JButton goBackButton; // work on making this work
	private JButton addSymptomButton;
	private JButton confirmAddSymptomButton;
	private JButton deleteSymptomButton;
	private JButton confirmDeleteSymptomButton;
	private JButton addDiagnosisButton;
	private JButton confirmAddDiagnosisButton;
	private JButton deleteDiagnosisButton;
	private JButton confirmDeleteDiagnosisButton;

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

	private int proID;
	private int hosID;

	public Provider(ConnectionService connection, JFrame oldFrame, int proID, int hosID) {
		System.out.println("made an provider");
		this.connection = connection;
		this.proID = proID;
		this.hosID = hosID;
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
		addDiagnosisButton = new JButton("Add Diagnosis");
		confirmAddDiagnosisButton = new JButton("Confirm Add Diagnosis");
		deleteDiagnosisButton = new JButton("Delete Diagnosis");
		confirmDeleteDiagnosisButton = new JButton("Confirm Delete Diagnosis");
		searchMedsByPatientsButton = new JButton("Search Patient Meds");
		confirmSearchMedsByPatientsButton = new JButton("Confirm Patient ID");



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
		// add to panel
		buttonPanel.add(logoutButton);
		buttonPanel.add(addPatientButton);
		buttonPanel.add(addMedicationButton);
		buttonPanel.add(updateMedicationButton);
		buttonPanel.add(deleteMedicationButton);
		buttonPanel.add(searchMedsByPatientsButton);
		buttonPanel.add(addSymptomButton);
		buttonPanel.add(deleteSymptomButton);
		buttonPanel.add(addDiagnosisButton);
		buttonPanel.add(deleteDiagnosisButton);

		buttonPanel.setVisible(true);

		// initlaize tables
		try {
			Statement stmt = this.connection.getConnection().createStatement();
			// TODO: make it so that they can only see their own patients
//			ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientInformationView");

			String stmtCall = "{? = call getPatientsOfProvider(?)}";
			CallableStatement cs;
			try {
				cs = connection.getConnection().prepareCall(stmtCall);
				cs.registerOutParameter(1, java.sql.Types.INTEGER);
				cs.setInt(2, this.proID);
				cs.execute();
				ResultSet rs = cs.getResultSet();
				resultTable = initalizeTableRETURN(rs);
				
				addEventListenerToTable(resultTable);
				putTableInPanel(resultTable, resultPanel, frame);
		
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			frame.add(buttonPanel, BorderLayout.SOUTH);
			frame.repaint();

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		goBackButton.addActionListener(e -> {
			new Provider(this.connection, this.frame, this.proID, this.hosID);
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
			field5.setText("Date of Visit");
			field6.setText("Hospital Name");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(field5);
			procedurePanel.add(field6);
			procedurePanel.add(confirmAddPatientButton);

		});

		confirmAddPatientButton.addActionListener(e -> {

			try {
				CallableStatement stmt;
				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				try {
					stmt = connection.getConnection().prepareCall(stmtCall);
					stmt.registerOutParameter(1, java.sql.Types.INTEGER);
					stmt.setInt(2, this.proID);
					stmt.execute();
					ResultSet rs = stmt.getResultSet();
					this.frame.setTitle("Provider: " + this.proID);
					initalizeTable(rs, resultTable, resultPanel, frame);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				String storedProcedureCall = "{? = call AddPatient(?, ?, ?, ?, ?, ?, ?)}";
				field1text = field1.getText();

				field2text = field2.getText();
				field3text = field3.getText(); //

				field4text = field4.getText();
				field5text = field5.getText(); //date of visit
				field6text = field6.getText(); //Hospital Name

				
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					
					if (field3text.equals("")|| field3text.equals("Middle Initial")) {
						cs.setString(4, null);
					} else {
						cs.setString(4, field3text);
					}
					java.sql.Date date = java.sql.Date.valueOf(field4text);

					cs.setDate(5, date);
//	                    cs.setDate(5, date);
					cs.setInt(6, this.proID);

					java.sql.Date dateOfVisit = java.sql.Date.valueOf(field5text);
	
					cs.setDate(7, dateOfVisit);
					cs.setString(8, field6text);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Patient added!");
//	                        rests.add(restName);
//	                        return true;
					} else {
						System.out.println(returnCode);
//	                        if (returnCode == 1) {
//	                            JOptionPane.showMessageDialog(null, "Error: Duplicate restaurant name.");
//	                        } else {
//	                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
//	                        }
//	                        return false;
					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Could not add patient");

				}
				// using stored procedure to only view the providers patient informaiton
//				String stmtCall = "{? = call getPatientsOfProvider(?)}";
//				CallableStatement cs = connection.getConnection().prepareCall(stmtCall);
//				
//				cs.setInt(1, this.id);
//				cs.executeUpdate();

				CallableStatement cs;
				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					ResultSet rs = cs.getResultSet();
					this.frame.setTitle("Provider: " + this.proID);
					initalizeTable(rs, resultTable, resultPanel, frame);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

		});

		deleteMedicationButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("MedicineName");
			field2.setText("PatientID");
			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 8)));
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(confirmDeleteMedicationButton);

		});

		confirmDeleteMedicationButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call deleteMedicine(?, ?, ?)}";
				field1text = field1.getText();
				field2text = field2.getText();
				int field2int = Integer.parseInt(field2text);
				// if you need it to go with the provider id..someting we should add?
//                int field3int = Integer.parseInt(field3text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);
                    cs.setInt(4, this.proID);

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

				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					ResultSet rs = cs.getResultSet();
					this.frame.setTitle("Provider: " + this.proID);
					initalizeTable(rs, resultTable, resultPanel, frame);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}
////				}

		});

		// adding medication buttons
		addMedicationButton.addActionListener(e -> {
			
			
			setUpFramesForActions();
			field1.setText("Medicine Name");
			field2.setText("Dose, include units");
			field3.setText("PatientID");
			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field3.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});
			

//			field2.setText("Dose, include units");
//			field3.setText("PatientID");
			
//			field4.setText("ProviderID");

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
//			procedurePanel.add(field4);
			procedurePanel.add(confirmAddMedicationButton);

		});

		confirmAddMedicationButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call addMedicine(?, ?,?,?)}";
				field1text = field1.getText(); // medicine name
				field2text = field2.getText(); // dose
				field3text = field3.getText();// patientid
//				field4text = field4.getText();// providerid
				int field3int = Integer.parseInt(field3text);
//				int field4int = Integer.parseInt(field4text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setInt(4, field3int);
					cs.setInt(5, this.proID);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Medicine added!");

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				

				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
					this.frame.setTitle("Provider: " + this.proID);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

		});
		// updating medications buttons

		updateMedicationButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("MedicineName");
			field2.setText("Dose, include units");
			field3.setText("PatientID");

			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			    	System.out.println("WOOOO");
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 8)));
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 9)));
			            field3.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));


			        }
			    }
			});
			
			
			
			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(confirmUpdateMedicationButton);
			

		});
		
		

		confirmUpdateMedicationButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call updateMedicine(?, ?, ?, ?)}";
				field1text = field1.getText(); // medicine name
				field2text = field2.getText(); // dose
				field3text = field3.getText();// patientid
				int field3int = Integer.parseInt(field3text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);
					cs.setInt(4, field3int);
					cs.setInt(5, this.proID);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Medicine Updated!");

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
					this.frame.setTitle("Provider: " + this.proID);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

		});
		
		//TODO: WRITE SPROC TO MATCH
		
		searchMedsByPatientsButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Patient ID");
		

			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			    	System.out.println("WOOOO");
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			          
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));


			        }
			    }
			});
			
			
			
			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
		
			procedurePanel.add(confirmSearchMedsByPatientsButton);
			

		});
		
		

		confirmSearchMedsByPatientsButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call searchMedsByPatient(?, ?)}";
				field1text = field1.getText(); // patientid

			
				int field1int = Integer.parseInt(field1text);
				
				ResultSet rs = null;

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setInt(2, field1int);
					cs.setInt(3, this.proID);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					
					cs.executeUpdate();
					
					rs = cs.getResultSet();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Patient Meds Shown");

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}

				
				
				initalizeTable(rs, resultTable, resultPanel, frame);
				this.frame.setTitle("Displaying Meds for Patient: " + field1int);
//				
//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

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
			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});
			
			

		});

		confirmAddSymptomButton.addActionListener(e -> {

//			try {
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

				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					this.frame.setTitle("Provider: " + this.proID);
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				

//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

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
			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 7)));
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});

		});

		confirmDeleteSymptomButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call deleteSymptom(?, ?)}";
				field1text = field1.getText(); // symptom name
				field2text = field2.getText(); // patientId
				int field2int = Integer.parseInt(field2text);

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.execute();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Symptom Deleted!");

					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}
				
				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					this.frame.setTitle("Provider: " + this.proID);
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

		});
		
		
		addDiagnosisButton.addActionListener(e -> {
			setUpFramesForActions();
						
			field1.setText("Patient ID");
			field3.setText("Diagnosis Name");
			field4.setText("Occurrence as YYYY-MM-DD");
			field5.setText("Frequency");
			
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});


			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(field5);

			procedurePanel.add(confirmAddDiagnosisButton);

		});

		confirmAddDiagnosisButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call addDiagnosis(?, ?, ?, ?, ?)}";
				field1text = field1.getText(); // patientId
				field3text = field3.getText(); // diagnosis name
				field4text = field4.getText(); // occurence
				field5text = field5.getText(); // freq

				int field1int = Integer.parseInt(field1text);


				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setInt(2, field1int);
					cs.setInt(3, this.proID);
					cs.setString(4, field3text);
					cs.setString(5, field4text);
					cs.setString(6, field5text);


					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.execute();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Diagnosis Added!");

					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}
				
				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					this.frame.setTitle("Provider: " + this.proID);
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

		});
		
		deleteDiagnosisButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Diagnosis Name");
			field2.setText("Patient ID");
//			field3.setText("Provider ID");
	
			resultTable.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(final MouseEvent e) {
			        if (e.getClickCount() == 1) {
			            final JTable jTable= (JTable)e.getSource();
			            int row = jTable.getSelectedRow();
			            int column = jTable.getSelectedColumn();
			            String valueInCell = (String)jTable.getValueAt(row, column);
			            System.out.println(valueInCell);
			            field1.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 4)));
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 0)));

			        }
			    }
			});

			

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
	
			procedurePanel.add(confirmDeleteDiagnosisButton);

		});

		confirmDeleteDiagnosisButton.addActionListener(e -> {

//			try {
				String storedProcedureCall = "{? = call deleteDiagnosis(?, ?, ?)}";
				field1text = field1.getText(); // diagnosis name
				field2text = field2.getText(); // patientID
		

				int field2int = Integer.parseInt(field2text);
//				int field3int = Integer.parseInt(field3text);


				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setInt(3, field2int);
					cs.setInt(4, this.proID);



					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.execute();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Diagnosis Deleted!");

					}
				} catch (SQLException er) {
					System.out.println(er);
					JOptionPane.showMessageDialog(null, "Error: Incorrect Information");
				}
				
				String stmtCall = "{? = call getPatientsOfProvider(?)}";
				CallableStatement cs;
				try {
					cs = connection.getConnection().prepareCall(stmtCall);
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.setInt(2, this.proID);
					cs.execute();
					this.frame.setTitle("Provider: " + this.proID);
					ResultSet rs = cs.getResultSet();
					initalizeTable(rs, resultTable, resultPanel, frame);
//					return cs.getResultSet();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

//			} catch (SQLException ex) {
//				JOptionPane.showMessageDialog(null, "Could Not Add Symptom");
//			}

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
