from datetime import datetime
import pandas as pd
import pyodbc

server = 'golem.csse.rose-hulman.edu'
database = 'Hospital'
username = 'hospitalAdmin'
password = 'Password123'
cnxn = pyodbc.connect('DRIVER={ODBC Driver 17 for SQL Server};SERVER=' +
                      server+';DATABASE='+database+';UID='+username+';PWD='+ password)
cnxn.setdecoding(pyodbc.SQL_CHAR, encoding='latin1')
cnxn.setencoding('latin1')
cursor = cnxn.cursor()

file_path = 'dataSource.csv'
df = pd.read_csv(file_path)

def format_date(date_str):
    try:
        return datetime.strptime(date_str, '%m/%d/%Y').strftime('%Y-%m-%d')
    except ValueError:
        return None
    
def yes_no(value):
    if value.lower() == 'yes':
        return 'true'
    elif value.lower() == 'no':
        return 'false'
    else:
        return None

def do_data(df, cursor):
    for index, row in df.iterrows():

        can_prescribe = yes_no(row['Can Prescribe'])
        paitent_person_id = None
        provider_person_id = None
        diagnosis_id = None
        symptom_id = None
        hospital_id = None
        paitent_dob = None
        provider_dob = None

        # INSERT INTO PERSON
        paitent_dob = format_date(row['Patient DOB'])
        if paitent_dob:
            cursor.execute("""
                IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
                BEGIN
                    INSERT INTO person (FirstName, LastName, DOB)
                    VALUES (?, ?, ?)
                END
            """, row['Patient Name'].split()[0],
                 row['Patient Name'].split()[-1], paitent_dob,
                 row['Patient Name'].split()[0],
                 row['Patient Name'].split()[-1], paitent_dob)

            # GET THE PERSON ID
            cursor.execute("""
                            SELECT ID FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?
                            """,  row['Patient Name'].split()[0],
                                  row['Patient Name'].split()[-1], paitent_dob)
            
            paitent_person_id = cursor.fetchone().ID
            print("PAITENT ID IS: ", paitent_person_id)

            #INSERT INTO PAITENT
            if paitent_person_id is not None:
                cursor.execute("""
                               IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
                               BEGIN
                                    INSERT INTO patients (ID) VALUES (?)
                               END
                               """, row['Patient Name'].split()[0],
                                  row['Patient Name'].split()[-1], paitent_dob, paitent_person_id)
                
###################################################################################################################################

        #INSERT INTO PROVIDER
        provider_dob = format_date(row['Provider DOB'])
        if provider_dob:
            cursor.execute("""
                IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
                BEGIN
                    INSERT INTO person (FirstName, LastName, DOB)
                    VALUES (?, ?, ?)
                END
            """, row['Provider Name'].split()[0], row['Provider Name'].split()[-1], provider_dob,
            row['Provider Name'].split()[0], row['Provider Name'].split()[-1], provider_dob)
           
           #GET THE PERSON ID
            cursor.execute("""
                            SELECT ID FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?
                            """, 
                            row['Provider Name'].split()[0], row['Provider Name'].split()[-1], provider_dob)
            
            provider_person_id = cursor.fetchone().ID
           
           # INESRT INTO PROVIDOR
            cursor.execute("""
                IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
                BEGIN
                    INSERT INTO providers (ID, Speciality, CanPrescribe)
                    VALUES (?, ?, ?)
                END
            """,row['Provider Name'].split()[0], row['Provider Name'].split()[-1], provider_dob,
                 provider_person_id, row['Provider Speciality'],can_prescribe)
            
###################################################################################################################################

        # INSERT INTO DIAGNOSIS
        cursor.execute("""
           IF NOT EXISTS (SELECT 1 FROM diagnosis WHERE Name = ?)
            BEGIN
                INSERT INTO diagnosis (Name, Occurrence, Frequency)
                VALUES (?, ?, ?)
            END
        """, row['Diagnosis Name'], row['Diagnosis Name'], row['Diagnosis Occurance'], row['Diagnosis Frequency'])

        # GET THE DIAGNOSIS ID
        cursor.execute("""
                        SELECT ID FROM diagnosis WHERE Name = ?
                        """, 
                        row['Diagnosis Name'])
        
        diagnosis_id = cursor.fetchone().ID

###################################################################################################################################

        # INSERT INTO SYMPTOMS
        symptom_name = row['Symptoms']
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM symptoms WHERE Name = ?)
            BEGIN
                INSERT INTO symptoms (Name)
                VALUES (?)
            END
        """, symptom_name, symptom_name)

        # GET THE SYMPTOM ID
        cursor.execute("""
            SELECT ID FROM symptoms WHERE Name = ?
            """, symptom_name)
        symptom_id = cursor.fetchone().ID
        
        print("PAITENT ID IS: ", paitent_person_id)

###################################################################################################################################

        # INSERT INTO EXHIBITS
        if symptom_id is not None and paitent_person_id is not None:
            cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM exhibits WHERE PatientID = ? AND SymptomID = ?)
                BEGIN
                    INSERT INTO exhibits (PatientID, SymptomID)
                    VALUES (?, ?)
                END
            """, paitent_person_id, symptom_id, paitent_person_id, symptom_id)

###################################################################################################################################

        # INSERT INTO HOSPITAL
        cursor.execute("""
                       IF NOT EXISTS (SELECT 1 FROM hospital WHERE Name = ? AND Address = ?)
                       INSERT INTO hospital (Name, Address) VALUES (?, ?)
                       """
                       , row['Hospital Name'], row['Hospital Address'], row['Hospital Name'], row['Hospital Address'])
        
        # GET THE HOSPITAL ID
        cursor.execute("""
                        SELECT ID FROM hospital WHERE Name = ? AND Address = ?
                        """, 
                        row['Hospital Name'], row['Hospital Address'])
        
        hospital_id = cursor.fetchone().ID

###################################################################################################################################

        # INSERT INTO TAKESCAREOF
        if provider_person_id is not None and paitent_person_id is not None and hospital_id is not None:
            cursor.execute("""
                INSERT INTO takesCareOf (ProviderID, PatientID, HospitalID, DateOfVisit)
                VALUES (?, ?, ?, ?)
            """, provider_person_id, paitent_person_id, hospital_id, row['Diagnosis Occurance']) 

###################################################################################################################################

        # INSERT INTO MEDICINE
        cursor.execute("""
                        IF NOT EXISTS (SELECT 1 FROM medicine WHERE Name = ?)
                        INSERT INTO medicine (name) VALUES (?)
                        """
                        , row['Medicine Name'], row['Medicine Name'])
        
        cursor.execute(""" SELECT ID FROM medicine WHERE name = ?
                        """, row['Medicine Name'])
        medicine_id = cursor.fetchone().ID

###################################################################################################################################

        # INSERT INTO PRESCRIBES
        if provider_person_id is not None and paitent_person_id is not None and medicine_id is not None:
            cursor.execute("""
                INSERT INTO prescribes (PatientID, MedicineID, ProviderID, Dose)
                VALUES (?, ?, ?, ?)
            """, paitent_person_id, medicine_id, provider_person_id, row['Medicine Dose'])

###################################################################################################################################

        # INSERT INTO HAS
        if paitent_person_id is not None and diagnosis_id:
            cursor.execute("INSERT INTO has (PatientID, DiagnosisID) VALUES (?, ?)", paitent_person_id, diagnosis_id)

# Call the function
do_data(df, cursor)

# Commit and close
cnxn.commit()
cursor.close()
cnxn.close()