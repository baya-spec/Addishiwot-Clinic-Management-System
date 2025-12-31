Addis Hiwot Clinic Management System

Welcome to the Addis Hiwot Clinic project. This project is a small clinic management application made with Java and JavaFX. It is written for learning and small clinics.

What this app does
This app helps clinic staff manage patients and daily work. It supports:
- Registering new patients
- Logging visits and appointments
- Recording basic exam results and notes
- Sending patients to the laboratory for tests
- Tracking lab test results
- Creating and tracking prescriptions from the pharmacy
- Managing staff accounts and roles

Who can use the app
- Receptionists: register patients and make appointments
- Examiners: record vitals and exam notes
- Doctors: view patient records, write prescriptions, and refer to lab or pharmacy
- Laboratory staff: record and upload test results
- Pharmacy staff: view prescriptions and mark them as dispensed
- Administrator: approve staff accounts and manage the system

How the app works (simple steps)
1. Start the app. The app opens a login screen.
2. A staff member logs in using their username and password.
3. The app checks the user role and shows pages for that role.
4. Reception can add a new patient with name, contact, and basic info.
5. Examiner or doctor can open a patient record and write exam notes.
6. Doctor can request lab tests or write prescriptions for the pharmacy.
7. Lab staff add test results to the patient record when ready.
8. Pharmacy staff mark prescriptions as given to the patient.
9. Admin can approve new staff accounts and see system logs.

Technical overview
- Language: Java 11 or later is recommended.
- UI: JavaFX is used for the graphical user interface.
- Database: A local SQL database is used. The schema is in the `src/main/resources/schema.sql` file.
- Passwords: Passwords are hashed for safety before storing in the database.

Quick setup (run on your computer)
1. Install Java 11 or newer on your system.
2. Install Maven if you will run with Maven.
3. Open a terminal in the project root folder.
4. To run with Maven use:

```bash
mvn javafx:run
```

5. Or run the `Launcher` or `HelloApplication` class from your IDE.

Key files and where to look first
- `src/main/java/com/example/addishiwot-clinic/HelloApplication.java` — the JavaFX application starter
- `src/main/java/com/example/addishiwot-clinic/Launcher.java` — an alternate launcher class
- `src/main/java/com/example/addishiwot-clinic/HelloController.java` — main UI controller and event handlers
- `src/main/java/com/example/addishiwot-clinic/ClinicManager.java` — core business logic and helpers
- `src/main/java/com/example/addishiwot-clinic/Database.java` — database connection and helpers
- `src/main/java/com/example/addishiwot-clinic/PasswordUtils.java` — password hashing utilities
- `src/main/resources/schema.sql` — database schema and table creation statements
- `src/main/resources/com/example/demo/hello-view.fxml` — the main UI layout file
- `src/main/resources/com/example/demo/styles.css` — styles for the app UI

Important notes and tips
- When you create a new staff account (doctor, lab, pharmacy, examiner), the account may start as "pending". An administrator must approve the account before it can be used.
- Passwords are not stored as plain text. They use a secure hash before saving in the database.
- If the app cannot connect to the database, check the database URL and settings in `Database.java`.
- The app is designed for learning and small use. For production use you should add stronger security, backups, and more testing.

How to contribute
- Report bugs by opening an issue on GitHub. Give steps to reproduce the problem.
- Make small, focused changes. Create a new branch for your work.
- Write a clear commit message and a short description in your pull request.
- If you change the database schema, update `schema.sql` and explain the change.

Ideas for improvement
- Add patient appointment reminders by email or SMS
- Improve role-based access control with finer permissions
- Add export and import of patient data in CSV format
- Add automated tests for key functions and UI flows

Contact and license
This project is free to use for learning and improvement. If you want help or wish to contribute, open an issue or a pull request on GitHub.

Thank you for checking out the Addis Hiwot Clinic project.

