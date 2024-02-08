package main;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Patient extends User {
	// frame
	private JFrame frame;
	// connection
	private ConnectionService connection;

	// buttons
	private JButton logoutButton;
	private JButton goBackButton; // work on making this work

	// panels
	private JPanel resultPanel;
	private JPanel buttonPanel;

	// result table
	private JTable resultTable;

	static final int frameWidth = 1600;
	static final int frameHeight = 800;
	private int patID;

	public Patient(ConnectionService connection, JFrame oldFrame, int idNum) {
		System.out.println("made a patient");
		this.connection = connection;
		oldFrame.dispose();
		this.frame = new JFrame();
		this.frame.setVisible(true);
		updateFrame();
		this.patID = idNum;
		initializeUserScreen();

		System.out.println("made a id" + " " + idNum);
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
		// TODO Auto-generated method stub
		logoutButton = new JButton("Logout");
		buttonPanel = new JPanel();
		resultPanel = new JPanel();

		buttonPanel.add(logoutButton);
		buttonPanel.setVisible(true);
		frame.add(buttonPanel, BorderLayout.SOUTH);


		ResultSet rs = null;
		CallableStatement cstmt = null;
		if(this.patID != 0) {
			try {
				String storedProc = "{? = call getPatientInfo(?)}";
				cstmt = connection.getConnection().prepareCall(storedProc);
				System.out.println("now gonna connect to SQL server" + " " + this.patID);
				cstmt.setInt(2, this.patID);
		        
		        cstmt.registerOutParameter(1, Types.INTEGER);

		        cstmt.execute();
		        rs = cstmt.getResultSet();
		        if(rs != null) {
					initalizeTable(rs, resultTable, resultPanel, frame);
					frame.repaint();

		        }
		        else {
			        JOptionPane.showMessageDialog(null, "Login Failed.");
		        }
				
			}
			catch (SQLException e) {
		        JOptionPane.showMessageDialog(null, "Login Failed.");
		        e.printStackTrace();
			} finally {
		        try {
		            if (cstmt != null) cstmt.close();
		            if (cstmt != null) cstmt.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
			        JOptionPane.showMessageDialog(null, "Connection Failed.");
		        }
			}

		}
		
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
		

	}
	
}
