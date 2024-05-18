package com.example.privateclinic.Models;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class MedicineType {
    private String maLoaiThuoc;
    private String tenDangThuoc;

    public MedicineType() {
        maLoaiThuoc="";
        tenDangThuoc="";
    }

    public ResultSet LoadList()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT madt, tendt FROM dangthuoc";
        return connect.getData(query);

    }
}
