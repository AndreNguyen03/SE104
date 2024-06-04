package com.example.privateclinic.Models;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class UseWay {
    private int macd;
    private String tencd;
    public UseWay() {
        macd = 0;
        tencd ="";
    }
    public int getMaCD() {
        return macd;
    }

    public void setMaCD(int macd) {
        this.macd = macd;
    }

    public String getTenCD() {
        return tencd;
    }

    public void setTenCD(String tencd) {
        this.tencd = tencd;
    }

}
