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
}
