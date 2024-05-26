package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.ResultSet;

public class MedicineTypeDAO {
    public MedicineTypeDAO() {
    }

    public ResultSet LoadListMedicines()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT madt, tendt FROM dangthuoc";
        return connect.getData(query);
    }
}
