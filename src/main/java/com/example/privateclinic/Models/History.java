package com.example.privateclinic.Models;

import java.sql.Date;

public class History {
    int mals;

    int manv;
    Date ngay;
    String noiDung;
    public History() {
    }

    public History(int manv,Date ngay, String noiDung) {
        this.manv = manv;
        this.ngay = ngay;
        this.noiDung = noiDung;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
