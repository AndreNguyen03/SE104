package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Patient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    private int lastMaxId = 0;

    public PatientDAO() {

    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void addPatient(Patient patient) {
        LocalDate now = LocalDate.now();

        String query = "INSERT INTO benhnhan (MaBN, HoTen, GioiTinh, NgaySinh, SDT, DiaChi) VALUES (?, ?, ?, ?, ?, ?)";

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

public ObservableList<Patient> getPatientsFromReceptionByDate(Date date) {
    ObservableList<Patient> patients = FXCollections.observableArrayList();
    String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, tn.ngayvao, tn.stt, tn.matn FROM benhnhan bn, tiepnhan tn WHERE bn.mabn = tn.mabn AND tn.ngayvao::date = ? AND tn.manv IS NULL";
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
        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, tn.ngayvao " +
                "FROM benhnhan bn,khambenh kb, tiepnhan tn " +
                "WHERE tn.ngayvao = ? AND bn.mabn = kb.mabn AND tn.mabn = bn.mabn";
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

//    public ObservableList<Patient> getPatientsByDateReception(Date date) {
//        ObservableList<Patient> patients = FXCollections.observableArrayList();
//        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, tn.ngayvao FROM benhnhan bn, tiepnhan tn WHERE DATE(ngayvao) = ? AND tn.mabn = bn.mabn";
//        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
//            statement.setDate(1, date);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    Patient patient = new Patient();
//                    patient.setPatientId(resultSet.getInt("mabn"));
//                    patient.setPatientName(resultSet.getString("hoten"));
//                    patient.setPatientGender(resultSet.getString("gioitinh"));
//                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
//                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
//                    patient.setPatientAddress(resultSet.getString("diachi"));
//                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
//                    patients.add(patient);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return patients/**/;
//    }

    public void admitPatient(int mabn, int stt) {
        String checkSql = "SELECT COUNT(*) AS so_lan_kham FROM tiepnhan WHERE mabn = ?";
        String insertSql = "INSERT INTO tiepnhan (mabn, stt,ngayvao , lankham) VALUES (?, ?, ?, ?)";

        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);

        try (PreparedStatement checkStmt = connectDB.databaseLink.prepareStatement(checkSql);
             PreparedStatement insertStmt = connectDB.databaseLink.prepareStatement(insertSql)) {

            // Check the current count of visits for the given patient
            checkStmt.setInt(1, mabn);
            ResultSet rs = checkStmt.executeQuery();
            int soLanKham = 0;
            if (rs.next()) {
                soLanKham = rs.getInt("so_lan_kham");
            }

            // Prepare the insert statement
            insertStmt.setInt(1, mabn);
            insertStmt.setInt(2, stt);
            insertStmt.setObject(3, dateTime);
            insertStmt.setInt(4, soLanKham + 1);

            // Execute the insert statement
            insertStmt.executeUpdate();
            System.out.println("Patient admitted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getDoctor(int maTiepNhan) {
        String doctor = null;
        String query = "SELECT hoten FROM khambenh, nhanvien WHERE matn = ? and khambenh.manv = nhanvien.manv";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, maTiepNhan);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return null;
                } else {
                    while (resultSet.next()) {
                        doctor = resultSet.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    public int getReceptionId(int mabn) {
        int id = 0;

        String query = "Select matn FROM tiepnhan WHERE tiepnhan.mabn = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, mabn);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }


    public ObservableList<Patient> getPatientsByDateReception(Date date) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, tn.ngayvao, nv.hoten\n" +
                "FROM benhnhan bn\n" +
                "JOIN tiepnhan tn ON tn.mabn = bn.mabn\n" +
                "LEFT JOIN khambenh kb ON tn.matn = kb.matn\n" +
                "LEFT JOIN nhanvien nv ON kb.manv = nv.manv\n" +
                "WHERE DATE(tn.ngayvao) = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient record = new Patient();
                    record.setPatientId(resultSet.getInt(1));
                    record.setPatientName(resultSet.getString(2));
                    record.setPatientGender(resultSet.getString(3));
                    record.setPatientBirth(resultSet.getDate(4));
                    record.setPatientPhoneNumber(resultSet.getString(5));
                    record.setPatientAddress(resultSet.getString(6));
                    record.setArrivalDate(resultSet.getObject(7));

                    // Kiểm tra cột thứ 8 có tồn tại không trước khi truy cập
                    try {
                        record.setDoctor(resultSet.getString(8));
                    } catch (SQLException e) {
                        record.setDoctor(" ");
                    }
                    patients.add(record);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public void checkForNewRecords(TableView<Patient> tvPatientDetails, LocalDate value) {
        String query = "SELECT makb FROM khambenh WHERE makb > " + lastMaxId + " ORDER BY makb ASC";
        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            boolean hasNewRecords = false;

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                lastMaxId = id; // Cập nhật ID cuối cùng đã kiểm tra
                hasNewRecords = true;
            }

            if (hasNewRecords) {
                Platform.runLater(() -> updateTableViewWithPatients(tvPatientDetails, value));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTableViewWithPatients(TableView<Patient> tableView, LocalDate value) {
        ObservableList<Patient> patients = getPatientsByDateReception(Date.valueOf(value));
        tableView.setItems(patients);
    }
}
