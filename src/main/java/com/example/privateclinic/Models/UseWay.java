package com.example.privateclinic.Models;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class UseWay {
    private String macd;
    private String tencd;
    public UseWay() {
        macd = "";
        tencd ="";
    }

    public ResultSet LoadList()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT macd, tencd FROM cachdung";
        return connect.getData(query);

    }
}
