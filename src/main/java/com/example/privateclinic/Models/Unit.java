package com.example.privateclinic.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Unit { // Đơn vị tính
    private int madvt;
    private String tendvt;

    public int getMaDVT() {
        return madvt;
    }

    public void setMaDVT(int madvt) {
        this.madvt = madvt;
    }

    public String getTenDVT() {
        return tendvt;
    }

    public void setTenDVT(String tendvt) {
        this.tendvt = tendvt;
    }

    public Unit() {
    }


}
