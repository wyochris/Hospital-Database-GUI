package main;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ScreenViewer extends JFrame{
	
	 private JPanel panel;
	 private JPanel textPanel;
	 private JPanel resultPanel;
	 private JPanel bottomPanel;
	 private JButton loginAsDoctor;
	 private JButton loginAsAdmin;
	 private JButton cancelButton;
	 private JButton logInButton;
	 
	 private JButton confirmAddPatientButton;
	 private JButton addPatientButton;
	 private JButton addProviderButton;
	 private JButton confirmAddProviderButton;
	
	 private JButton doctorView;
	 private JButton patientView;

	 private JTextField field1;
	 private JTextField field2;
	 private JTextField field3;
	 private JTextField field4;
	 private JTextField field5;
	 private JTextField field6;
	 
	 private JFrame frame;
	 

	public ScreenViewer(JFrame frame) {
		this.panel = new JPanel();
        this.textPanel = new JPanel();
        this.resultPanel = new JPanel();
        this.bottomPanel = new JPanel();
        this.frame = frame;
        
        initializeHospitalLogin();
		
	}
	
	
	private void initializeHospitalLogin() {
        loginAsDoctor = new JButton("Doctor Login");
        loginAsAdmin = new JButton("Admin Login");
        cancelButton = new JButton("Cancel");
        logInButton = new JButton("Log In");
        doctorView = new JButton("Doctor View");
        patientView = new JButton("Patient View");
        confirmAddPatientButton = new JButton("Confirm Add Patient");
        addPatientButton = new JButton("Add Patient");
        addProviderButton = new JButton("Add Provider");
        confirmAddProviderButton = new JButton("Confirm Add Provider");
        panel = new JPanel();
//        panel.setLayout(new BorderLayout());

        field1 = new JTextField();
        field2 = new JTextField();
        field3 = new JTextField();
        field4 = new JTextField();

        field5 = new JTextField();
        field6 = new JTextField();

//		TODO: add login as patient for later functionality
//		JButton loginAsPatient = new JButton();

        loginAsDoctor.addActionListener(e -> {
            System.out.println("Hello Doctor!");
            field1.setText("Doctor Username");

            textPanel.add(field1);

            field2.setText("Doctor Password");
            textPanel.add(field2);

            textPanel.add(logInButton);

            textPanel.add(cancelButton);

            updateFrame();
            frame.setTitle("Doctor Login");
            setButtons();
            frame.repaint();

        });

        loginAsAdmin.addActionListener(e -> {
            System.out.println("Hello Admin!");

            field1.setText("Admin Username");
            textPanel.add(field1);

            field2.setText("Admin Password");
            textPanel.add(field2);

            textPanel.add(logInButton);

            textPanel.add(cancelButton);

            updateFrame();
            frame.setTitle("Admin Login");
            setButtons();
            frame.repaint();
        });

        cancelButton.addActionListener(e -> {
            System.out.println("Cancelled");

            field1.setVisible(false);
            field2.setVisible(false);

            updateFrame();
            frame.setTitle("Hospital");
//            textPanel.removeAll();
            if(this.success != null){
                success.setVisible(false);
            }
            resultPanel.removeAll();
            if(resultPanel != null){
                frame.remove(resultPanel);
            }
            loginAsDoctor.setVisible(true);
            loginAsAdmin.setVisible(true);
            logInButton.setVisible(false);
            cancelButton.setVisible(false);
            doctorView.setVisible(false);
            patientView.setVisible(false);
            addPatientButton.setVisible(false);
            addProviderButton.setVisible(false);
            closeConnection();
            frame.repaint();
        });

        logInButton.addActionListener(e -> {
            field1text = field1.getText();
            System.out.println(field1text);

            field2text = field2.getText();
            System.out.println(field2text);

            field1.setVisible(false);
            field2.setVisible(false);
            loginAsDoctor.setVisible(false);
            loginAsAdmin.setVisible(false);
            logInButton.setVisible(false);
            cancelButton.setVisible(false);
            frame.repaint();

            connect();
            try {
                success = new JLabel("Successfully Connected");

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");

                frame.add(textPanel, BorderLayout.SOUTH);

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


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            cancelButton.setVisible(true);
            textPanel.add(doctorView);
            doctorView.setVisible(true);
            textPanel.add(patientView);
            patientView.setVisible(true);
            textPanel.add(addPatientButton);
            addPatientButton.setVisible(true);
            textPanel.add(success);

        });
	}
    
	
	
}
