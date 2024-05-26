package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    ConnectDB connectDB = new ConnectDB();

    public PatientDAO() {

    }

    public void addPatient(Patient patient) {
        LocalDate now = LocalDate.now();

        String query = "INSERT INTO benhnhan (MaBN, HoTen, GioiTinh, NgaySinh, SDT, DiaChi, ngayvao) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, patient.getPatientId());
            statement.setString(2, patient.getPatientName());
            statement.setString(3, patient.getPatientGender());
            statement.setDate(4, patient.getPatientBirth());
            statement.setString(5, patient.getPatientPhoneNumber());
            statement.setString(6, patient.getPatientAddress());
            statement.setDate(7, Date.valueOf(now));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM benhnhan";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setPatientId(resultSet.getInt("MaBN"));
                patient.setPatientName(resultSet.getString("HoTen"));
                patient.setPatientGender(resultSet.getString("GioiTinh"));
                patient.setPatientBirth(resultSet.getDate("NgaySinh"));
                patient.setPatientPhoneNumber(resultSet.getString("SDT"));
                patient.setPatientAddress(resultSet.getString("DiaChi"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void updatePatient(Patient patient) {
        String query = "UPDATE benhnhan SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, SDT = ?, DiaChi = ? WHERE MaBN = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setString(1, patient.getPatientName());
            statement.setString(2, patient.getPatientGender());
            statement.setDate(3, patient.getPatientBirth());
            statement.setString(4, patient.getPatientPhoneNumber());
            statement.setInt(6, patient.getPatientId());
            statement.setString(5, patient.getPatientAddress());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int patientId) {
        String query = "DELETE FROM benhnhan WHERE MaBN = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, patientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Patient getPatientById(int id) {
        Patient patient = null;
        String query = "SELECT * FROM benhnhan WHERE MaBN = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                patient = new Patient();
                patient.setPatientId(resultSet.getInt("MaBN"));
                patient.setPatientName(resultSet.getString("HoTen"));
                patient.setPatientGender(resultSet.getString("GioiTinh"));
                patient.setPatientBirth(resultSet.getDate("NgaySinh"));
                patient.setPatientPhoneNumber(resultSet.getString("SDT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public Patient getPatientByName(String name) {
        Patient patient = null;
        String query = "SELECT * FROM benhnhan WHERE HoTen = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                patient = new Patient();
                patient.setPatientId(resultSet.getInt("MaBN"));
                patient.setPatientName(resultSet.getString("HoTen"));
                patient.setPatientGender(resultSet.getString("GioiTinh"));
                patient.setPatientBirth(resultSet.getDate("NgaySinh"));
                patient.setPatientPhoneNumber(resultSet.getString("SDT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    public int getNextPatientId() {
        String query = "SELECT MAX(mabn) FROM benhnhan";

        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                return maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 1; // Trường hợp không có bệnh nhân nào trong cơ sở dữ liệu
    }


    public ObservableList<Patient> getPatientsByDate(Date date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, bn.ngayvao FROM benhnhan bn WHERE ngayvao = ? " +
                "AND bn.mabn NOT IN (SELECT mabn FROM khambenh)";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getInt("mabn"));
                    patient.setPatientName(resultSet.getString("hoten"));
                    patient.setPatientGender(resultSet.getString("gioitinh"));
                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
                    patient.setPatientAddress(resultSet.getString("diachi"));
                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patients/**/;
    }
    public ObservableList<Patient> getPatientsDoneByDate(Date date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, bn.ngayvao " +
                "FROM benhnhan bn,khambenh kb " +
                "WHERE bn.ngayvao = ? AND bn.mabn = kb.mabn";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getInt("mabn"));
                    patient.setPatientName(resultSet.getString("hoten"));
                    patient.setPatientGender(resultSet.getString("gioitinh"));
                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
                    patient.setPatientAddress(resultSet.getString("diachi"));
                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patients;
    }
}
