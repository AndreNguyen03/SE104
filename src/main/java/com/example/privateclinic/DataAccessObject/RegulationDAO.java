package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Medicine;
import com.example.privateclinic.Models.Regulation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegulationDAO {

    ConnectDB connectDB = ConnectDB.getInstance();
    public RegulationDAO() {
    }

    public int getRegulationValueById(int regulationId) {
        int regulationValue = -1;
        String query = "SELECT giatri FROM quydinh WHERE maqd = ?";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, regulationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    regulationValue = resultSet.getInt("giatri");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regulationValue;
    }

    public void updateRegulationValue(int regulationId, int regulationValue) {
        String query = "UPDATE quydinh SET giatri = ? WHERE maqd = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, regulationValue);
            statement.setInt(2, regulationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
