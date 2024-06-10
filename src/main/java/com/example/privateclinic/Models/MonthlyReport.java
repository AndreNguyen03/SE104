package com.example.privateclinic.Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MonthlyReport {
    String date;
    int patientCount;
    int revenue;
    double average;
    double rate;

    public MonthlyReport() {
    }

    public MonthlyReport(String date, int patientCount, int revenue, double average, double rateColumn) {
        this.date = date;
        this.patientCount = patientCount;
        this.revenue = revenue;
        this.average = average;
        this.rate = rateColumn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
