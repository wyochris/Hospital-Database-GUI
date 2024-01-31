package main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.*;

public class ConnectionService {
    private Connection connection = null;
    private String field1;
    private String field2;

    public ConnectionService(String field1, String field2) {
    	this.field1 = field1;
    	this.field2 = field2;
    }
    public boolean connect() {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";

        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", this.field1)
                .replace("${pass}", this.field2);

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

