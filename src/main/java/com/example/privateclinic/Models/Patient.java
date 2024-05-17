package com.example.privateclinic.Models;

import java.sql.Date;

public class Patient {
    private int patientId;
    private String patientName;
    private String patientGender;
    private String patientPhoneNumber;
    private Date patientBirth;
    private String patientAddress;


    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientGender='" + patientGender + '\'' +
                ", patientPhoneNumber='" + patientPhoneNumber + '\'' +
                ", patientBirth=" + patientBirth +
                ", patientAddress='" + patientAddress + '\'' +
                '}';
    }

    public Patient(int patientId, String patientName, String patientGender, String patientPhoneNumber, Date patientBirth, String patientAddress) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientGender = patientGender;
        this.patientPhoneNumber = patientPhoneNumber;
        this.patientBirth = patientBirth;
        this.patientAddress = patientAddress;
    }


    public Date getPatientBirth() {
        return patientBirth;
    }

    public void setPatientBirth(Date patientBirth) {
        this.patientBirth = patientBirth;
    }
    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public Patient() {
    }


    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientPhoneNumber() {
        return patientPhoneNumber;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber) {
        this.patientPhoneNumber = patientPhoneNumber;
    }
}
