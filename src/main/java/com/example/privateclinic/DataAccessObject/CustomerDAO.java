package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Examination;
import com.example.privateclinic.Models.Prescribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerDAO {

    /*public ObservableList<Customer> getPatientsByDate(Date date) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, bn.ngayvao FROM benhnhan bn WHERE ngayvao = ? " +
                "AND bn.mabn NOT IN (SELECT mabn FROM khambenh)";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setMaBN(resultSet.getInt("mabn"));
                    customer.setHoTen(resultSet.getString("hoten"));
                    customer.setGioiTinh(resultSet.getString("gioitinh"));
                    customer.setNgaySinh(resultSet.getDate("ngaysinh"));
                    customer.setSDT(resultSet.getString("sdt"));
                    customer.setDiaChi(resultSet.getString("diachi"));
                    customer.setNgayVao(resultSet.getDate("ngayvao"));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    public ObservableList<Customer> getPatientsDoneByDate(Date date) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT bn.mabn, bn.hoten, bn.gioitinh, bn.ngaysinh, bn.sdt, bn.diachi, bn.ngayvao " +
                "FROM benhnhan bn,khambenh kb " +
                "WHERE bn.ngayvao = ? AND bn.mabn = kb.mabn";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setMaBN(resultSet.getInt("mabn"));
                    customer.setHoTen(resultSet.getString("hoten"));
                    customer.setGioiTinh(resultSet.getString("gioitinh"));
                    customer.setNgaySinh(resultSet.getDate("ngaysinh"));
                    customer.setSDT(resultSet.getString("sdt"));
                    customer.setDiaChi(resultSet.getString("diachi"));
                    customer.setNgayVao(resultSet.getDate("ngayvao"));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }*/
    /*public  ObservableList<Examination> getPatientsByDate(String id, String year)
    {
        ObservableList<Examination> examinations = FXCollections.observableArrayList();
        String query = "SELECT ngay FROM khambenh WHERE EXTRACT(YEAR FROM ngay) = ? and mabn= ? ";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.getConnection().prepareStatement(query)) {
            statement.setString(1, year);
            statement.setString(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Examination examination = new Examination();
                    examination.setNgay(resultSet.getString("ngay"));
                    examinations.add(examination);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prescribes;
    }*/
}
