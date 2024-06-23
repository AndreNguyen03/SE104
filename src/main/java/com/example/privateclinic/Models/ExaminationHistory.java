package com.example.privateclinic.Models;

import javafx.collections.ObservableList;

public class ExaminationHistory {
    Patient patient;
    ObservableList<Receipt> prescribes;
    Examination examination;

    public ExaminationHistory() {
        patient= new Patient();
        examination = new Examination();
    }

    public ExaminationHistory(Patient patient, Examination examination,ObservableList<Receipt>  prescribes) {
        this.patient = patient;
        this.prescribes = prescribes;
        this.examination = examination;
    }

    public Patient getCustomer() {
        return patient;
    }

    public void setCustomer(Patient patient) {
        this.patient = patient;
    }

    public ObservableList<Receipt> getPrescribe() {
        return prescribes;
    }

    public void setPrescribe(ObservableList<Receipt> prescribes) {
        this.prescribes = prescribes;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    public String getPatientName() {
        return patient.getPatientName();
    }
    public int getPatientId() {
        return patient.getPatientId();
    }
    public String getPatientPhoneNumber() {
        return patient.getPatientPhoneNumber();
    }
    public int getNumber() {
        return patient.getNumber();
    }

    public String getPatientAddress() {
        return patient.getPatientAddress();
    }
    public String getTenNhanVien() {
        return examination.getTenNhanVien();
    }
    public String getNgay() {
        return examination.getNgay();
    }

    public Patient getPatient() {
        return patient;
    }

    public int getMatn() {
        return examination.getMatn();
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public String getTenBenh() {
        return examination.getMainDisease().getTenBenh();
    }
    public String getTrieuChung() {
        return examination.getTrieuChung();
    }

}
