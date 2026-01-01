package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String id;
    private String firstName;
    private String secondName;
    private String lastName;
    private int age;
    private String gender;
    private String address;
    private String phoneNumber;
    private double weight;
    private String bloodPressure;
    private String medicalHistory;
    private double temperature;
    private String painDescription;
    private Doctor assignedDoctor;
    private boolean isTreated;
    private boolean isAbsent;
    private boolean isInLaboratory;
    private String labResults;
    private boolean isInPharmacy;
    private String prescription;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private List<String> orderedLabTests;
    private String sentBy; // To store the username of the sender

    public Patient(String id, String firstName, String secondName, String lastName, int age, String gender, String address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isTreated = false;
        this.isAbsent = false;
        this.isInLaboratory = false;
        this.isInPharmacy = false;
        this.orderedLabTests = new ArrayList<>();
    }

    public String getFullName() {
        return firstName + " " + (secondName != null ? secondName + " " : "") + lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getPainDescription() {
        return painDescription;
    }

    public void setPainDescription(String painDescription) {
        this.painDescription = painDescription;
    }

    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    public void setAssignedDoctor(Doctor assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
    }

    public boolean isTreated() {
        return isTreated;
    }

    public void setTreated(boolean treated) {
        isTreated = treated;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public boolean isInLaboratory() {
        return isInLaboratory;
    }

    public void setInLaboratory(boolean inLaboratory) {
        this.isInLaboratory = inLaboratory;
    }

    public String getLabResults() {
        return labResults;
    }

    public void setLabResults(String labResults) {
        this.labResults = labResults;
    }

    public boolean isInPharmacy() {
        return isInPharmacy;
    }

    public void setInPharmacy(boolean inPharmacy) {
        this.isInPharmacy = inPharmacy;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public List<String> getOrderedLabTests() {
        return orderedLabTests;
    }

    public void setOrderedLabTests(List<String> orderedLabTests) {
        this.orderedLabTests = orderedLabTests;
    }
    
    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    @Override
    public String toString() {
        return getFullName() + " (Phone: " + phoneNumber + ")";
    }
}
