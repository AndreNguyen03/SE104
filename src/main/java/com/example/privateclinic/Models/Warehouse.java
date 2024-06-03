package com.example.privateclinic.Models;

import java.sql.Date;
import java.time.LocalDate;

public class Warehouse {
    private int warehouseID;
    private int medicineID;
    private int userID;
    private int importTimes;
    private int importQuantity;
    private LocalDate importDate;
    private double importPrice;

    public Warehouse() {
    }

    public Warehouse(int warehouseID, int medicineID, int userID, int importTimes, int importQuantity, LocalDate importDate, double importPrice) {
        this.warehouseID = warehouseID;
        this.medicineID = medicineID;
        this.userID = userID;
        this.importTimes = importTimes;
        this.importQuantity = importQuantity;
        this.importDate = importDate;
        this.importPrice = importPrice;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getImportTimes() {
        return importTimes;
    }

    public void setImportTimes(int importTimes) { this.importTimes = importTimes; }

    public int getImportQuantity() {
        return importQuantity;
    }

    public void setImportQuantity(int importQuantity) { this.importQuantity = importQuantity; }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

}
