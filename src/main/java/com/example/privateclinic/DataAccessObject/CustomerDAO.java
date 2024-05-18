package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerDAO {

    public ObservableList<Customer> getPatientsByDate(Date date) {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT mabn, hoten, gioitinh, ngaysinh, sdt, diachi, ngayvao FROM benhnhan WHERE ngayvao = ?";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.getConnection().prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setMaBN(resultSet.getString("mabn"));
                    customer.setHoTen(resultSet.getString("hoten"));
                    customer.setGioiTinh(resultSet.getString("gioitinh"));
                    customer.setNgaySinh(resultSet.getString("ngaysinh"));
                    customer.setSDT(resultSet.getString("sdt"));
                    customer.setDiaChi(resultSet.getString("diachi"));
                    customer.setNgayVao(resultSet.getString("ngayvao"));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    public  ObservableList<Customer> getPatientsByDate(String id, String year)
    {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT mabn, hoten, gioitinh, ngaysinh, sdt, diachi, ngayvao FROM benhnhan WHERE Year(ngayvao)=?";
        ConnectDB connectDB = new ConnectDB();
        try (PreparedStatement statement = connectDB.getConnection().prepareStatement(query)) {
            statement.setString(1, year);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setMaBN(resultSet.getString("mabn"));
                    customer.setHoTen(resultSet.getString("hoten"));
                    customer.setGioiTinh(resultSet.getString("gioitinh"));
                    customer.setNgaySinh(resultSet.getString("ngaysinh"));
                    customer.setSDT(resultSet.getString("sdt"));
                    customer.setDiaChi(resultSet.getString("diachi"));
                    customer.setNgayVao(resultSet.getString("ngayvao"));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

}
