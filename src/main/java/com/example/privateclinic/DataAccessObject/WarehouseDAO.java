package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Medicine;
import com.example.privateclinic.Models.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {
    ConnectDB connectDB = ConnectDB.getInstance();

    public boolean addWarehouse(Warehouse warehouse) {
        LocalDate now = LocalDate.now();

        String query = "INSERT INTO nhapkho (manhap, mathuoc, manv, lannhap, soluong, ngay, gia) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, warehouse.getWarehouseID());
            statement.setInt(2, warehouse.getMedicineID());
            statement.setInt(3, warehouse.getUserID());
            statement.setInt(4, warehouse.getImportTimes());
            statement.setInt(5, warehouse.getImportQuantity());
            statement.setDate(6, Date.valueOf(now));
            statement.setDouble(7, warehouse.getImportPrice());

             return statement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWarehouse(int medicineId) {
        String query = "DELETE FROM nhapkho WHERE mathuoc = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, medicineId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getNextWarehouseId() {
        String query = "SELECT MAX(manhap) FROM nhapkho";

        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                return maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 1;
    }

    public int getMaxImportTimes(int medicineId) {
        String query = "SELECT MAX(lannhap) FROM nhapkho WHERE mathuoc = ?";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, medicineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return 0;
    }
}
