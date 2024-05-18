package com.example.privateclinic.Models;

import java.sql.ResultSet;

public class Unit { // Đơn vị tính
    private String MaDVT;
    private String TenDVT;

    public Unit() {
    }

    public ResultSet LoadList()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT madvt, tendvt FROM donvitinh";
        return connect.getData(query);

    }
}
