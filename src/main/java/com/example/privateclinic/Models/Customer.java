package com.example.privateclinic.Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private String maBN;
    private String hoTen;
    private String gioiTinh;
    private String ngaySinh;
    private String SDT;
    private String diaChi;
    private String ngayVao;

    public Customer() {
    }

    public Customer(String maBN, String hoTen, String SDT) {
        this.maBN = maBN;
        this.hoTen = hoTen;
        this.SDT = SDT;
    }

    public Customer(String maBN, String hoTen, String gioiTinh, String ngaySinh, String SDT, String diaChi, String ngayVao) {
        this.maBN = maBN;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.SDT = SDT;
        this.diaChi = diaChi;
        this.ngayVao = ngayVao;
    }

    public String getMaBN() {
        return maBN;
    }

    public void setMaBN(String maBN) {
        this.maBN = maBN;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgayVao() {
        return ngayVao;
    }

    public void setNgayVao(String ngayVao) {
        this.ngayVao = ngayVao;
    }

    public ResultSet LoadListCustomers()
    {
        ConnectDB connect = new ConnectDB();
        String query = "SELECT mabn, hoten, gioitinh, ngaysinh, sdt, diachi, ngayvao FROM benhnhan";
        return connect.getData(query);
    }
    public ResultSet LoadCustomer(String id)
    {
        ConnectDB connectDB = new ConnectDB();
        String querry = "SELECT mabn, hoten, gioitinh, ngaysinh, sdt, diachi, ngayvao  FROM benhnhan WHERE mabn = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connectDB.getConnection().prepareStatement(querry);
            preparedStatement.setInt(1,Integer.parseInt(id));
            return preparedStatement.executeQuery();
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            return null;
        }
    }
    public ResultSet Search(String search) throws SQLException
    {
        ConnectDB connect = new ConnectDB();
        PreparedStatement preparedStatement = null;
        String query = "SELECT mabn, hoten, gioitinh, ngaysinh, sdt, diachi, ngayvao " +
                "FROM NHANVIEN WHERE (MaNV like '?%' or HoTen like N'% ?%'";
        preparedStatement = connect.getConnection().prepareStatement(query);
        preparedStatement.setString(1,search);
        preparedStatement.setString(2,search);
        return connect.getData(preparedStatement.toString());
    }

}
