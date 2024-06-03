package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class UseWayDAO {
    public UseWayDAO() {
    }
    ConnectDB connectDB = ConnectDB.getInstance();

    public ResultSet LoadListUseWays() {
        String query ="SELECT macd,tencd FROM cachdung";
        return connectDB.getData(query);
    }
}
