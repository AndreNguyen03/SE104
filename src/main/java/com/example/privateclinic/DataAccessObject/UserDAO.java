package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    User user = new User();

    public User getEmployee() {
        return user;
    }

    public void setEmployee(User _user) {
        this.user = _user;
    }


    public String GetHash(String plainText) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Chuyển đổi chuỗi thành mảng bytes và băm bằng MD5
            byte[] messageDigest = md.digest(plainText.getBytes());

            // Chuyển đổi mảng bytes thành chuỗi hex để hiển thị kết quả
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Thuật toán MD5 không được hỗ trợ.");
            e.printStackTrace();
            return null;
        }
    }
    public int CheckValidate(String username, String password) {
        password = GetHash(password);
        String query = "SELECT * FROM nhanvien WHERE username = ? AND (defaultpassword = ? OR password = ?)";
        try(PreparedStatement statement = connectDB.getPreparedStatement(query))
        {
            statement.setString(1,username);
            statement.setString(2,password);
            statement.setString(3,password);
            ResultSet resultSet = statement.executeQuery();
            //kiểm tra kq trả về
            if (resultSet.next()) {
                //tìm thấy người dùng có user và password khớp
                if (resultSet.getString("defaultpassword")!=null && resultSet.getString("defaultpassword").equals(password))
                    return 1;//mật khẩu mặc định
                else {
                    user.setEmployee_id(resultSet.getInt("manv"));
                    user.setEmployName(resultSet.getString("hoten"));
                    user.setPhoneNumber(resultSet.getString("sdt"));
                    user.setCitizen_id(resultSet.getString("cccd"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPosition(resultSet.getString("vitri"));
                    user.setAddress(resultSet.getString("diachi"));
                    user.setEmail(resultSet.getString("email"));
                    return 2; // mật khẩu chính
                }
            } else {
                return 0; // không tìm thấy
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            return -1;
        }
    }
    public String getEmail(String username) throws SQLException {
        String username_result =null;
        ConnectDB connect = new ConnectDB();
        String query = "SELECT email FROM nhanvien WHERE username = '" + username +"'";
        ResultSet resultSet = connect.getData(query);
        if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
        {
            username_result = resultSet.getString("email");
        }
        return username_result;
    }
    public boolean UpdatePassword(String username,String newPassword, int index) throws SQLException
    {
        newPassword = GetHash(newPassword);
        String querry;
        PreparedStatement preparedStatement = null;
        if (index == 0)
        {
            querry = "UPDATE nhanvien SET password = ?, defaultpassword = NULL  WHERE username = ? ";
            preparedStatement = connectDB.databaseLink.prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        else
        {
            querry = "UPDATE nhanvien SET password = ? WHERE username = ? ";
            preparedStatement = connectDB.databaseLink.prepareStatement(querry);
            preparedStatement.setString(1,newPassword);
            preparedStatement.setString(2,username);
        }
        if (connectDB.handleData(preparedStatement))
        {
            return true;
        }
        else
            return false;
    }
    final String LOWER_CASE = "abcdefghijklmnopqursuvwxyz";
    final String  UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String  NUMBERS = "123456789";
    final String  SPECIALS = "!@£$%^&*()#€";

    public String GeneratePassword(boolean useLowercase, boolean useUppercase, boolean useNumbers, boolean useSpecial, int passwordSize)
    {
        char[] _password = new char[passwordSize];
        String charSet = "";
        SecureRandom random = new SecureRandom();
        int counter= 0;
        if (useLowercase) charSet += LOWER_CASE;

        if (useUppercase) charSet += UPPER_CASE;
        if (useNumbers) charSet += NUMBERS;

        if (useSpecial) charSet += SPECIALS;

        for (counter = 0; counter < passwordSize; counter++) {
            _password[counter] = charSet.charAt(random.nextInt(charSet.length()));
        }

        return new String(_password);
    }

    public String getUsername(String _username)
    {
        String username =null;
        String query = "SELECT username FROM nhanvien WHERE username = '" + _username +"'";
        ResultSet resultSet = connectDB.getData(query);
        try
        {
            if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
            {
                username = resultSet.getString("username");
            }
            return username;
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
        return null;
    }
    public boolean checkID(int id)
    {
        String query = "SELECT * FROM nhanvien WHERE cccd = '" + id +"'";
        ResultSet resultSet = connectDB.getData(query);
        try
        {
            if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
            {
                return true;
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addEmployee(User employee) {
        String query = "INSERT INTO nhanvien (hoten, sdt, cccd, username, password, vitri, defaultpassword, diachi, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String defaultPassword = GeneratePassword(true,true,true,true,8);
        try (PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setString(1, employee.getEmployeeUsername());
            statement.setString(2, employee.getEmployeePhoneNumber());
            statement.setString(3, employee.getEmployeeCitizenId());
            statement.setString(4, employee.getEmployeeUsername());
            statement.setString(5, "");
            statement.setString(6, employee.getEmployeePosition());
            statement.setString(7, defaultPassword);
            statement.setString(8, employee.getEmployeeAddress());
            statement.setString(9, employee.getEmployeeEmail());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setEmployeeId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(User employee) {
        String query = "UPDATE nhanvien SET hoten = ?, sdt = ?, cccd = ?, diachi = ?, vitri = ?, email = ? WHERE manv = ?";
        try (PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setString(1, employee.getEmployeeName());
            statement.setString(2, employee.getEmployeePhoneNumber());
            statement.setString(3, employee.getEmployeeCitizenId());
            statement.setString(4, employee.getEmployeeAddress());
            statement.setString(5, employee.getEmployeePosition());
            statement.setString(6, employee.getEmployeeEmail());
            statement.setInt(7, employee.getEmployeeId());
            int aff =  statement.executeUpdate();
            if (aff > 0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM nhanvien WHERE manv = ?";
        try (PreparedStatement statement = connectDB.getPreparedStatement(query)) {
            statement.setInt(1, employeeId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected>0) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<User> getAllEmployees() {
        ObservableList<User> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM nhanvien ORDER BY manv ASC";
        try (PreparedStatement statement = connectDB.getPreparedStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User employee = new User();
                employee.setEmployeeId(resultSet.getInt("manv"));
                employee.setEmployeeName(resultSet.getString("hoten"));
                employee.setEmployeeCitizenId(resultSet.getString("cccd"));
                employee.setEmployeePhoneNumber(resultSet.getString("sdt"));
                employee.setEmployeeAddress(resultSet.getString("diachi"));
                employee.setEmployeePosition(resultSet.getString("vitri"));
                employee.setEmployeeUsername(resultSet.getString("username"));
                employee.setEmployeeEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    public ObservableList<User> seatchUser(String searchString) {
        ObservableList<User> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM nhanvien WHERE unaccent(hoten) ILIKE unaccent(?) ";
        boolean isInteger = false;
        try {
            int id = Integer.parseInt(searchString);
            query+= "AND manv = ? ";
            isInteger = true;
        } catch (NumberFormatException e) {
        }
        query += "ORDER BY manv ASC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1,"%"+searchString+"%");
            if(isInteger) statement.setInt(2,Integer.parseInt(searchString));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User employee = new User();
                employee.setEmployeeId(resultSet.getInt("manv"));
                employee.setEmployeeName(resultSet.getString("hoten"));
                employee.setEmployeeCitizenId(resultSet.getString("cccd"));
                employee.setEmployeePhoneNumber(resultSet.getString("sdt"));
                employee.setEmployeeAddress(resultSet.getString("diachi"));
                employee.setEmployeePosition(resultSet.getString("vitri"));
                employee.setEmployeeUsername(resultSet.getString("username"));
                employee.setEmployeeEmail(resultSet.getString("email"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
