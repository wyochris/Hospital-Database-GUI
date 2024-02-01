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

import ScreenViewer.User;
import main.ScreenViewer.UserType;

public class ScreenViewer extends JFrame {

	private JPanel panel;
	private JPanel textPanel;
	private JPanel resultPanel;
	private JPanel bottomPanel;
	private JButton loginAsProvider;
	private JButton loginAsAdmin;
	private JButton cancelButton;
	
	private JButton loginAsPatient;
	private JButton confirmLogInButtonProvider;
	private JButton confirmLogInButtonPatient;
	private JButton confirmLogInButtonAdmin;
	private JButton registerAsProviderButton;
	private JButton confirmRegisterAsProvider;
	 
	private JButton confirmAddPatientButton;
	private JButton addPatientButton;
	private JButton addProviderButton;
	private JButton confirmAddProviderButton;
	


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

	private JFrame frame;

	private JLabel success;

	private JTable resultTable;

	static final int frameWidth = 800;
	static final int frameHeight = 800;

	static final int frameLocX = 100;
	static final int frameLocY = 100;
	protected ConnectionService connectionService;

	protected User user;
	
	 enum UserType {
			PATIENT, PROVIDER, ADMIN;
	 }

	public ScreenViewer(JFrame frame) {
		this.panel = new JPanel();
		this.textPanel = new JPanel();
		this.resultPanel = new JPanel();
		this.bottomPanel = new JPanel();
		this.frame = frame;

		this.frame.setTitle("Hospital");
		this.frame.setLocation(frameLocX, frameLocY);
		this.frame.setLayout(new BorderLayout());

		initializeHospitalLogin();

		this.frame.pack();
		this.frame.setVisible(true);

//        after initalizing login buttons, pack the frame
//        this.frame.setSize(frameWidth, frameHeight);

	}

	public void initializeHospitalLogin() {
		loginAsProvider = new JButton("Doctor Login");
		loginAsAdmin = new JButton("Admin Login");
		cancelButton = new JButton("Cancel");
		
        loginAsPatient = new JButton("Patient Login");
        confirmLogInButtonAdmin = new JButton("Log In");
        confirmLogInButtonProvider = new JButton("Log In");
        confirmLogInButtonPatient = new JButton("Login");
        registerAsProviderButton = new JButton("Provider Registration");
        confirmRegisterAsProvider = new JButton("Register");

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
		
        registerAsProviderButton.addActionListener(e -> {
        	registerActionListener(UserType.PROVIDER);
        });

		loginAsProvider.addActionListener(e -> {
			loginActionListener(UserType.PROVIDER);

		});

		loginAsAdmin.addActionListener(e -> {
			loginActionListener(UserType.ADMIN);
		});

		cancelButton.addActionListener(e -> {
			System.out.println("Cancelled");

            field1.setVisible(false);
            field2.setVisible(false);
            field3.setVisible(false);
            field4.setVisible(false);
            field5.setVisible(false);

			updateFrame();
			frame.setTitle("Hospital");
//            textPanel.removeAll();
			if (this.success != null) {
				success.setVisible(false);
			}
			resultPanel.removeAll();
			if (resultPanel != null) {
				frame.remove(resultPanel);
			}
			loginAsProvider.setVisible(true);
			loginAsAdmin.setVisible(true);
            loginAsPatient.setVisible(true);
            confirmLogInButtonAdmin.setVisible(false);
            confirmLogInButtonProvider.setVisible(false);
            confirmLogInButtonPatient.setVisible(false);
            confirmRegisterAsProvider.setVisible(false);
            registerAsProviderButton.setVisible(true);

            cancelButton.setVisible(false);
//            addPatientButton.setVisible(false);
//            addProviderButton.setVisible(false);
//      

			connectionService.closeConnection();

			frame.repaint();
		});

		confirmLogInButtonAdmin.addActionListener(e -> {
			field1text = field1.getText();
			System.out.println(field1text);

			field2text = field2.getText();
			System.out.println(field2text);

			field1.setVisible(false);
			field2.setVisible(false);
			loginAsProvider.setVisible(false);
			loginAsAdmin.setVisible(false);
			confirmLogInButtonAdmin.setVisible(false);
			confirmRegisterAsProvider.setVisible(false);
			confirmLogInButtonPatient.setVisible(false);
			loginAsPatient.setVisible(false);
            confirmRegisterAsProvider.setVisible(false);
			cancelButton.setVisible(false);
			frame.repaint();
			connectionService = new ConnectionService(field1text, field2text);

			connectionService.connect();
			this.user = new Admin(connectionService, frame);
			return;
          
		});
		
		// TODO: Refactor? 
		confirmLogInButtonProvider.addActionListener(e -> {
            field1text = field1.getText();
            System.out.println(field1text);

            field2text = field2.getText();
            System.out.println(field2text);

            field1.setVisible(false);
            field2.setVisible(false);
            loginAsProvider.setVisible(false);
            loginAsAdmin.setVisible(false);
            loginAsPatient.setVisible(false);
            confirmLogInButtonProvider.setVisible(false);
            cancelButton.setVisible(false);
            frame.repaint();
            
            Encryption en = new Encryption();
            connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
            connectionService.connect();

            UserLogin userLog = new UserLogin(connectionService);
            try {
                Boolean loginSuccess = userLog.login(field1text, field2text);

            }
            catch(Exception e1) {
            	System.out.println(e1);
            }
            
            cancelButton.setVisible(true);
//            textPanel.add(success);
            
        });
		
		// TODO: Refactor??
        confirmRegisterAsProvider.addActionListener(e -> {
            field1text = field1.getText();
            System.out.println(field1text);

            field2text = field2.getText();
            System.out.println(field2text);
            
            field3text = field3.getText();
            System.out.println(field3text);
            
            field4text = field4.getText();
            System.out.println(field4text);
            
            field5text = field5.getText();
            System.out.println(field5text);

            field1.setVisible(false);
            field2.setVisible(false);
            field3.setVisible(false);
            field4.setVisible(false);
            field5.setVisible(false);
            loginAsProvider.setVisible(false);
            loginAsAdmin.setVisible(false);
            loginAsPatient.setVisible(false);
            confirmRegisterAsProvider.setVisible(false);
            cancelButton.setVisible(false);
            frame.repaint();
            
            Encryption en = new Encryption();
            connectionService = new ConnectionService(en.getEncryptionUsername(), en.getEncryptionPassword());
            connectionService.connect();
            UserLogin userLog = new UserLogin(connectionService);
                
            if( userLog.con != null){
                try{
                	
//                	Boolean register = userLog.register(field1text, field2text, field3text, field4text,field5text, "true");
                	Boolean reg = userLog.register("Tim", "Walker", "1985-05-11", "timwalker", "Password123", "true");

                }
                catch(Exception e1) {
                	System.out.println(e1);
                }
            }
            
            
            cancelButton.setVisible(true);
        });

		textPanel.add(loginAsProvider);
		textPanel.add(loginAsAdmin);
		
        textPanel.add(loginAsPatient);
        textPanel.add(registerAsProviderButton);

		// panel.add(loginAsPatient);

//        frame.add(panel, BorderLayout.NORTH);
//        frame.add(bottomPanel, BorderLayout.SOUTH);
		System.out.println("Hello");
		frame.add(textPanel);
		frame.repaint();

	}

