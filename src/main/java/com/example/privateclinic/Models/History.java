package com.example.privateclinic.Models;

import java.sql.Date;

public class History {
    int mals;
    int manv;
    String ngay;
    String noiDung;
    String tenNhanVien;

    public History() {
    }

    public History(int manv, String noiDung) {
        this.manv = manv;
        this.noiDung = noiDung;
    }

    public History(int mals, String tenNhanVien, String ngay, String noiDung) {
        this.mals = mals;
        this.tenNhanVien = tenNhanVien;
        this.ngay = ngay;
        this.noiDung = noiDung;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }
}
