package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {

		private static Connection connection = null;
		
		public DatabaseConnection(String field1text, String field2text) {
	        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";

	        String fullURL = url
	                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
	                .replace("${dbName}", "Hospital")
	                .replace("${user}", field1text)
	                .replace("${pass}", field2text);

	        try {

	            connection = DriverManager.getConnection(fullURL);

//	            connection.setAutoCommit(false);
	            System.out.println("Connected");

	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
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

