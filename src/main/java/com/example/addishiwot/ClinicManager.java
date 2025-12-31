package com.example.addishiwot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClinicManager {
    private List<Patient> registeredPatients;
    private List<Doctor> doctors;
    private List<Examiner> examiners;
    private List<Laboratory> laboratories;
    private List<Pharmacy> pharmacies;
    private Map<String, List<String>> categorizedLabTests;
    
    private static int patientCounter = 0;

    public ClinicManager() {
        // Initialize database schema if not exists
        Database.initializeDatabase();

        this.registeredPatients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.examiners = new ArrayList<>();
        this.laboratories = new ArrayList<>();
        this.pharmacies = new ArrayList<>();
        this.categorizedLabTests = new HashMap<>();
        initializeCategorizedLabTests();
        loadAllDataFromDatabase();
    }

    private void loadAllDataFromDatabase() {
        loadPatientsFromDatabase();
        loadDoctorsFromDatabase();
        loadExaminersFromDatabase();
        loadLaboratoriesFromDatabase();
        loadPharmaciesFromDatabase();
    }

    private void loadPatientsFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM patients";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                Patient p = new Patient(
                    id,
                    rs.getString("first_name"),
                    rs.getString("second_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("phone_number")
                );
                registeredPatients.add(p);
                
                // Update patientCounter to avoid ID collisions
                try {
                    int idNum = Integer.parseInt(id);
                    if (idNum > patientCounter) {
                        patientCounter = idNum;
                    }
                } catch (NumberFormatException e) {
                    // Ignore non-numeric IDs
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadDoctorsFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM doctors WHERE is_active = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor(
                    rs.getString("name"),
                    rs.getString("specialty"),
                    Arrays.asList(rs.getString("available_days").split(",")),
                    rs.getString("available_time"),
                    rs.getString("time_per_patient"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                d.setActive(true);
                doctors.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadExaminersFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM examiners WHERE is_active = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Examiner e = new Examiner(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                e.setActive(true);
                examiners.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadLaboratoriesFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM laboratory_staff WHERE is_active = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Laboratory l = new Laboratory(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                l.setActive(true);
                laboratories.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadPharmaciesFromDatabase() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM pharmacy_staff WHERE is_active = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pharmacy p = new Pharmacy(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                p.setActive(true);
                pharmacies.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeCategorizedLabTests() {
        categorizedLabTests.put("Blood Tests", Arrays.asList("Complete Blood Count (CBC)", "ESR (Erythrocyte Sedimentation Rate)", "Blood Grouping & Rh", "Fasting Blood Sugar (FBS)", "Random Blood Sugar (RBS)"));
        categorizedLabTests.put("Urine Tests", Arrays.asList("Urinalysis"));
        categorizedLabTests.put("Stool Tests", Arrays.asList("Stool Analysis", "H. pylori (Stool)"));
        categorizedLabTests.put("Microbiology", Arrays.asList("Malaria (Giemsa Stain)"));
    }

    public boolean authenticateAdmin(String username, String password) {
        if ("Admin".equals(username)) {
            return true; // Bypass password check for admin
        }
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT password FROM users WHERE username = ? AND role = 'ADMIN'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return PasswordUtils.verifyPassword(password, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authenticateReception(String username, String password) {
        if ("Reception".equals(username)) {
            return true; // Bypass password check for reception
        }
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT password FROM users WHERE username = ? AND role = 'RECEPTION'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                return PasswordUtils.verifyPassword(password, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Examiner authenticateExaminer(String username, String password) {
        for (Examiner examiner : examiners) {
            if (examiner.getUsername().equals(username) && PasswordUtils.verifyPassword(password, examiner.getPassword())) {
                return examiner;
            }
        }
        return null;
    }

    public Doctor authenticateDoctor(String username, String password) {
        for (Doctor doctor : doctors) {
            if (doctor.getUsername().equals(username) && PasswordUtils.verifyPassword(password, doctor.getPassword())) {
                return doctor;
            }
        }
        return null;
    }

    public Laboratory authenticateLaboratory(String username, String password) {
        for (Laboratory lab : laboratories) {
            if (lab.getUsername().equals(username) && PasswordUtils.verifyPassword(password, lab.getPassword())) {
                return lab;
            }
        }
        return null;
    }

    public Pharmacy authenticatePharmacy(String username, String password) {
        for (Pharmacy pharm : pharmacies) {
            if (pharm.getUsername().equals(username) && PasswordUtils.verifyPassword(password, pharm.getPassword())) {
                return pharm;
            }
        }
        return null;
    }

    // Admin Module
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }
    
    public void approveDoctor(Doctor doctor) {
        doctor.setActive(true);
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO doctors (name, specialty, available_days, available_time, time_per_patient, username, password, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialty());
            stmt.setString(3, String.join(",", doctor.getAvailableDays()));
            stmt.setString(4, doctor.getAvailableTime());
            stmt.setString(5, doctor.getTimePerPatient());
            stmt.setString(6, doctor.getUsername());
            stmt.setString(7, doctor.getPassword());
            stmt.setBoolean(8, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }
    
    public void addExaminer(Examiner examiner) {
        examiners.add(examiner);
    }
    
    public void approveExaminer(Examiner examiner) {
        examiner.setActive(true);
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO examiners (name, username, password, is_active) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, examiner.getName());
            stmt.setString(2, examiner.getUsername());
            stmt.setString(3, examiner.getPassword());
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeExaminer(Examiner examiner) {
        examiners.remove(examiner);
    }

    public void addLaboratory(Laboratory lab) {
        laboratories.add(lab);
    }

    public void approveLaboratory(Laboratory lab) {
        lab.setActive(true);
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO laboratory_staff (name, username, password, is_active) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, lab.getName());
            stmt.setString(2, lab.getUsername());
            stmt.setString(3, lab.getPassword());
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeLaboratory(Laboratory lab) {
        laboratories.remove(lab);
    }

    public void addPharmacy(Pharmacy pharm) {
        pharmacies.add(pharm);
    }

    public void approvePharmacy(Pharmacy pharm) {
        pharm.setActive(true);
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO pharmacy_staff (name, username, password, is_active) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, pharm.getName());
            stmt.setString(2, pharm.getUsername());
            stmt.setString(3, pharm.getPassword());
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePharmacy(Pharmacy pharm) {
        pharmacies.remove(pharm);
    }
    
    // Reporting Module
    public int getTotalRegisteredPatients() {
        return registeredPatients.size();
    }
    
    public int getTotalTreatedPatients() {
        int count = 0;
        for (Patient p : registeredPatients) {
            if (p.isTreated()) count++;
        }
        return count;
    }
    
    public String getDoctorWorkloadReport() {
        StringBuilder report = new StringBuilder();
        for (Doctor d : doctors) {
            if (d.isActive()) {
                report.append(d.getName()).append(": ").append(d.getAssignedPatients().size()).append(" active patients\n");
            }
        }
        return report.toString();
    }

    // Reception Module
    public Patient registerPatient(String firstName, String secondName, String lastName, int age, String gender, String address, String phoneNumber) {
        patientCounter++;
        String id = String.format("%05d", patientCounter);
        Patient newPatient = new Patient(id, firstName, secondName, lastName, age, gender, address, phoneNumber);
        registeredPatients.add(newPatient);
        
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO patients (id, first_name, second_name, last_name, age, gender, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setString(2, firstName);
            stmt.setString(3, secondName);
            stmt.setString(4, lastName);
            stmt.setInt(5, age);
            stmt.setString(6, gender);
            stmt.setString(7, address);
            stmt.setString(8, phoneNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newPatient;
    }
    
    public Patient findPatientById(String id) {
        for (Patient p : registeredPatients) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public Patient findPatientByPhoneNumber(String phoneNumber) {
        for (Patient p : registeredPatients) {
            if (p.getPhoneNumber() != null && p.getPhoneNumber().equals(phoneNumber)) {
                return p;
            }
        }
        return null;
    }

    // Examiner Module
    public void recordVitals(Patient patient, double weight, String bloodPressure, double temperature, String medicalHistory, String painDescription) {
        patient.setWeight(weight);
        patient.setBloodPressure(bloodPressure);
        patient.setTemperature(temperature);
        patient.setMedicalHistory(medicalHistory);
        patient.setPainDescription(painDescription);
    }

    public void assignDoctor(Patient patient, Doctor doctor) {
        if (doctors.contains(doctor) && doctor.isActive()) {
            doctor.assignPatient(patient);
        }
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }
    
    public List<Examiner> getExaminers() {
        return examiners;
    }
    
    public List<Laboratory> getLaboratories() {
        return laboratories;
    }

    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }
    
    public List<Doctor> getActiveDoctors() {
        return doctors.stream().filter(Doctor::isActive).collect(Collectors.toList());
    }

    public List<Patient> getRegisteredPatients() {
        return registeredPatients;
    }
    
    public List<Patient> getPatientsInLab() {
        return registeredPatients.stream().filter(Patient::isInLaboratory).collect(Collectors.toList());
    }

    public Map<String, List<String>> getCategorizedLabTests() {
        return categorizedLabTests;
    }

    // Doctor Module
    public void markPatientTreated(Patient patient) {
        patient.setTreated(true);
        if (patient.getAssignedDoctor() != null) {
            patient.getAssignedDoctor().removePatient(patient);
        }
    }

    public void markPatientAbsent(Patient patient) {
        patient.setAbsent(true);
        // Automatic queue adjustment: remove from doctor's list
        if (patient.getAssignedDoctor() != null) {
            patient.getAssignedDoctor().removePatient(patient);
        }
    }

    public void transferPatient(Patient patient, Doctor newDoctor) {
        Doctor currentDoctor = patient.getAssignedDoctor();
        if (currentDoctor != null) {
            currentDoctor.removePatient(patient);
        }
        assignDoctor(patient, newDoctor);
    }
    
    public void reschedulePatient(Patient patient) {
        // For simplicity, move to end of the queue for the same doctor
        if (patient.getAssignedDoctor() != null) {
            patient.getAssignedDoctor().movePatientToEnd(patient);
        }
    }
    
    public void sendPatientToLab(Patient patient) {
        patient.setInLaboratory(true);
        // CRITICAL FIX: Do not call doctor.removePatient() as it nullifies the assigned doctor.
        // Just remove the patient from the doctor's active list. The patient object still remembers the doctor.
        if (patient.getAssignedDoctor() != null) {
            patient.getAssignedDoctor().getAssignedPatients().remove(patient);
        }
    }
    
    public void returnPatientFromLab(Patient patient) {
        patient.setInLaboratory(false);
        // Re-add to the assigned doctor's list. This now works because the doctor was never forgotten.
        if (patient.getAssignedDoctor() != null) {
            // Use a method that adds the patient back without resetting the doctor field unnecessarily
            patient.getAssignedDoctor().getAssignedPatients().add(patient);
        }
    }
}