	private void updateFrame() {
		frame.pack();
//	        frame.setLocation(frameLocX, frameLocY);
		frame.setSize(frameWidth, frameHeight);
		frame.setLayout(new BorderLayout());
		frame.repaint();

	}

	private void setButtons() {
        field1.setVisible(true);
        field2.setVisible(true);
        loginAsProvider.setVisible(false);
        loginAsPatient.setVisible(false);
        loginAsAdmin.setVisible(false);
        cancelButton.setVisible(true);
        confirmRegisterAsProvider.setVisible(false);
        registerAsProviderButton.setVisible(false);

	}

	private void loginActionListener(UserType typeOfUser) {
		field1.setText(typeOfUser + " Username");

		textPanel.add(field1);

		field2.setText(typeOfUser + " Password");
		textPanel.add(field2);

        // NEW BY CHRIS
        
        if(typeOfUser == UserType.ADMIN) {
        	textPanel.add(confirmLogInButtonAdmin);
        	confirmLogInButtonAdmin.setVisible(true);
        }
        
        else if(typeOfUser == UserType.PATIENT) {
        	textPanel.add(confirmLogInButtonPatient);
        	confirmLogInButtonPatient.setVisible(true);

        }
        
        else {
        	textPanel.add(confirmLogInButtonProvider);
        	confirmLogInButtonProvider.setVisible(true);

        	
        }

		textPanel.add(cancelButton);

		updateFrame();
		frame.setTitle(typeOfUser + " Login");
		setButtons();
		frame.repaint();
	}
	
	private void registerActionListener(UserType userType) {
        field1.setText(userType+" First Name");
        textPanel.add(field1);

        field2.setText(userType +" Last Name");
        textPanel.add(field2);
        
        field3.setText(userType+" DOB");
        textPanel.add(field3);

        field4.setText(userType +" Username");
        textPanel.add(field4);
        
        field5.setText(userType+" Password");
        textPanel.add(field5);
        
        if(userType == UserType.PROVIDER) {
       	 textPanel.add(confirmRegisterAsProvider);
        }
        
//        else if(userType == User.PATIENT) {
//       	 textPanel.add(confirmLogInButtonPatient);
//        }
//        
//        else {
//       	 textPanel.add(confirmLogInButtonProvider);
//        }

        textPanel.add(cancelButton);

        updateFrame();
        frame.setTitle(userType+" Register");
        setRegisterButtons();
        frame.repaint();
	}
	
	private void setRegisterButtons() {
        field1.setVisible(true);
        field2.setVisible(true);
        field3.setVisible(true);
        field4.setVisible(true);
        field5.setVisible(true);

        loginAsProvider.setVisible(false);
        loginAsPatient.setVisible(false);
        loginAsAdmin.setVisible(false);
        confirmRegisterAsProvider.setVisible(true);
        registerAsProviderButton.setVisible(false);
        cancelButton.setVisible(true);
	}

}
