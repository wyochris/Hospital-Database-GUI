import java.sql.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashSet;


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
            
            
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            ArrayList<String> patientName = new ArrayList<>();
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
            
            data.add(patientName); 			// 0
            data.add(patientDOB); 			// 1
            data.add(providerSpeciality);	// 2
            data.add(providerName);			// 3
            data.add(providerDOB);			// 4
            data.add(CanPrescribe);			// 5
            data.add(hospitalAddress);		// 6
            data.add(hospitalName);			// 7
            data.add(MedicineName);			// 8
            data.add(MedicineDose);			// 9
            data.add(DiagnosisName);		// 10
            data.add(DiagnosisFreq);		// 11
            data.add(DiagnosisOcc);			// 12
            data.add(SymptomsName);			// 13
            
            BufferedReader input = new BufferedReader(new FileReader(file));
            
            String line;
            input.readLine(); // skip the first line (headers)
  
//            String[] testPoint = input.readLine().split(","); 
//            for(int i=0; i<testPoint.length-1; i++) {
//            	System.out.println(testPoint[i]);
//            }
            
            while((line = input.readLine()) != null) {
            	for(int i=0; i<14;i++) {
                    String[] dataPoint = line.split(","); 
            		data.get(i).add(dataPoint[i]);
            	}
            	
//            	System.out.println(line);
            }
            
            HashSet<String> alreadyPerson = new HashSet<>();
            for(int i = 0; i < data.get(0).size() - 1; i++) {
            	String str = data.get(0).get(i);
            	String[] name = str.split(" ");
              	String fName = "";
            	String minit = null;
            	String lName = "";
            	if(name.length == 3) {
            		fName = name[0];
            		minit = name[1];
            		lName = name[2];
            	}
            	if(name.length == 2) {
            		fName = name[0];
            		lName = name[1];
            	}
            	String DOB = "";
            	if(alreadyPerson.add(str)) {
            		DOB = data.get(1).get(i);             	
                	personStatement.setString(1, fName);
                	personStatement.setString(2, minit);
                	personStatement.setString(3, lName);
                	personStatement.setString(4, DOB);
            	}
            }
            
            for(int i = 0; i < data.get(5).size() - 1; i++) {
            	String str = data.get(5).get(i);
            	String[] name = str.split(" ");
              	String fName = "";
            	String minit = null;
            	String lName = "";
            	if(name.length == 3) {
            		fName = name[0];
            		minit = name[1];
            		lName = name[2];
            	}
            	if(name.length == 2) {
            		fName = name[0];
            		lName = name[1];
            	}
            	String DOB = "";
            	if(alreadyPerson.add(str)) {
            		DOB = data.get(4).get(i);             	
                	personStatement.setString(1, fName);
                	personStatement.setString(2, minit);
                	personStatement.setString(3, lName);
                	personStatement.setString(4, DOB);
            	}
            }
            
            HashSet<String> alreadyHospital = new HashSet<>();
            for(int i = 0; i < data.get(6).size() - 1; i++) {
            	String addr = data.get(6).get(i);
            	String hName = data.get(7).get(i);
            	if(alreadyHospital.add(hName)) {
                	hospitalStatement.setString(1, hName);
                	hospitalStatement.setString(2, addr);
            	}
            }
            
            HashSet<String> alreadyMedicine = new HashSet<>();
            for(int i = 0; i < data.get(8).size() - 1; i++) {
            	String mName = data.get(8).get(i);
            	if(alreadyMedicine.add(mName)) {
                	medicineStatement.setString(1, mName);
            	}
            }
            
            HashSet<String> alreadyDiagnosis = new HashSet<>();
            for(int i = 0; i < data.get(9).size() - 1; i++) {
            	String dName = data.get(9).get(i);
            	String freq = data.get(10).get(i);
            	String occ = data.get(11).get(i);
            	if(alreadyDiagnosis.add(dName)) {
                	diagnosisStatement.setString(1, dName);
                	diagnosisStatement.setString(1, freq);
                	diagnosisStatement.setString(1, occ);
            	}
            }
            
            HashSet<String> alreadySymptoms = new HashSet<>();
            for(int i = 0; i < data.get(13).size() - 1; i++) {
            	String sName = data.get(13).get(i);
            	if(alreadySymptoms.add(sName)) {
                	symptomsStatement.setString(1, sName);
            	}
            }
            
            String getPerson = "SELECT ID, LastName FROM person";
            Statement pQuery = connection.createStatement();    
            ResultSet pSet = pQuery.executeQuery(getPerson);
            
            String getHospital = "SELECT ID, Name FROM Hospital";
            Statement hQuery = connection.createStatement();    
            ResultSet hSet = hQuery.executeQuery(getHospital);
            
            String getMedicine = "SELECT ID, Name FROM medicine";
            Statement mQuery = connection.createStatement();    
            ResultSet mSet = mQuery.executeQuery(getMedicine);
            
            String getDiagnosis = "SELECT ID, Name FROM diagnosis";
            Statement dQuery = connection.createStatement();    
            ResultSet dSet = mQuery.executeQuery(getDiagnosis);
            
            String getSymptoms = "SELECT ID, Name FROM symptoms";
            Statement sQuery = connection.createStatement();    
            ResultSet sSet = mQuery.executeQuery(getSymptoms);
            
            ArrayList<Integer> personIDs = new ArrayList<>();
			while (pSet.next()) {
				Integer personName = pSet.getInt("ID");
				personIDs.add(personName);
			}
			
            ArrayList<Integer> hIDs = new ArrayList<>();
			while (hSet.next()) {
				Integer hName = pSet.getInt("ID");
				hIDs.add(hName);
			}
			
            ArrayList<Integer> mIDs = new ArrayList<>();
			while (mSet.next()) {
				Integer mName = pSet.getInt("ID");
				mIDs.add(mName);
			}
			
            ArrayList<Integer> dIDs = new ArrayList<>();
			while (dSet.next()) {
				Integer dName = pSet.getInt("ID");
				dIDs.add(dName);
			}
			
            ArrayList<Integer> sIDs = new ArrayList<>();
			while (sSet.next()) {
				Integer sName = sSet.getInt("ID");
				sIDs.add(sName);
			}
			
            String paitent = "INSERT INTO paitent (ID) VALUES (?)";
            PreparedStatement paitentStatement = connection.prepareStatement(paitent);          
            for(int i = 0; i < data.get(0).size() - 1; i++) {
            	Integer ids = personIDs.get(i);
            	paitentStatement.setDouble(1, ids);
            }
            
            String provider = "INSERT INTO provider (ID, Speciality, CanPresribe) VALUES (?, ?, ?)";
            PreparedStatement paitentStatement = connection.prepareStatement(paitent);      
            for(int i = 0; i < data.get().size() - 1; i++) {
            	String mName = data.get(8).get(i);
            	if(alreadyMedicine.add(mName)) {
                	medicineStatement.setString(1, mName);
            	}
            }
            
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
