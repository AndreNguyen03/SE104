package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.History;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryDAO {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    ConnectDB connectDB = ConnectDB.getInstance();
    public boolean addHistory(History history)
    {
        LocalDateTime datetime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query = "INSERT INTO lichsu (manv,ngay,noidung) VALUES (?,?,?)";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            statement.setInt(1,history.getManv());
            statement.setObject(2,datetime);
            statement.setString(3,history.getNoiDung());
            return statement.executeUpdate() >0;
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }
}
