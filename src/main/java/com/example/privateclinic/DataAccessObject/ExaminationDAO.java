package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Examination;
import com.example.privateclinic.Models.Prescribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ExaminationDAO {
    public int addExamination(Examination examination)
    {
        String query = "INSERT INTO khambenh (manv, mabn, ngay, benhchinh, benhphu, trieuchung, luuy) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING makb";
        ConnectDB connectDB = new ConnectDB();
        int examId = -1;
        try (PreparedStatement statement =connectDB.databaseLink.prepareStatement(query)){

            statement.setInt(1,examination.getManv());
            statement.setInt(2,examination.getMabn());
            statement.setDate(3,examination.getNgay());
            statement.setInt(4,examination.getMaBenhChinh());
            statement.setInt(5,examination.getMaBenhPhu());
            statement.setString(6,examination.getTrieuchung());
            statement.setString(7,examination.getLuuy());

            ResultSet resultSet = statement.executeQuery(); // Sử dụng executeQuery() thay vì executeUpdate()
            if (resultSet.next()) {
                examId = resultSet.getInt(1); // Lấy giá trị của makhambenh từ ResultSet
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return examId;
    }
    public  ObservableList<Examination> getPatientsByDate(int id, int year)
    {
        ObservableList<Examination> examinations = FXCollections.observableArrayList();
        String query = "SELECT ngay FROM khambenh WHERE EXTRACT(YEAR FROM ngay) = ? and mabn= ? ";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Examination examination = new Examination();
                    examination.setNgay(resultSet.getDate("ngay"));
                    examinations.add(examination);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }

}
