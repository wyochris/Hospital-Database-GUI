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

def get_scope_identity(cursor):
    cursor.execute("SELECT @@IDENTITY AS ID;")
    # cursor.execute("SELECT SCOPE_IDENTITY() AS ID;")
    result = cursor.fetchone()
    return result.ID if result else None

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

            paitent_person_id = get_scope_identity(cursor)
            if paitent_person_id is not None:
                cursor.execute("INSERT INTO patients (ID) VALUES (?)", paitent_person_id)

        provider_dob = format_date(row['Provider DOB'])
        if provider_dob:
            cursor.execute("""
                INSERT INTO person (FirstName, LastName, DOB)
                VALUES (?, ?, ?)
            """, row['Provider Name'].split()[0], row['Provider Name'].split()[-1], provider_dob)
           
            provider_person_id = get_scope_identity(cursor)
           
            cursor.execute("""
                INSERT INTO providers (ID, Speciality, CanPrescribe)
                VALUES (?, ?, ?)
            """, provider_person_id, row['Provider Speciality'],can_prescribe)

        cursor.execute("""
           IF NOT EXISTS (SELECT 1 FROM diagnosis WHERE Name = ?)
            BEGIN
                INSERT INTO diagnosis (Name, Occurrence, Frequency)
                VALUES (?, ?, ?)
            END
        """, row['Diagnosis Name'], row['Diagnosis Name'], row['Diagnosis Occurance'], row['Diagnosis Frequency'])
        diagnosis_id = get_scope_identity(cursor)

        symptom_name = row['Symptoms']
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM symptoms WHERE Name = ?)
            BEGIN
                INSERT INTO symptoms (Name)
                VALUES (?)
            END
        """, symptom_name, symptom_name)
        symptom_id = get_scope_identity(cursor)

        # case where symptom is already there, so I use select statement instead of scope identity 
        if symptom_id is None:
                    cursor.execute("""
                        SELECT ID FROM symptoms WHERE Name = ?
                        """, symptom_name)
                    symptom_id = cursor.fetchone().ID

        print("SYMPTOM ID IS: ", symptom_id, "Paitent ID IS: ", paitent_person_id)

        if symptom_id is not None and paitent_person_id is not None:
            cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM exhibits WHERE PatientID = ? AND SymptomID = ?)
                BEGIN
                    INSERT INTO exhibits (PatientID, SymptomID)
                    VALUES (?, ?)
                END
            """, paitent_person_id, symptom_id, paitent_person_id, symptom_id)

        cursor.execute("INSERT INTO hospital (Name, Address) VALUES (?, ?)", row['Hospital Name'], row['Hospital Address'])
        hospital_id = get_scope_identity(cursor)

        if provider_person_id is not None and paitent_person_id is not None and hospital_id is not None:
            cursor.execute("""
                INSERT INTO takesCareOf (ProviderID, PatientID, HospitalID, DateOfVisit)
                VALUES (?, ?, ?, ?)
            """, provider_person_id, paitent_person_id, hospital_id, row['Diagnosis Occurance']) 

        if pd.notna(row['Medicine Name']):
            cursor.execute("INSERT INTO medicine (name) VALUES (?)", row['Medicine Name'])
            medicine_id = get_scope_identity(cursor)

            if provider_person_id is not None and paitent_person_id is not None and medicine_id is not None:

                cursor.execute("""
                    INSERT INTO prescribes (PatientID, MedicineID, ProviderID, Dose)
                    VALUES (?, ?, ?, ?)
                """, paitent_person_id, medicine_id, provider_person_id, row['Medicine Dose'])
        
        if paitent_person_id is not None and diagnosis_id:
            cursor.execute("INSERT INTO has (PatientID, DiagnosisID) VALUES (?, ?)", paitent_person_id, diagnosis_id)

# Call the function
do_data(df, cursor)

# Commit and close
cnxn.commit()
cursor.close()
cnxn.close()
