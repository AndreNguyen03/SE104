package com.example.privateclinic.Models;

public class Prescribe {
    int maThuoc;
    int maKhamBenh;
    int ngay;
    int sang;
    int trua;
    int chieu;
    int toi;
    int soLuong;
    public Prescribe()
    {

    }
    public Prescribe(int maThuoc, int maKhamBenh, int ngay, int sang, int trua, int chieu, int toi, int soLuong) {
        this.maThuoc = maThuoc;
        this.maKhamBenh = maKhamBenh;
        this.ngay = ngay;
        this.sang = sang;
        this.trua = trua;
        this.chieu = chieu;
        this.toi = toi;
        this.soLuong = soLuong;
    }

    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public int getMaKhamBenh() {
        return maKhamBenh;
    }

    public void setMaKhamBenh(int maKhamBenh) {
        this.maKhamBenh = maKhamBenh;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public int getSang() {
        return sang;
    }

    public void setSang(int sang) {
        this.sang = sang;
    }

    public int getTrua() {
        return trua;
    }

    public void setTrua(int trua) {
        this.trua = trua;
    }

    public int getChieu() {
        return chieu;
    }

    public void setChieu(int chieu) {
        this.chieu = chieu;
    }

    public int getToi() {
        return toi;
    }

    public void setToi(int toi) {
        this.toi = toi;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
