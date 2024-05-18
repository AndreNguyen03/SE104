package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class UseWayDAO {
    public UseWayDAO() {
    }

    public ResultSet LoadListUseWays() {
        ConnectDB connectDB = new ConnectDB();
        String query ="SELECT macd,tencd FROM cachdung";
        return connectDB.getData(query);
    }
}
