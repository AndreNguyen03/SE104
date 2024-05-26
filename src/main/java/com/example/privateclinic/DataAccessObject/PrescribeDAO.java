package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Prescribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescribeDAO {
    public PrescribeDAO() {
    }

    public boolean addPrescribe(int examinationID, Prescribe prescribe)
    {
        String query = "INSERT INTO kethuoc (makhambenh,sothutu,mathuoc,ngay,sang,trua,chieu,toi,soluong,thanhtien)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?)";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, examinationID);
            statement.setInt(2, prescribe.getSothuTu());
            statement.setInt(3, prescribe.getMaThuoc());
            statement.setInt(4, prescribe.getNgay());
            statement.setInt(5, prescribe.getSang());
            statement.setInt(6, prescribe.getTrua());
            statement.setInt(7, prescribe.getChieu());
            statement.setInt(8, prescribe.getToi());
            statement.setInt(9, prescribe.getSoLuong());
            statement.setDouble(10, prescribe.getSoLuong());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
