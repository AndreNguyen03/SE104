package com.example.privateclinic.Models;

public class Medicine {
    int maThuoc;
    String tenThuoc;
    int maDonViTinh;
    int maCachDung;
    int maDangThuoc;
    int soLuong;
    double giaBan;


    String tenDonViTinh;

    String tenCachDung;
    String tenDangThuoc;
    public String getTenDonViTinh() {
        return tenDonViTinh;
    }

    public void setTenDonViTinh(String tenDonViTinh) {
        this.tenDonViTinh = tenDonViTinh;
    }

    public String getTenCachDung() {
        return tenCachDung;
    }

    public void setTenCachDung(String tenCachDung) {
        this.tenCachDung = tenCachDung;
    }

    public String getTenDangThuoc() {
        return tenDangThuoc;
    }

    public void setTenDangThuoc(String tenDangThuoc) {
        this.tenDangThuoc = tenDangThuoc;
    }



    public Medicine() {
    }

    public Medicine(int maThuoc, String tenThuoc, int maDonViTinh, int maCachDung, int maDangThuoc, int soLuong, double giaBan) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.maDonViTinh = maDonViTinh;
        this.maCachDung = maCachDung;
        this.maDangThuoc = maDangThuoc;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public int getMaDonViTinh() {
        return maDonViTinh;
    }

    public void setMaDonViTinh(int maDonViTinh) {
        this.maDonViTinh = maDonViTinh;
    }

    public int getMaCachDung() {
        return maCachDung;
    }

    public void setMaCachDung(int maCachDung) {
        this.maCachDung = maCachDung;
    }

    public int getMaDangThuoc() {
        return maDangThuoc;
    }

    public void setMaDangThuoc(int maDangThuoc) {
        this.maDangThuoc = maDangThuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }
}
