package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;

public class Main extends JFrame{

    private static JFrame frame;
    private JPanel panel;
    private static Connection connection = null;
    static final int frameWidth = 600;
    static final int frameHeight = 600;

    static final int frameLocX = 250;
    static final int frameLocY = 350;


    private void runApp() {
        frame = new JFrame();

        //Set Frame Properties
        frame = new JFrame();
        frame.setTitle("Hospital");
        frame.setLocation(frameLocX, frameLocY);
        frame.setLayout(new BorderLayout());


        connect();
        initializeHospitalLogin();
        frame.pack();
        frame.setVisible(true);
        closeConnection();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void initializeHospitalLogin() {
        JButton loginAsDoctor = new JButton("Doctor Login");
        JButton loginAsAdmin = new JButton("Admin Login");
        JButton cancelButton = new JButton("Cancel");
        panel = new JPanel();

        JTextField field1 = new JTextField();


        JTextField field2 = new JTextField();



//		TODO: add login as patient for later functionality
//		JButton loginAsPatient = new JButton();


        loginAsDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello Doctor!");
                field1.setText("Doctor Username");

                panel.add(field1);

                field2.setText("Doctor Password");
                panel.add(field2);

                panel.add(cancelButton);


                updateFrame();
                loginAsDoctor.setVisible(false);
                loginAsAdmin.setVisible(false);
                frame.repaint();

            }
        } );

        loginAsAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello Admin!");

                field1.setText("Admin Username");
                panel.add(field1);

                field2.setText("Admin Password");
                panel.add(field2);

                panel.add(cancelButton);


               updateFrame();
                loginAsDoctor.setVisible(false);
                loginAsAdmin.setVisible(false);
                frame.repaint();
            }
        } );

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancelled");

                field1.setText("Admin Username");
                panel.add(field1);

                field2.setText("Admin Password");
                panel.add(field2);

                panel.add(cancelButton);


                updateFrame();
                loginAsDoctor.setVisible(false);
                loginAsAdmin.setVisible(false);
                frame.repaint();
            }
        } );


        panel.add(loginAsDoctor);
        panel.add(loginAsAdmin);
//		panel.add(loginAsPatient);


        frame.add(panel);
        frame.add(panel);

        frame.repaint();

        return;

    }


    public static void main(String[] args) throws IOException {
        Main mainApp = new Main();
        mainApp.runApp();
    }

    private static void updateFrame() {
        frame.pack();
        frame.setLocation(frameLocX, frameLocY);
        frame.setSize(frameWidth, frameHeight);
        frame.setLayout(new BorderLayout());

    }


    public static void connect() {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";

        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", "hospitalAdmin")
                .replace("${pass}", "Password123");



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
            //do nothing
        }
    }

}