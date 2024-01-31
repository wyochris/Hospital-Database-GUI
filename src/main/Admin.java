package main;

import java.awt.BorderLayout;
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

public class Admin implements User {

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
		logoutButton = new JButton("Logout");

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
			
			initalizeTable(rs);


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

		logoutButton.addActionListener(e -> {
			System.out.println("Cancelled");

			field1.setVisible(false);
			field2.setVisible(false);

			frame.setTitle("Hospital");
            textPanel.removeAll();
//			if (this.success != null) {
//				success.setVisible(false);
//			}
			resultPanel.removeAll();
			if (resultPanel != null) {
				frame.remove(resultPanel);
			}

			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
			addPatientButton.setVisible(false);
			addProviderButton.setVisible(false);

			this.connection.closeConnection();

			frame.repaint();
		});

		// action listeners to buttons
		doctorView.addActionListener(e -> {

			try {

				Statement stmt = this.connection.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");

//                frame.add(textPanel, BorderLayout.SOUTH);
				initalizeTable(rs);
//		
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

			logoutButton.setVisible(true);
			addPatientButton.setVisible(false);
			textPanel.add(addProviderButton);
			addProviderButton.setVisible(true);

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
				initalizeTable(rs);
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

		addPatientButton.addActionListener(e -> {

			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addPatientButton.setVisible(false);

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
				initalizeTable(rs);
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

		});

		addProviderButton.addActionListener(e -> {

			resultPanel.setVisible(false);
			logoutButton.setVisible(false);
			doctorView.setVisible(false);
			patientView.setVisible(false);
//	            success.setVisible(false);
			addProviderButton.setVisible(false);

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
				initalizeTable(rs);
//	                frame.add(textPanel, BorderLayout.SOUTH);



			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

			logoutButton.setVisible(true);
			addPatientButton.setVisible(true);
			addProviderButton.setVisible(false);

		});

		// repaint the frame

		frame.repaint();
		this.frame.setVisible(true);

	}

	public void updateScreen(JFrame frame) {
//		initalize new buttons here
//		action listeners for the buttons
//		call the stored procedures from here
	}

	public void initalizeTable(ResultSet rs) {
		try {
		DefaultTableModel tableModel = new DefaultTableModel();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		// Add column headers
		for (int i = 1; i <= columnsNumber; i++) {
			tableModel.addColumn(rsmd.getColumnName(i));
		}

		// Add data rows
		while (rs.next()) {
			Object[] rowData = new Object[columnsNumber];
			for (int i = 1; i <= columnsNumber; i++) {
				rowData[i - 1] = rs.getString(i);
			}
			tableModel.addRow(rowData);
		}

		// Create JTable with the table model
		resultTable = new JTable(tableModel);

		// Add the table to the result panel
		resultPanel.removeAll();
		resultPanel.add(new JScrollPane(resultTable));

		// Add the result panel to the frame
		frame.add(resultPanel, BorderLayout.CENTER);
		frame.revalidate();
		} catch(SQLException ex){
			throw new RuntimeException(ex);
		}
		
	}
}
