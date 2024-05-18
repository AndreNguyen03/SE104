package com.example.privateclinic.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Unit { // Đơn vị tính
    private String MaDVT;
    private String TenDVT;

    public String getMaDVT() {
        return MaDVT;
    }

    public void setMaDVT(String maDVT) {
        MaDVT = maDVT;
    }

    public String getTenDVT() {
        return TenDVT;
    }

    public void setTenDVT(String tenDVT) {
        TenDVT = tenDVT;
    }

    public Unit() {
    }


}
