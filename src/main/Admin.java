package main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private JButton goBackButton;
	// views
	private JButton providerView;
	private JButton patientView;
	private JButton hospitalView;
	private JButton updateHospitalButton;
	private JButton confirmUpdateHospitalButton;
	private JButton updatePatientButton;
	private JButton confirmUpdatePatientButton;

	private JButton updateProviderButton;
	private JButton confirmUpdateProviderButton;
	// panels
	private JPanel procedurePanel;
	private JPanel buttonPanel;
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
	private JTextField field7;

	private String field1text;
	private String field2text;

	private String field3text;
	private String field4text;
	private String field5text;
	private String field6text;
	private String field7text;
	
	static final int frameWidth = 1600;
	static final int frameHeight = 800;

	public Admin(ConnectionService connection, JFrame oldFrame) {
		System.out.println("made an admin");
		this.connection = connection;
//		JFrame.setDefaultLookAndFeelDecorated(true);
		this.frame = new JFrame();
		oldFrame.dispose();
		this.frame.setVisible(true);
		updateFrame();
		initializeUserScreen();
	}

	private void updateFrame() {
//		frame.pack();
//	        frame.setLocation(frameLocX, frameLocY);
		frame.setSize(frameWidth, frameHeight);
//		frame.setLayout(new BorderLayout());
		frame.repaint();

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
		providerView = new JButton("Provider View");
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
		goBackButton = new JButton("Go Back");
		updatePatientButton = new JButton("Update Patient");
		confirmUpdatePatientButton = new JButton("Confirm Update Patient");
		updateProviderButton = new JButton("Update Provider");
		confirmUpdateProviderButton = new JButton("Confirm Update Provider");
		hospitalView = new JButton("Hospital View");
		updateHospitalButton = new JButton("Update Hospital");
		confirmUpdateHospitalButton = new JButton("Confirm Update Hospital");

//		init panels
		resultPanel = new JPanel();
		procedurePanel = new JPanel();
		buttonPanel = new JPanel();

		// intilize JTextFields
		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();
		field5 = new JTextField();
		field6 = new JTextField();
		field7 = new JTextField();

		buttonPanel.add(logoutButton);
		buttonPanel.add(providerView);
		buttonPanel.add(hospitalView);
		buttonPanel.add(addPatientButton);
		buttonPanel.add(deletePatientButton);
		buttonPanel.add(updatePatientButton);
//		

		// initlaize tables
		try {
//             success = new JLabel("Successfully Connected");
//			Statement stmt = this.connection.getConnection().createStatement();
//			//TODO: ask about select *
//			ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
			String procCall = "{? = call showAllPatients}";
			CallableStatement stmt = connection.getConnection().prepareCall(procCall);

			stmt.registerOutParameter(1, java.sql.Types.INTEGER);
			stmt.execute();
			ResultSet rs = stmt.getResultSet();
			resultTable = initalizeTableRETURN(rs);
			
			addEventListenerToTable(resultTable);
			putTableInPanel(resultTable, resultPanel, frame);
			
//			initalizeTable(rs, resultTable, resultPanel, frame);

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		frame.add(buttonPanel, BorderLayout.SOUTH);

		goBackButton.addActionListener(e ->{
			new Admin(this.connection, this.frame);
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

		hospitalView.addActionListener(e -> {
			setUpFramesForActions();
			procedurePanel.add(logoutButton);
			procedurePanel.add(patientView);
			procedurePanel.add(providerView);
			procedurePanel.add(updateHospitalButton);

			try {
				
				String procCall = "{? = call showAllHospitals}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				
//				initalizeTable(rs, resultTable, resultPanel, frame);

//				Statement stmt = this.connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.hospitalView");
				resultTable = initalizeTableRETURN(rs);
				
				addEventListenerToTable(resultTable);
				putTableInPanel(resultTable, resultPanel, frame);
//                frame.add(textPanel, BorderLayout.SOUTH);
//				initalizeTable(rs, resultTable, resultPanel, frame);
//
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});

		// action listeners to buttons
		providerView.addActionListener(e -> {
			
		
			setUpFramesForActions();
			procedurePanel.add(logoutButton);
			procedurePanel.add(patientView);
			procedurePanel.add(hospitalView);
			procedurePanel.add(addProviderButton);
			procedurePanel.add(deleteProviderButton);
			procedurePanel.add(updateProviderButton);

			try {
				
				String procCall = "{? = call showAllProviders}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
//				initalizeTable(rs, resultTable, resultPanel, frame);

//				Statement stmt = this.connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.ProvidersView");
				resultTable = initalizeTableRETURN(rs);
				
				addEventListenerToTable(resultTable);
				putTableInPanel(resultTable, resultPanel, frame);
//                frame.add(textPanel, BorderLayout.SOUTH);
//				initalizeTable(rs, resultTable, resultPanel, frame);
//		
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			

		});

		deleteProviderButton.addActionListener(e -> {
			setUpFramesForActions();
			
			field1.setText("Provider ID");

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
			procedurePanel.add(confirmDeleteProviderButton);
			

		});

		confirmDeleteProviderButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call deleteProvider(?)}";
				field1text = field1.getText(); //providerID
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
					JOptionPane.showMessageDialog(null, "Error Occurred.");

//                    throw new RuntimeException(er);
				}
				String procCall = "{? = call showAllProviders}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.ProvidersView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
	
		});
		
		addProviderButton.addActionListener(e -> {
			setUpFramesForActions();
//			

			field1.setText("First Name");
			field2.setText("Last Name");
			field3.setText("Middle Initial");
			field4.setText("DOB as yyyy-MM-dd");
			field5.setText("Specialty");
			field6.setText("canPrecribes: true or false");
			
			
			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(field5);
			procedurePanel.add(field6);
			procedurePanel.add(confirmAddProviderButton);

		});
		
		confirmAddProviderButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call AddProvider(?, ?, ?, ?, ?, ?)}";
				field1text = field1.getText(); //Fname
				field2text = field2.getText(); //lname
				field3text = field3.getText(); //minit
				field4text = field4.getText(); //DOB
				field5text = field5.getText(); //speciality
				field6text = field6.getText(); //can prescribe
				try {
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
					cs.setString(6, field5text);
					cs.setString(7, field6text);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Provider added!");
					} else {

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error Occurred.");
				}
				
				String procCall = "{? = call showAllProviders}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.ProvidersView");
