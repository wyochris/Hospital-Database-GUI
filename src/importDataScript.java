import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

//import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class importDataScript {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";


        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", "hospitalAdmin")
                .replace("${pass}", "Password123");

        String fileToRead = "333 Data Source CURRENT.xlsx";

        int batchSize = 20;

        Connection connection = null;
//        try {
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            if(connection != null && !connection.isClosed()){
//                connection.close();
//            }
//        } catch (SQLException e) {
//            //do nothing
//        }

        Scanner sc = null;
        try {
            sc = new Scanner(new File("333 Data Source CURRENT.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        sc.useDelimiter(",");   //sets the delimiter pattern
        while (sc.hasNext())  //returns a boolean value
        {
            System.out.print(sc.next() + "| ");  //find and returns the next complete token from this scanner
        }
        sc.close();  //closes the scanner
//        try {
//            long start = System.currentTimeMillis();
//
//            FileInputStream inputStream = new FileInputStream(fileToRead);
//
//            Workbook workbook = new XSSFWorkbook(inputStream);
//
//            Sheet firstSheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = firstSheet.iterator();
//
////            connection = DriverManager.getConnection(jdbcURL, username, password);
//            connection = DriverManager.getConnection(fullURL);
//            System.out.println("Success");
//            connection.setAutoCommit(false);
//
//            String sql = "INSERT INTO students (name, enrolled, progress) VALUES (?, ?, ?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//
//            int count = 0;
//
//            rowIterator.next(); // skip the header row
//
//            while (rowIterator.hasNext()) {
//                Row nextRow = rowIterator.next();
//                Iterator<Cell> cellIterator = nextRow.cellIterator();
//
//                while (cellIterator.hasNext()) {
//                    Cell nextCell = cellIterator.next();
//
//                    int columnIndex = nextCell.getColumnIndex();
//
//                    switch (columnIndex) {
//                        case 0:
//                            String name = nextCell.getStringCellValue();
//                            statement.setString(1, name);
//                            break;
//                        case 1:
//                            Date enrollDate = nextCell.getDateCellValue();
//                            statement.setTimestamp(2, new Timestamp(enrollDate.getTime()));
//                        case 2:
//                            int progress = (int) nextCell.getNumericCellValue();
//                            statement.setInt(3, progress);
//                    }
//
//                }
//
//                statement.addBatch();
//
//                if (++count % batchSize == 0) {
//                    statement.executeBatch();
//                }
//
//            }
//
//            workbook.close();
//
//            // execute the remaining queries
//            statement.executeBatch();
//
//            connection.commit();
//            connection.close();
//
//            long end = System.currentTimeMillis();
//            System.out.printf("Import done in %d ms\n", (end - start));
//
//        } catch (IOException ex1) {
//            System.out.println("Error reading file");
//            ex1.printStackTrace();
//        } catch (SQLException ex2) {
//            System.out.println("Database error");
//            ex2.printStackTrace();
//        }
    }

}

