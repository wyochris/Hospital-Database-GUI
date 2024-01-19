import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
 
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}};encrypt=false";
        
        String fullURL = url
                .replace("${dbServer}", "golem.csse.rose-hulman.edu")
                .replace("${dbName}", "Hospital")
                .replace("${user}", "hospitalAdmin")
                .replace("${pass}", "Password123");
 
        String fileName = "dataSource.csv";
        File file = new File(fileName);
        Scanner input; 
        
        Connection connection = null;
 
        try {
            long start = System.currentTimeMillis();
             
            connection = DriverManager.getConnection(fullURL);
            connection.setAutoCommit(false);
  
            String person = "INSERT INTO person (ID, FirstName, LastName, Minit, DOB) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement personStatement = connection.prepareStatement(person);    
             
            String has = "INSERT INTO has (PatientID, DiagnosisID) VALUES (?, ?)";
            PreparedStatement hasStatement = connection.prepareStatement(has);    
            
            String patient = "INSERT INTO patient (ID) VALUES (?)";
            PreparedStatement patientStatement = connection.prepareStatement(patient);    
            
            String provider = "INSERT INTO provider (ID, Speciality, Type, CanPrescribe) VALUES (?, ?, ?, ?)";
            PreparedStatement providerStatement = connection.prepareStatement(provider);    
            
            String diagnosis = "INSERT INTO diagnosis (ID, Name, Occurance, Frequency) VALUES (?, ?, ?, ?)";
            PreparedStatement diagnosisStatement = connection.prepareStatement(diagnosis);    
            
            String symptoms = "INSERT INTO symptoms (ID, Name) VALUES (?, ?)";
            PreparedStatement symptomsStatement = connection.prepareStatement(symptoms);    
            
            String exhibits = "INSERT INTO exhibits (PatientID, SymptomID) VALUES (?, ?)";
            PreparedStatement exhibitsStatement = connection.prepareStatement(exhibits);    
            
            String takesCareOf = "INSERT INTO takesCareOf (ProviderID, PatientID, HospitalID, DateOfVisit) VALUES (?, ?, ?, ?)";
            PreparedStatement takesCareOfStatement = connection.prepareStatement(takesCareOf);    
            
            String prescribes = "INSERT INTO prescribes (PatientID, MedicineID, ProviderID, Dose) VALUES (?, ?, ?, ?)";
            PreparedStatement prescribesStatement = connection.prepareStatement(prescribes);    
            
            String hospital = "INSERT INTO hospital (ID, Name, Address) VALUES (?, ?, ?)";
            PreparedStatement hospitalStatement = connection.prepareStatement(hospital);    
            
            String medicine = "INSERT INTO medicine (ID, name) VALUES (?, ?)";
            PreparedStatement medicineStatement = connection.prepareStatement(medicine);    
            
            int personCount = 0;
            int patientCount = 0;
            int providerCount = 0;
            int diagnosisCount = 0;
            int symptomCount = 0;
            int hospitalCount = 0;
            int medicineCount = 0;
            
//            ArrayList<String> patientName = new ArrayList<>();
            ArrayList<String> patientDOB = new ArrayList<>();
            ArrayList<String> providerName = new ArrayList<>();
            ArrayList<String> providerSpeciality = new ArrayList<>();
            ArrayList<String> providerDOB = new ArrayList<>();
            ArrayList<String> hospitalName = new ArrayList<>();
            ArrayList<String> hospitalAddress = new ArrayList<>();
            ArrayList<String> MedicineName = new ArrayList<>();
            ArrayList<String> MedicineDose = new ArrayList<>();
            ArrayList<String> DiagnosisName = new ArrayList<>();
            ArrayList<String> DiagnosisFreq = new ArrayList<>();
            ArrayList<String> DiagnosisOcc = new ArrayList<>();
            ArrayList<String> SymptomsName = new ArrayList<>();
            ArrayList<String> CanPrescribe = new ArrayList<>();
            
//            data.add(patientName);
//            data.add(patientDOB);
//            data.add(providerName);
//            data.add(providerSpeciality);
//            data.add(providerDOB);
//            data.add(hospitalName);
//            data.add(hospitalAddress);
//            data.add(MedicineName);
//            data.add(MedicineDose);
//            data.add(DiagnosisName);
//            data.add(DiagnosisFreq);
//            data.add(DiagnosisOcc);
//            data.add(SymptomsName);
//            data.add(CanPrescribe);
            
            BufferedReader input = new BufferedReader(new FileReader(file));
            
            int columnNum = 0;
            
            while(input.hasNext()) {
            	String line = input.next();
            	String[] dataPoint = line.split(".");
            	
            	columnNum = 0;
            	System.out.println(line);
            }
            
            
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
//                    case 0:
//                        String name = nextCell.getStringCellValue();
//                        statement.setString(1, name);
//                        break;
//                    case 1:
//                        Date enrollDate = nextCell.getDateCellValue();
//                        statement.setTimestamp(2, new Timestamp(enrollDate.getTime()));
//                    case 2:
//                        int progress = (int) nextCell.getNumericCellValue();
//                        statement.setInt(3, progress);
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
 
             
            // execute the remaining queries
//            statement.executeBatch();
  
            connection.commit();
            connection.close();
             
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
             
        } catch (IOException ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
        } catch (SQLException ex2) {
            System.out.println("Database error");
            ex2.printStackTrace();
        }
 
    }
}
