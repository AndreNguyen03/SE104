package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class UnitDAO {
    public UnitDAO() {
    }

    public ResultSet LoadListUnits()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT madvt, tendvt FROM donvitinh";
        return connect.getData(query);
    }
}
