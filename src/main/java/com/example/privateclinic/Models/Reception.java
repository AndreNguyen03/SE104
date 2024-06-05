package com.example.privateclinic.Models;

import java.time.LocalDateTime;

public class Reception {
    int reception_id;
    int patient_id;
    LocalDateTime arrival_date;
    int exam_times;
    int number;
    public Reception() {
    }

    public Reception(int reception_id, int patient_id, LocalDateTime arrival_date, int exam_times, int number,String tenBenhNhan) {
        this.reception_id = reception_id;
        this.patient_id = patient_id;
        this.arrival_date = arrival_date;
        this.exam_times = exam_times;
        this.number = number;
    }

    public Reception(int patient_id, LocalDateTime arrival_date, int exam_times, int number) {
        this.patient_id = patient_id;
        this.arrival_date = arrival_date;
        this.exam_times = exam_times;
        this.number = number;
    }

    public int getReception_id() {
        return reception_id;
    }

    public void setReception_id(int reception_id) {
        this.reception_id = reception_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public LocalDateTime getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(LocalDateTime arrival_date) {
        this.arrival_date = arrival_date;
    }

    public int getExam_times() {
        return exam_times;
    }

    public void setExam_times(int exam_times) {
        this.exam_times = exam_times;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