//
//				initalizeTable(rs, resultTable, resultPanel, frame);				

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			
		});

		patientView.addActionListener(e -> {
			setUpFramesForActions();
			procedurePanel.add(logoutButton);
			procedurePanel.add(providerView);
			procedurePanel.add(hospitalView);
			procedurePanel.add(addPatientButton);
			procedurePanel.add(deletePatientButton);
			procedurePanel.add(updatePatientButton);

			try {
				String procCall = "{? = call showAllPatients}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
//	                frame.add(textPanel, BorderLayout.SOUTH);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});
		
		addPatientButton.addActionListener(e -> {
			setUpFramesForActions();
//			

			field1.setText("First Name");
			field2.setText("Last Name");
			field3.setText("Middle Initial");
			field4.setText("DOB as yyyy-MM-dd");
			field5.setText("Provider ID");
			field6.setText("Date of Visit");
			field7.setText("Hospital Name");


			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(field5);
			procedurePanel.add(field6);
			procedurePanel.add(field7);

			procedurePanel.add(confirmAddPatientButton);

		});

		

		confirmAddPatientButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call AddPatient(?, ?, ?, ?, ?, ?, ?)}";
				field1text = field1.getText(); // firstname

				field2text = field2.getText(); // last anme
				field3text = field3.getText(); // middle inital
				
				

				field4text = field4.getText(); // date of birth
				int field5int = Integer.parseInt(field5.getText());
				field6text = field6.getText(); //date of visit
				field7text = field7.getText();

				try {
					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);
					cs.setString(2, field1text);
					cs.setString(3, field2text);

					// first need to check if middle initial
					if (field3text.equals("")|| field3text.equals("Middle Initial")) {
						cs.setString(4, null);
					} else {
						cs.setString(4, field3text);
					}
					java.sql.Date date = java.sql.Date.valueOf(field4text);

					cs.setDate(5, date);
					cs.setInt(6, field5int);

					java.sql.Date dateOfVisit = java.sql.Date.valueOf(field6text);

					cs.setDate(7, dateOfVisit);
					cs.setString(8, field7text);

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Patient added!");

					} else {

					}
				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Unknown Error Occurred.");
				}
				
				
				String procCall = "{? = call showAllPatients}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);
				
				//old with the select *
				
//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
//				

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});

		deletePatientButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Patient ID");
			
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
			procedurePanel.add(confirmDeletePatientButton);

		});
		confirmDeletePatientButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call deletePatient(?)}";
				field1text = field1.getText(); //patientid
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
					JOptionPane.showMessageDialog(null, "Error Occurred.");
//                    throw new RuntimeException(er);
				}

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
				
				String procCall = "{? = call showAllPatients}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
	
		});


		updatePatientButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Patient ID");
			field2.setText("First Name");
			field3.setText("Last Name");
			field4.setText("Middle Initial");
			
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
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 1)));
			            field3.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 3)));
			            field4.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 2)));

			        }
			    }
			});

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(confirmUpdatePatientButton);

		});
		confirmUpdatePatientButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call updatePatientInfo(?, ?, ?, ?)}";
				field1text = field1.getText(); //patientid
				field2text = field2.getText(); //FName
				field3text = field3.getText(); //LName
				field4text = field4.getText(); //MInit
				int field1int;

				field1int = Integer.parseInt(field1text);
				try {

					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);

					cs.setInt(2, field1int);

					if(field2text.equals("First Name")){
						cs.setString(3, null);
					}else{

						cs.setString(3, field2text);
					}
					if(field3text.equals("Last Name")){
						cs.setString(4, null);
					}else{

						cs.setString(4, field3text);
					}
					if(field4text.equals("Middle Initial")){
						cs.setString(5, null);
					}else{
						cs.setString(5, field4text);
					}

					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					System.out.println("hello");
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Patient updated!");
					} else {
						JOptionPane.showMessageDialog(null, "Error: Patient does not exist.");
					}
