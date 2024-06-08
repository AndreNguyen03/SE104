package com.example.privateclinic.Models;

import java.time.LocalDate;

public class MonthlyReport {
    private LocalDate date;
    private int patientCount;
    private double revenue;
    private double rate;
    private int month;

    // Constructor chấp nhận ngày thay vì số nguyên biểu diễn tháng
    public MonthlyReport(int patientCount, double revenue, double rate) {
        this.date = date;
        this.patientCount = patientCount;
        this.revenue = revenue;
        this.rate = rate;
    }

    // Thêm constructor để chấp nhận số nguyên biểu diễn tháng
    public MonthlyReport(int month, int patientCount, double revenue, double rate) {
        // Tính toán ngày đầu tiên của tháng từ số nguyên biểu diễn tháng
        this.date = LocalDate.of(LocalDate.now().getYear(), month, 1);
        this.patientCount = patientCount;
        this.revenue = revenue;
        this.rate = rate;
    }

    public LocalDate getStartDateOfMonth() {
        return LocalDate.of(LocalDate.now().getYear(), month, 1);
    }

    public LocalDate getEndDateOfMonth() {
        int lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1).lengthOfMonth();
        return LocalDate.of(LocalDate.now().getYear(), month, lastDayOfMonth);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
