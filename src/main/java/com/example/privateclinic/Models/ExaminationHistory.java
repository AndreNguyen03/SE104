package com.example.privateclinic.Models;

import javafx.collections.ObservableList;

public class ExaminationHistory {
    Customer customer;
    ObservableList<Prescribe> prescribes;
    Examination examination;

    public ExaminationHistory() {
        customer= new Customer();
        examination = new Examination();
    }

    public ExaminationHistory(Customer customer, Examination examination,ObservableList<Prescribe>  prescribes) {
        this.customer = customer;
        this.prescribes = prescribes;
        this.examination = examination;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ObservableList<Prescribe> getPrescribe() {
        return prescribes;
    }

    public void setPrescribe(ObservableList<Prescribe> prescribes) {
        this.prescribes = prescribes;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }
}
