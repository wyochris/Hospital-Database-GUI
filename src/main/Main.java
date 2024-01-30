package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{

    private static JFrame frame;
    private JPanel panel;
    private JPanel textPanel;
    private JTable resultTable;
    private static Connection connection = null;
    static final int frameWidth = 800;
    static final int frameHeight = 800;

    static final int frameLocX = 100;
    static final int frameLocY = 100;

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

    private JPanel resultPanel;
    private JPanel bottomPanel;

    private JTextField field1;

    private JTextField field2;

    private JTextField field3;

    private JTextField field4;
    private JTextField field5;

    private JTextField field6;

    private static String field1text = "fail";
    private static String field2text = "fail";

    private static String field3text = "fail";
    private static String field4text = "fail";
    private static String field5text = "fail";
    private static String field6text = "fail";
    private JLabel success;


    private void runApp() {
        frame = new JFrame();
        panel = new JPanel();
        textPanel = new JPanel();
        resultPanel = new JPanel();
        bottomPanel = new JPanel();

        //Set Frame Properties
        frame.setTitle("Hospital");
        frame.setLocation(frameLocX, frameLocY);
        frame.setLayout(new BorderLayout());

        initializeHospitalLogin();
        frame.pack();
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        doctorView.addActionListener(e -> {

            try {

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");

//                frame.add(textPanel, BorderLayout.SOUTH);

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
                    CallableStatement cs = connection.prepareCall(storedProcedureCall);
                    cs.setString(2, field1text);
                    cs.setString(3, field2text);
                    cs.setString(4, field3text);
                    java.sql.Date date = java.sql.Date.valueOf(field4text);


                    cs.setDate(5, date);
//                    cs.setDate(5, date);

                    cs.registerOutParameter(1, java.sql.Types.INTEGER);
                    cs.executeUpdate();
                    int returnCode = cs.getInt(1);
                    if (returnCode == 0) {
                        JOptionPane.showMessageDialog(null, "Patient added!");
//                        rests.add(restName);
//                        return true;
                    } else {
//                        if (returnCode == 1) {
//                            JOptionPane.showMessageDialog(null, "Error: Duplicate restaurant name.");
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
//                        }
//                        return false;
                    }
                } catch (SQLException er) {
                    throw new RuntimeException(er);
                }

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");

//                frame.add(textPanel, BorderLayout.SOUTH);

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
            doctorView.setVisible(true);
            patientView.setVisible(true);
            success.setVisible(true);
            addPatientButton.setVisible(true);
            confirmAddPatientButton.setVisible(false);
            resultPanel.setVisible(true);
            field1.setVisible(false);
            field2.setVisible(false);
            field3.setVisible(false);
            field4.setVisible(false);
            cancelButton.setVisible(true);


        });

        addPatientButton.addActionListener(e->{

            resultPanel.setVisible(false);
            cancelButton.setVisible(false);
            doctorView.setVisible(false);
            patientView.setVisible(false);
            success.setVisible(false);
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
                    CallableStatement cs = connection.prepareCall(storedProcedureCall);
                    cs.setString(2, field1text);
                    cs.setString(3, field2text);
                    cs.setString(4, field3text);
                    java.sql.Date date = java.sql.Date.valueOf(field4text);


                    cs.setDate(5, date);
//                    cs.setDate(5, date);
                    cs.setString(6, field5text);
                    cs.setString(7, field6text);

                    cs.registerOutParameter(1, java.sql.Types.INTEGER);
                    cs.executeUpdate();
                    int returnCode = cs.getInt(1);
                    if (returnCode == 0) {
                        JOptionPane.showMessageDialog(null, "Provider added!");
//                        rests.add(restName);
//                        return true;
                    } else {
//                        if (returnCode == 1) {
//                            JOptionPane.showMessageDialog(null, "Error: Duplicate restaurant name.");
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Error: Unknown error occurred.");
//                        }
//                        return false;
                    }
                } catch (SQLException er) {
                    throw new RuntimeException(er);
                }

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.DoctorView");

//                frame.add(textPanel, BorderLayout.SOUTH);

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
            doctorView.setVisible(true);
            patientView.setVisible(true);
            success.setVisible(true);
            addProviderButton.setVisible(true);
            confirmAddProviderButton.setVisible(false);
            resultPanel.setVisible(true);
            field1.setVisible(false);
            field2.setVisible(false);
            field3.setVisible(false);
            field4.setVisible(false);
            field5.setVisible(false);
            field6.setVisible(false);
            cancelButton.setVisible(true);


        });

        addProviderButton.addActionListener(e->{

            resultPanel.setVisible(false);
            cancelButton.setVisible(false);
            doctorView.setVisible(false);
            patientView.setVisible(false);
            success.setVisible(false);
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

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.PatientsView");

//                frame.add(textPanel, BorderLayout.SOUTH);

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
            addPatientButton.setVisible(true);
            addProviderButton.setVisible(false);

        });



        textPanel.add(loginAsDoctor);
        textPanel.add(loginAsAdmin);

        //		panel.add(loginAsPatient);

//        frame.add(panel, BorderLayout.NORTH);
//        frame.add(bottomPanel, BorderLayout.SOUTH);
        System.out.println("Hello");
        frame.add(textPanel);

        frame.repaint();
    }

    private void setButtons(){
        field1.setVisible(true);
        field2.setVisible(true);
        loginAsDoctor.setVisible(false);
        loginAsAdmin.setVisible(false);
        logInButton.setVisible(true);
        cancelButton.setVisible(true);

    }


    public static void main(String[] args) throws IOException {
        Main mainApp = new Main();
        mainApp.runApp();
    }

    private static void updateFrame() {
        frame.pack();
//        frame.setLocation(frameLocX, frameLocY);
        frame.setSize(frameWidth, frameHeight);
        frame.setLayout(new BorderLayout());
        frame.repaint();

    }


    public static void connect() {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";

        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", field1text)
                .replace("${pass}", field2text);

        try {
            long start = System.currentTimeMillis();

            connection = DriverManager.getConnection(fullURL);

//            connection.setAutoCommit(false);
            System.out.println("Connected");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        //TODO: Task 1
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Closed Connection");
            }
        } catch (SQLException e) {

        }
    }

}