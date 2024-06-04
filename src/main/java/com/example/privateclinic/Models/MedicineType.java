package com.example.privateclinic.Models;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class MedicineType {
    private int madt;
    private String tendt;

    public MedicineType() {
        madt=0;
        tendt="";
    }

    public int getMaDT() {
        return madt;
    }

    public void setMaDT(int madt) {
        this.madt = madt;
    }

    public String getTenDT() {
        return tendt;
    }

    public void setTenDT(String tendt) {
        this.tendt = tendt;
    }

}
