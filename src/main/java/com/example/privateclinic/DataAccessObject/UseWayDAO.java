package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UseWayDAO {
    public UseWayDAO() {
    }
    ConnectDB connectDB = ConnectDB.getInstance();

    public ResultSet LoadListUseWays() {
        String query ="SELECT macd,tencd FROM cachdung";
        return connectDB.getData(query);
    }

    public boolean isUseWayNameExists(String usewayName, int currentUseWayId) {
        String query = "SELECT COUNT(*) AS count FROM cachdung WHERE tencd = ? AND macd != ?";
        int count = 0;

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1, usewayName);
            statement.setInt(2, currentUseWayId);

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

    public int getUseWayIDByName(String usewayName) {
        int usewayID = -1; // Giả sử không tìm thấy

        // Kiểm tra nếu tên thuốc tồn tại trong cơ sở dữ liệu
        if (isUseWayNameExists(usewayName,0)) {
            // Truy vấn cơ sở dữ liệu để lấy mã thuốc
            String query = "SELECT macd FROM cachdung WHERE tencd = ?";
            try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
                statement.setString(1, usewayName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        usewayID = resultSet.getInt("macd");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usewayID;
    }
}
