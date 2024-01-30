package main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.*;

public class ConnectionService {
    private Connection connection = null;
    private JTextField field1;
    private JTextField field2;

    public boolean connect(JTextField field1, JTextField field2) {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";

        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", this.field1.getText())
                .replace("${pass}", this.field2.getText());

        try {
            connection = DriverManager.getConnection(fullURL);

            System.out.println("Connected");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

	public Connection getConnection() {
		return this.connection;
	}
    
    public void closeConnection() {
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Closed Connection");
            }
        } catch (SQLException e) {
        	
        }
    }
        
}

