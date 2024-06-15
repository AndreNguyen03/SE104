package com.example.privateclinic.Models.Report;

public class DoctorReport{
    int employId;
    String employName;
    String employPosition;
    int turn;
    int total;

    public DoctorReport() {
    }

    public DoctorReport(int employId, String employName, String employPosition, int turn, int tongtien) {
        this.employId = employId;
        this.employName = employName;
        this.employPosition = employPosition;
        this.turn = turn;
        this.total = tongtien;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getEmployId() {
        return employId;
    }

    public void setEmployId(int employId) {
        this.employId = employId;
    }

    public String getEmployName() {
        return employName;
    }

    public void setEmployName(String employName) {
        this.employName = employName;
    }

    public String getEmployPosition() {
        return employPosition;
    }

    public void setEmployPosition(String employPosition) {
        this.employPosition = employPosition;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
