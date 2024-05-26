package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    ConnectDB connectDB = new ConnectDB();

    public EmployeeDAO() {
    }

    public void addEmployee(Employee employee) {
        String query = "INSERT INTO nhanvien (hoten, sdt, cccd, username, password, vitri, defaultpassword, diachi, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPhoneNumber());
            statement.setString(3, employee.getCitizenId());
            statement.setString(4, employee.getUsername());
            statement.setString(5, "");
            statement.setString(6, employee.getPosition());
            statement.setString(7, "123");
            statement.setString(8, employee.getAddress());
            statement.setString(9, employee.getEmail());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        String query = "UPDATE nhanvien SET hoten = ?, sdt = ?, cccd = ?, diachi = ?, vitri = ?, email = ? WHERE manv = ?";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getPhoneNumber());
            statement.setString(3, employee.getCitizenId());
            statement.setString(4, employee.getAddress());
            statement.setString(5, employee.getPosition());
            statement.setString(6, employee.getEmail());
            statement.setInt(7, employee.getId());
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

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM nhanvien";
        try (Connection connection = connectDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("manv"));
                employee.setName(resultSet.getString("hoten"));
                employee.setCitizenId(resultSet.getString("cccd"));
                employee.setPhoneNumber(resultSet.getString("sdt"));
                employee.setAddress(resultSet.getString("diachi"));
                employee.setPosition(resultSet.getString("vitri"));
                employee.setUsername(resultSet.getString("username"));
                employee.setEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
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
