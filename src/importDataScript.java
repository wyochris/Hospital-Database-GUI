import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class importDataScript {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";


        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", "hospitalAdmin")
                .replace("${pass}", "Password123");

        String fileToRead = "";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(fullURL);
            System.out.println("Success");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            //do nothing
        }
    }

}

