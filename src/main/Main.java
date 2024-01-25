package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{

    private static JFrame frame;
    private JPanel panel;
    private JPanel textPanel;
    private JTable resultTable;
    private static Connection connection = null;
    static final int frameWidth = 600;
    static final int frameHeight = 600;

    static final int frameLocX = 250;
    static final int frameLocY = 350;

    private JButton loginAsDoctor;
    private JButton loginAsAdmin;
    private JButton cancelButton;
    private JButton logInButton;
    private JPanel resultPanel;
    private JPanel bottomPanel;

    private JTextField field1;

    private JTextField field2;

    private static String field1text = "fail";
    private static String field2text = "fail";
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
        panel = new JPanel();
//        panel.setLayout(new BorderLayout());

        field1 = new JTextField();
        field2 = new JTextField();

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
                textPanel.add(success);
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

        });


        textPanel.add(loginAsDoctor);
        textPanel.add(loginAsAdmin);

        //		panel.add(loginAsPatient);

//        frame.add(panel, BorderLayout.NORTH);
//        frame.add(bottomPanel, BorderLayout.SOUTH);
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

            connection.setAutoCommit(false);
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