package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Employee;
import com.example.privateclinic.Models.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    ConnectDB connectDB = new ConnectDB();

    public UserDAO() {
    }

    public void addEmployee(User user) {
        String query = "INSERT INTO nhanvien (hoten, sdt, cccd, username, password, vitri, defaultpassword, diachi, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getEmployName());
            statement.setString(2, user.getPhoneNumber());
            statement.setString(3, user.getCitizen_id());
            statement.setString(4, user.getUsername());
            statement.setString(5, "");
            statement.setString(6, user.getPosition());
            statement.setString(7, "123");
            statement.setString(8, user.getAddress());
            statement.setString(9, user.getEmail());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setEmployee_id(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(User user) {
        String query = "UPDATE nhanvien SET hoten = ?, sdt = ?, cccd = ?, diachi = ?, vitri = ?, email = ? WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getEmployName());
            statement.setString(2, user.getPhoneNumber());
            statement.setString(3, user.getCitizen_id());
            statement.setString(4, user.getAddress());
            statement.setString(5, user.getPosition());
            statement.setString(6, user.getEmail());
            statement.setInt(7, user.getEmployee_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int employeeId) {
        String query = "DELETE FROM nhanvien WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllEmployees() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM nhanvien";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setEmployee_id(resultSet.getInt("manv"));
                user.setEmployName(resultSet.getString("hoten"));
                user.setCitizen_id(resultSet.getString("cccd"));
                user.setPhoneNumber(resultSet.getString("sdt"));
                user.setAddress(resultSet.getString("diachi"));
                user.setPosition(resultSet.getString("vitri"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;
        String query = "SELECT * FROM nhanvien WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getInt("manv"));
                employee.setName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public Employee getEmployeeByName(String employeeName) {
        Employee employee = null;
        String query = "SELECT * FROM nhanvien WHERE hoten = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employeeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getInt("manv"));
                employee.setName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

}
