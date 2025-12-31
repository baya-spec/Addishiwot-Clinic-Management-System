package com.example.addishiwot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private String name;
    private String specialty;
    private List<String> availableDays;
    private String availableTime;
    private String timePerPatient; // e.g., "15 mins"
    private List<Patient> assignedPatients;
    private String username;
    private String password;
    private boolean isActive;
    private LocalDateTime registrationTime;

    public Doctor(String name, String specialty, List<String> availableDays, String availableTime, String timePerPatient, String username, String password) {
        this.name = name;
        this.specialty = specialty;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
        this.timePerPatient = timePerPatient;
        this.assignedPatients = new ArrayList<>();
        this.username = username;
        this.password = password;
        this.isActive = false; // Default to inactive until approved
        this.registrationTime = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public List<String> getAvailableDays() {
        return availableDays;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public String getTimePerPatient() {
        return timePerPatient;
    }

    public List<Patient> getAssignedPatients() {
        return assignedPatients;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void assignPatient(Patient patient) {
        if (!assignedPatients.contains(patient)) {
            assignedPatients.add(patient);
        }
        patient.setAssignedDoctor(this);
    }

    public void removePatient(Patient patient) {
        assignedPatients.remove(patient);
        if (patient.getAssignedDoctor() == this) {
            patient.setAssignedDoctor(null);
        }
    }

    public void movePatientToEnd(Patient patient) {
        if (assignedPatients.remove(patient)) {
            assignedPatients.add(patient);
        }
    }

    @Override
    public String toString() {
        return name + " (" + specialty + ")" + (isActive ? "" : " [INACTIVE]");
    }
}