//                        return false;

				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error Occurred.");
//                    throw new RuntimeException(er);
				}

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
				
				String procCall = "{? = call showAllPatients}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});

		updateProviderButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Provider ID");
			field2.setText("First Name");
			field3.setText("Last Name");
			field4.setText("Middle Initial");
			field5.setText("Specialty");
			
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
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 1)));
			            field3.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 3)));
			            field4.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 2)));
			            field5.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 7)));

			        }
			    }
			});
			

			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(field4);
			procedurePanel.add(field5);
			procedurePanel.add(confirmUpdateProviderButton);

		});
		confirmUpdateProviderButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call updateProviderInfo(?, ?, ?, ?, ?)}";
				field1text = field1.getText(); //patientid
				field2text = field2.getText(); //FName
				field3text = field3.getText(); //LName
				field4text = field4.getText(); //MInit
				field5text = field5.getText(); //Specialty
				int field1int;

				field1int = Integer.parseInt(field1text);
				try {

					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);

					cs.setInt(2, field1int);

					if(field2text.equals("First Name")){
						cs.setString(3, null);
					}else{

						cs.setString(3, field2text);
					}
					if(field3text.equals("Last Name")){
						cs.setString(4, null);
					}else{

						cs.setString(4, field3text);
					}
					if(field4text.equals("Middle Initial")){
						cs.setString(5, null);
					}else{
						cs.setString(5, field4text);
					}
					if(field5text.equals("Specialty")){
						cs.setString(6, null);
					}else{
						cs.setString(6, field5text);
					}
					System.out.println("Hello");
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					System.out.println("Hello");
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Provider updated!");
					} else {
						JOptionPane.showMessageDialog(null, "Error: Provider does not exist.");
					}
//                        return false;

				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error Occurred.");
//                    throw new RuntimeException(er);
				}

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.ProvidersView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
				
				String procCall = "{? = call showAllProviders}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});


		updateHospitalButton.addActionListener(e -> {
			setUpFramesForActions();

			field1.setText("Hospital ID");
			field2.setText("Name");
			field3.setText("Address");
			
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
			            field2.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 1)));
			            field3.setText((String) (resultTable.getValueAt(resultTable.getSelectedRow(), 2)));
			           
			        }
			    }
			});


			procedurePanel.add(goBackButton);
			procedurePanel.add(field1);
			procedurePanel.add(field2);
			procedurePanel.add(field3);
			procedurePanel.add(confirmUpdateHospitalButton);

		});
		confirmUpdateHospitalButton.addActionListener(e -> {

			try {
				String storedProcedureCall = "{? = call updateHospitalInfo(?, ?, ?)}";
				field1text = field1.getText(); //patientid
				field2text = field2.getText(); //Name
				field3text = field3.getText(); //Address

				int field1int;

				field1int = Integer.parseInt(field1text);
				try {

					CallableStatement cs = connection.getConnection().prepareCall(storedProcedureCall);

					cs.setInt(2, field1int);

					if(field2text.equals("Name")){
						cs.setString(3, null);
					}else{

						cs.setString(3, field2text);
					}
					if(field3text.equals("Address")){
						cs.setString(4, null);
					}else{

						cs.setString(4, field3text);
					}
					System.out.println("Hello");
					cs.registerOutParameter(1, java.sql.Types.INTEGER);
					cs.executeUpdate();
					System.out.println("Hello");
					int returnCode = cs.getInt(1);
					if (returnCode == 0) {
						JOptionPane.showMessageDialog(null, "Hospital updated!");
					} else {
						JOptionPane.showMessageDialog(null, "Error: Hospital does not exist.");
					}
//                        return false;

				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, "Error Occurred.");
//                    throw new RuntimeException(er);
				}

//				Statement stmt = connection.getConnection().createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.hospitalView");
//				initalizeTable(rs, resultTable, resultPanel, frame);
				
				String procCall = "{? = call showAllHospitals}";
				CallableStatement stmt = connection.getConnection().prepareCall(procCall);

				stmt.registerOutParameter(1, java.sql.Types.INTEGER);
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				initalizeTable(rs, resultTable, resultPanel, frame);

			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

		});
	
		// repaint the frame
//
//		frame.repaint();
//		this.frame.setVisible(true);

	}
	


	private void setUpFramesForActions() {
		buttonPanel.setVisible(false);
		procedurePanel.removeAll();
		procedurePanel.setVisible(true);
		frame.add(procedurePanel, BorderLayout.SOUTH);
		this.frame.setSize(frameWidth, frameHeight);
//		this.frame.pack();
//		this.frame.repaint();
		this.frame.setVisible(true);

	}

}
