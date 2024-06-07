package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Examination;
import com.example.privateclinic.Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExaminationDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public int addExamination(Examination examination)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter);
        String query = "INSERT INTO khambenh (manv, matn, ngay, benhchinh, benhphu, trieuchung, luuy) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING makb";
        int examId = -1;
        try (PreparedStatement statement =connectDB.databaseLink.prepareStatement(query)){

            int maBp = examination.getMaBenhPhu();
            statement.setInt(1,examination.getManv());
            statement.setInt(2,examination.getMatn());
            statement.setObject(3,localDateTime);
            statement.setInt(4,examination.getMaBenhChinh());
            if(maBp==0) statement.setNull(5, java.sql.Types.INTEGER);
            else statement.setInt(5,examination.getMaBenhPhu());
            statement.setString(6,examination.getTrieuChung());
            statement.setString(7,examination.getLuuy());
            return connectDB.getId(statement);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return examId;
    }
    public boolean UpdateReception(int matn,int makb)
    {
        String query ="UPDATE tiepnhan SET manv = ? WHERE matn = ?";
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            statement.setInt(1,makb);
            statement.setInt(2,matn);
            return connectDB.handleData(statement);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ObservableList<Patient> getPatientsFromReceptionByDate(Date date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT tn.stt, bn.hoten, bn.sdt ,bn.gioitinh, bn.ngaysinh, bn.diachi, tn.ngayvao, bn.mabn, tn.matn FROM benhnhan bn, tiepnhan tn WHERE bn.mabn = tn.mabn AND tn.ngayvao::date = ? AND tn.manv IS NULL ORDER BY tn.stt ASC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getInt("mabn"),
                            resultSet.getString("hoten"),
                            resultSet.getString("gioitinh"),
                            resultSet.getString("sdt"),
                            resultSet.getDate("ngaysinh"),
                            resultSet.getString("diachi"),
                            resultSet.getDate("ngayvao"),
                            resultSet.getInt("matn"),
                            resultSet.getInt("stt")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patients;
    }
    public ObservableList<Patient> getPatientsDoneByDate(Date date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT tn.stt, bn.hoten, bn.sdt ,bn.gioitinh, bn.ngaysinh, bn.diachi, tn.ngayvao, bn.mabn, tn.matn FROM benhnhan bn, tiepnhan tn WHERE bn.mabn = tn.mabn AND tn.ngayvao::date = ? AND tn.manv IS NOT NULL";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient(
                            resultSet.getInt("mabn"),
                            resultSet.getString("hoten"),
                            resultSet.getString("gioitinh"),
                            resultSet.getString("sdt"),
                            resultSet.getDate("ngaysinh"),
                            resultSet.getString("diachi"),
                            resultSet.getDate("ngayvao"),
                            resultSet.getInt("matn"),
                            resultSet.getInt("stt")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patients;
    }
    public String getValueRole(int id) throws SQLException {
        String query = "SELECT giatri FROM quydinh WHERE maqd = "+id+"";
        return connectDB.getData(query).getString("giatri");
    }

}
