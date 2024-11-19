# Hospital Database GUI

**Hospital Database GUI** is a Java-based application supplemented with a Python data import script for managing hospital data. It provides an interactive graphical interface and back-end capabilities for administrators, healthcare providers, and patients. The system enables secure authentication, efficient database operations, and automated data imports for large-scale management. This project uses Java Swing for 

---

## Features

### Java Application

#### User Roles
1. **Admin**:
   - Comprehensive access to hospital data, including patient and provider records.
   - Perform CRUD operations and manage hospital details.

2. **Provider**:
   - Access assigned patient records.
   - Add, update, or delete medical data such as symptoms, medications, and diagnoses.

3. **Patient**:
   - Securely view personal medical records.

#### Core Functionalities
- **Secure Authentication**:
  - Role-based login using hashed passwords and salting.

- **Dynamic Table Management**:
  - Real-time interaction with hospital records through dynamic tables.
  - Event-driven updates for data consistency.

- **CRUD Operations**:
  - Add, update, and delete records for patients, providers, and hospitals.
  - Database operations secured with stored procedures.

- **Interactive GUI**:
  - Java Swing-based interface for seamless navigation and usability.

---

#### Features
- **Data Validation**:
  - Validates dates, medicines, and boolean fields for accurate data insertion.

- **Automated Database Integration**:
  - Supports importing patient, provider, hospital, medicine, and diagnosis data.
  - Ensures no duplicate entries using conditional SQL statements.

- **Relationship Mapping**:
  - Establishes relationships between entities such as patients and providers or patients and medicines.

- **Error Handling**:
  - Handles invalid or missing data with fallback mechanisms.

---

## Workflow

1. **Login and Role Selection**:
   - Users log in as Admin, Provider, or Patient.
   
2. **Admin Panel**:
   - Perform CRUD operations on all hospital entities.
   - Manage hospital details and oversee provider-patient interactions.

3. **Provider Panel**:
   - Access assigned patient records and update medical details.
   - View symptoms, diagnoses, and prescriptions.

4. **Patient Panel**:
   - Securely access personal medical records.

5. **Data Import**:
   - Automates the creation of relationships between entities.

---

## Highlights
- Modular design combining Java and Python for robust hospital data management.
- Secure and efficient database operations with real-time GUI updates.
- Batch data processing capabilities for large-scale data imports.

---

This project allows for hospital data management, ensuring security, accuracy, and scalability for healthcare organizations.
