from datetime import datetime
import pandas as pd
import pyodbc

class Person_cl:
    def __init__(self, fName, minit, lName, dob, id = None):
        self.fName = fName
        self.lName = lName
        self.minit = minit
        self.dob = dob
        self.id = id
    def setID(self, newID):
        self.id = newID
    def getID(self):
        return self.id
    
class Hospital_cl:
    def __init__(self, hName, hAddress, id = None):
        self.hName = hName
        self.hAddress = hAddress
        self.id = id
    def getID(self):
        return self.id

class Medicine_cl:
    def __init__(self, mName, id = None):
        self.mName = mName
        self.id = id
    def getID(self):
        return self.id
    
class Diagnosis_cl:
    def __init__(self, dName, id = None):
        self.dName = dName
        self.id = id
    def getID(self):
        return self.id
    
class Symptom_cl: 
    def __init__(self, sName, id = None):
        self.sName = sName
        self.id = id
    def getID(self):
        return self.id
    
###################################################################################################################################

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
    
def get_scope_identity(cursor):
    cursor.execute("SELECT @@IDENTITY AS ID;")
    # cursor.execute("SELECT SCOPE_IDENTITY() AS ID;")
    result = cursor.fetchone()
    if result:
         return str(result.ID)
    else:
        return "nothing!!!"

def is_med_valid(value):
    if value.lower() == "nothing":
        return False
    else:
        return True


def do_data(df, cursor):
    for index, row in df.iterrows():

        person_list = []
        hospital_list = []
        medicine_list = []
        diagnosis_list = []
        symptom_list = []

        pat_fName = row['Patient Name'].split()[0]
        pat_minit = None
        pat_lName = row['Patient Name'].split()[-1]
        pat_dob = format_date(row['Patient DOB']) 
        pat_id = None
        pro_fName = row['Provider Name'].split()[0]
        pro_minit = None
        pro_lName = row['Provider Name'].split()[-1]
        pro_id = None
        pro_dob = format_date(row['Provider DOB'])
        pro_can = yes_no(row['Can Prescribe'])
        pro_spec = row['Provider Speciality']
        hos_name = row['Hospital Name']
        hos_addr = str(row['Hospital Address'])
        hos_id = None
        med_valid = is_med_valid(str(row['Medicine Name']))
        if med_valid:
            med_name = str(row['Medicine Name'])
        else:
            med_name = ''
        med_dose = row['Medicine Dose']
        med_id = None
        diag_name = row['Diagnosis Name']
        diag_freq = row['Diagnosis Frequency']
        diag_occ =  row['Diagnosis Occurance']
        diag_id = None
        sym_name = row['Symptoms']
        sym_id = None
                
        ###################################################################################################################################

        # INSERT TO PAITENT/PERSON
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
            BEGIN
                INSERT INTO person (FirstName, LastName, DOB)
                VALUES (?, ?, ?)
            END
        """, pat_fName, pat_lName, pat_dob,
                pat_fName, pat_lName, pat_dob)

        pat_id = get_scope_identity(cursor)

        if(pat_id is None or pat_id == "nothing!!!" or
                pat_id == pro_id or pat_id == med_id or pat_id == diag_id or pat_id == hos_id or pat_id == sym_id):
            for pat in person_list:
                if pat.fName == pat_fName and pat.lName == pat_lName and pat_dob == pat.dob:
                    pat_id = pat.id

        if(pat_id is not None):
            person_list.append(Person_cl(pat_fName, pat_minit, pat_lName, pat_dob, pat_id))

        #INSERT INTO PAITENT
        cursor.execute("""
                        IF NOT EXISTS (SELECT 1 FROM patients WHERE id = ?)
                        BEGIN
                            INSERT INTO patients (ID) VALUES (?)
                        END
                        """, pat_id, pat_id)

        
###################################################################################################################################

        #INSERT INTO PERSON/PROVIDER
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM person WHERE FirstName = ? AND LastName = ? AND DOB = ?)
            BEGIN
                INSERT INTO person (FirstName, LastName, DOB)
                VALUES (?, ?, ?)
            END
        """, pro_fName, pro_lName, pro_dob,
                pro_fName, pro_lName, pro_dob)
        
        pro_id = get_scope_identity(cursor)

        if(pro_id is None or pro_id == "nothing!!!" or
                    pat_id == pro_id or pat_id == med_id or pro_id == diag_id or pro_id == hos_id or pro_id == sym_id):
            for pro in person_list:
                if pro.fName == pro_fName and pro.lName == pro_lName and pro_dob == pro.dob:
                    pro_id = pro.id

        if(pro_id is not None):
            person_list.append(Person_cl(pro_fName, pro_minit, pro_lName, pro_dob, pro_id))

        # INESRT INTO PROVIDOR
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM providers WHERE ID = ?)
            BEGIN
                INSERT INTO providers (ID, Speciality, CanPrescribe)
                VALUES (?, ?, ?)
            END
        """, pro_id, pro_id, pro_spec, pro_can)
            
###################################################################################################################################

        # INSERT INTO DIAGNOSIS
        cursor.execute("""
           IF NOT EXISTS (SELECT 1 FROM diagnosis WHERE Name = ?)
            BEGIN
                INSERT INTO diagnosis (Name, Occurrence, Frequency)
                VALUES (?, ?, ?)
            END
        """, diag_name, diag_name, diag_occ, diag_freq)
        
        diag_id = get_scope_identity(cursor)

        if(diag_id is None or diag_id == "nothing!!!" or
                    diag_id == pro_id or diag_id == med_id or diag_id == pat_id or diag_id == hos_id or diag_id == sym_id):
            for diag in medicine_list:
                if diag.dName == diag_name:
                    diag_id = diag.id

        if(diag_id is not None):
            diagnosis_list.append(Diagnosis_cl(diag_name, diag_id))

###################################################################################################################################

        # INSERT INTO SYMPTOMS
        cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM symptoms WHERE Name = ?)
            BEGIN
                INSERT INTO symptoms (Name)
                VALUES (?)
            END
        """, sym_name, sym_name)

        sym_id = get_scope_identity(cursor)

        if(sym_id is not None or sym_id == "nothing!!!" or
                sym_id == pro_id or sym_id == med_id or sym_id == diag_id or sym_id == hos_id or sym_id == pat_id):
            symptom_list.append(Symptom_cl(sym_name, sym_id))

        if(sym_id is None):
            print('so')
            for sym in symptom_list:
                if sym.sName == sym_name:
                    sym_id = sym.id
                    print('did it')

