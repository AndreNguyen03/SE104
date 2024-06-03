package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitDAO {

    ConnectDB connectDB = ConnectDB.getInstance();
    public UnitDAO() {
    }

    public ResultSet LoadListUnits()
    {
        ConnectDB connect = new ConnectDB();
        String query ="SELECT madvt, tendvt FROM donvitinh";
        return connect.getData(query);
    }

    public boolean isUnitNameExists(String unitName, int currentUnitId) {
        String query = "SELECT COUNT(*) AS count FROM donvitinh WHERE tendvt = ? AND madvt != ?";
        int count = 0;

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1, unitName);
            statement.setInt(2, currentUnitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count > 0;
    }

    public int getUnitIDByName(String unitName) {
        int unitID = -1; // Giả sử không tìm thấy

        // Kiểm tra nếu tên thuốc tồn tại trong cơ sở dữ liệu
        if (isUnitNameExists(unitName,0)) {
            // Truy vấn cơ sở dữ liệu để lấy mã thuốc
            String query = "SELECT madvt FROM donvitinh WHERE tendvt = ?";
            try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
                statement.setString(1, unitName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        unitID = resultSet.getInt("madvt");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return unitID;
    }
}
