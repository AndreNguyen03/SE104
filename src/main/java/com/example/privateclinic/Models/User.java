package com.example.privateclinic.Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.StringTemplate.STR;

public class User {
    private String Username ;
    public User()
    {}
    private String Password ;
    private int Employee_id;
    private String EmployName;
    private String Citizen_id;
    private String Email;
    private String PhoneNumber;
    private String Address;
    private String Position;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }



    public String getCitizen_id() {
        return Citizen_id;
    }

    public void setCitizen_id(String citizen_id) {
        Citizen_id = citizen_id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public int getEmployee_id() {
        return Employee_id;
    }

    public void setEmployee_id(int employee_id) {
        Employee_id = employee_id;
    }

    public String getEmployName() {
        return EmployName;
    }

    public void setEmployName(String employName) {
        EmployName = employName;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + Employee_id + '\'' +
                ", employeeName='" + EmployName + '\'' +
                ", employeeCitizenId='" + Citizen_id + '\'' +
                ", employeeAddress='" + Address + '\'' +
                ", employeePhoneNumber=" + PhoneNumber +
                ", employeeEmail='" + Email + '\'' +
                ", employeePosition=" + Position + '\'' +
                ", employeeUsername=" + Username +
                '}';
    }

    public User(int employeeId, String employeeName, String employeeCitizenId, String employeeAddress, String employeePhoneNumber, String employeeEmail, String employeePosition, String employeeUsername) {
        this.Employee_id = employeeId;
        this.EmployName = employeeName;
        this.Citizen_id = employeeCitizenId;
        this.Address = employeeAddress;
        this.PhoneNumber = employeePhoneNumber;
        this.Email = employeeEmail;
        this.Position = employeePosition;
        this.Username = employeeUsername;
    }

    public User(String employeeName, String employeeCitizenId, String employeeAddress, String employeePhoneNumber, String employeeEmail, String employeePosition, String employeeUsername) {
        this.EmployName = employeeName;
        this.Citizen_id = employeeCitizenId;
        this.Address = employeeAddress;
        this.PhoneNumber = employeePhoneNumber;
        this.Email = employeeEmail;
        this.Position = employeePosition;
        this.Username = employeeUsername;
    }

    public int getEmployeeId() {
        return Employee_id;
    }

    public void setEmployeeId(int employeeId) {
        this.Employee_id = employeeId;
    }

    public String getEmployeeName() {
        return EmployName;
    }

    public void setEmployeeName(String employeeName) {
        this.EmployName = employeeName;
    }

    public String getEmployeeCitizenId() {
        return Citizen_id;
    }

    public void setEmployeeCitizenId(String employeeCitizenId) {
        this.Citizen_id = employeeCitizenId;
    }

    public String getEmployeeAddress() {
        return Address;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.Address = employeeAddress;
    }

    public String getEmployeePhoneNumber() {
        return PhoneNumber;
    }

    public void setEmployeePhoneNumber(String employeePhoneNumber) {
        this.PhoneNumber = employeePhoneNumber;
    }

    public String getEmployeeEmail() {
        return Email;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.Email = employeeEmail;
    }

    public String getEmployeePosition() {
        return Position;
    }

    public void setEmployeePosition(String employeePosition) {
        this.Position = employeePosition;
    }

    public String getEmployeeUsername() {
        return Username;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.Username = employeeUsername;
    }

    public String getEmployeePassword() {
        return Password;
    }

    public void setEmployeePassword(String employeePassword) {
        this.Password = employeePassword;
    }

    public String getDefaultpassword() {
        return Password;
    }

    public void setDefaultpassword(String defaultpassword) {
        this.Password = defaultpassword;
    }
}



    /*public String UserID(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employee_id FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employee_id");
    }
    public String UserName(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT employname FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("employname");
    }
    public String getUsername(String _username) throws SQLException
    {
        String username =null;
        ConnectDB connect = new ConnectDB();
        String query = "SELECT username FROM employee WHERE username = '" + _username +"'";
        ResultSet resultSet = connect.getData(query);
        if(resultSet.next()) // kiểm tra xem resultSet có dữ liệu hay không
        {
            username = resultSet.getString("username");
        }
        return username;
    }*/
   /* public String getPosition(String username,String password) throws SQLException
    {
            ConnectDB connect = new ConnectDB();
            String query = "SELECT position FROM employee WHERE username = '" + username + "' AND password = '" + password + "'";
            return connect.getData(query).getString("position");
    }
    // Forgot password
    */
    /*public ResultSet LoadListEmployee () {
        ConnectDB connect = new ConnectDB();
        String query = "SELECT manv as \"ID\", HoTen as \"Họ và tên\",cccd as \"CCCD\",diachi as \"Địa chỉ\",sdt as \"Số điện thoại\",email as \"Email\",vitri as \"Vị trí làm việc\",username as \"Username\"  FROM nhanvien ";
        return connect.getData(query);
    }




    public  boolean DeleteEmployee(String id) throws SQLException
    {
            ConnectDB connectDB = new ConnectDB();
            String query = "DELETE employee WHERE employee_id =?";
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,id);

            if(connectDB.handleData(preparedStatement))
            {
                return  true;
            }
            return false;
    }

    public ResultSet SearchData(String search) //search id or search name
    {
        ConnectDB connect = new ConnectDB();
        String query = STR."SELECT MaNV as \"ID\", HoTen as \"Họ và tên\",CCCD as \"CCCD\",DiaChi as \"Địa chỉ\",SDT as \"Số điện thoại\",Email as \"Email\",ViTri as \"Vị trí công việc\",TK as \"Tài khoản\"  FROM NHANVIEN WHERE (MaNV like '\{search}%' or HoTen like N'% \{search}%')";
        return connect.getData(query);
    }*/

