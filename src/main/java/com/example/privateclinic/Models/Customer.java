package com.example.privateclinic.Models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private int maBN;
    private String hoTen;
    private String gioiTinh;
    private Date ngaySinh;
    private String SDT;
    private String diaChi;
    private Date ngayVao;

    public Customer() {
    }

    public Customer(int maBN, String hoTen, String SDT) {
        this.maBN = maBN;
        this.hoTen = hoTen;
        this.SDT = SDT;
    }

    public Customer(int maBN, String hoTen, String gioiTinh, Date ngaySinh, String SDT, String diaChi, String ngayVao) {
        this.maBN = maBN;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.SDT = SDT;
        this.diaChi = diaChi;
        this.ngayVao = Date.valueOf(ngayVao);
    }

    public int getMaBN() {
        return maBN;
    }

    public void setMaBN(int maBN) {
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

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
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

    public Date getNgayVao() {
        return ngayVao;
    }

    public void setNgayVao(Date ngayVao) {
        this.ngayVao = ngayVao;
    }

}
