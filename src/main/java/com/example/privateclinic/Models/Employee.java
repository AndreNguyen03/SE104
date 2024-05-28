package com.example.privateclinic.Models;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String employeeCitizenId;
    private String employeeAddress;
    private String employeePhoneNumber;
    private String employeeEmail;
    private String employeePosition;
    private String employeeUsername;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeCitizenId='" + employeeCitizenId + '\'' +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", employeePhoneNumber=" + employeePhoneNumber +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", employeePosition=" + employeePosition + '\'' +
                ", employeeUsername=" + employeeUsername +
                '}';
    }

    public Employee(String employeeName, String employeeCitizenId, String employeeAddress, String employeePhoneNumber, String employeeEmail, String employeePosition, String employeeUsername) {
        this.employeeName = employeeName;
        this.employeeCitizenId = employeeCitizenId;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.employeePosition = employeePosition;
        this.employeeUsername = employeeUsername;
    }

    public Employee() {}
    public int getId() {
        return employeeId;
    }

    public void setId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return employeeName;
    }

    public void setName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCitizenId() {
        return employeeCitizenId;
    }

    public void setCitizenId(String employeeCitizenId) {
        this.employeeCitizenId = employeeCitizenId;
    }

    public String getAddress() {
        return employeeAddress;
    }

    public void setAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getPhoneNumber() {
        return employeePhoneNumber;
    }

    public void setPhoneNumber(String employeePhoneNumber) {
        this.employeePhoneNumber = employeePhoneNumber;
    }

    public String getEmail() {
        return employeeEmail;
    }

    public void setEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getPosition() {
        return employeePosition;
    }

    public void setPosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getUsername() {
        return employeeUsername;
    }

    public void setUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }
}
