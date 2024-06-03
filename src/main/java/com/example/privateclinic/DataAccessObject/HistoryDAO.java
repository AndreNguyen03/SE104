package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.History;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoryDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    public void addHistory(History history)
    {
        String query = "INSERT INTO lichsu (manv,ngay,noidung) VALUES (?,?,?)";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            statement.setInt(1,history.getManv());
            statement.setDate(2,history.getNgay());
            statement.setString(3,history.getNoiDung());
            statement.executeUpdate();
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
    }
}