###################################################################################################################################

        # INSERT INTO EXHIBITS
        print(sym_name, sym_id, pat_id)
        if sym_id is not None and pat_id is not None:
            cursor.execute("""
            IF NOT EXISTS (SELECT 1 FROM exhibits WHERE PatientID = ? AND SymptomID = ?)
                BEGIN
                    INSERT INTO exhibits (PatientID, SymptomID)
                    VALUES (?, ?)
                END
            """, pat_id, sym_id, pat_id, sym_id)

###################################################################################################################################

        # INSERT INTO HOSPITAL
        cursor.execute("""
                       IF NOT EXISTS (SELECT 1 FROM hospital WHERE Name = ? AND Address = ?)
                       INSERT INTO hospital (Name, Address) VALUES (?, ?)
                       """,
                       hos_name, hos_addr, hos_name, hos_addr)
        
        hos_id = get_scope_identity(cursor)
        if(hos_id is None or hos_id == "nothing!!!" or
                hos_id == pro_id or hos_id == med_id or hos_id == diag_id or hos_id == pat_id or hos_id == sym_id):
            for hos in hospital_list:
                if hos.hName == hos_name:
                    hos_id = hos_id

        if(hos_id is not None):
            hospital_list.append(Hospital_cl(hos_name, str(hos_addr), hos_id))

###################################################################################################################################

        # INSERT INTO TAKESCAREOF

        if pro_id is not None:
            if pat_id is not None:
                if hos_id is not None:
                    cursor.execute("""
                        INSERT INTO takesCareOf (ProviderID, PatientID, HospitalID, DateOfVisit)
                        VALUES (?, ?, ?, ?)
                    """, pro_id, pat_id, hos_id, diag_occ) 

###################################################################################################################################
        if med_valid:
            # INSERT INTO MEDICINE
            cursor.execute("""
                            IF NOT EXISTS (SELECT 1 FROM medicine WHERE Name = ?)
                            BEGIN
                                INSERT INTO medicine (name) VALUES (?)
                            END
                            """
                            , med_name, med_name)
            
            med_id = get_scope_identity(cursor)

            if(med_id is None or med_id == "nothing!!!" or
                    med_id == pro_id or med_id == pat_id or med_id == diag_id or med_id == hos_id or med_id == sym_id):
                for med in medicine_list:
                    if med.mName == med_name:
                        med_id = med.id

            if med_id:
                medicine_list.append(Medicine_cl(med_name, med_id))
###################################################################################################################################

        # INSERT INTO PRESCRIBES
        if pat_id is not None:
            if med_valid:
                if pro_id is not None:
                    cursor.execute("""
                        IF NOT EXISTS (SELECT 1 FROM prescribes WHERE PatientID = ? AND MedicineID = ? AND ProviderID = ? AND Dose = ?)
                        BEGIN
                            INSERT INTO prescribes (PatientID, MedicineID, ProviderID, Dose)
                            VALUES (?, ?, ?, ?)
                        END
                    """, pat_id, med_id, pro_id, med_dose,
                            pat_id, med_id, pro_id, med_dose)

###################################################################################################################################

        # INSERT INTO HAS
        if pat_id is not None and diag_id is not None:
            cursor.execute("""
                           IF NOT EXISTS(SELECT 1 FROM has WHERE PatientID = ? AND DiagnosisID = ?)
                           BEGIN   
                                INSERT INTO has(PatientID, DiagnosisID) VALUES (?, ?)
                           END
                           """, pat_id, diag_id, pat_id, diag_id)

# Call the function
do_data(df, cursor)

# Commit and close
cnxn.commit()
cursor.close()
cnxn.close()

###################################################################################################################################

