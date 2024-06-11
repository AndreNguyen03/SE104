package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.History;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public ObservableList<History> getAllHistory(int day,int month,int year) {
        ObservableList<History> historyList = FXCollections.observableArrayList();
        String query = "SELECT mals,lichsu.manv,hoten,ngay,noidung FROM lichsu,nhanvien WHERE nhanvien.manv = lichsu.manv AND EXTRACT(MONTH FROM lichsu.ngay) = ? AND EXTRACT(YEAR FROM lichsu.ngay) = ? ";
        if(day>0) query+=" AND EXTRACT(DAY FROM ngay) = ? ORDER BY ngay::time DESC ";
        else query+=" ORDER BY ngay DESC";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            statement.setInt(1,month);
            statement.setInt(2,year);
            if(day>0)  statement.setInt(3,day);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                History history = new History(
                        resultSet.getInt("mals"),
                        resultSet.getString("hoten")+"-"+resultSet.getString("manv"),
                        resultSet.getString("ngay"),
                        resultSet.getString("noidung")
                );
                historyList.add(history);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return historyList;
    }
}
