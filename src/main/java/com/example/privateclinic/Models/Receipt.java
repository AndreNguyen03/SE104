package com.example.privateclinic.Models;

public class Receipt {
    int mahd;
    int tienkham;
    int tienthuoc;


    int makhambenh;
    int sothuTu;
    int maThuoc;
    int maKeThuoc;
    int ngay;
    int sang;
    int trua;
    int chieu;
    int toi;
    int soLuong;

    double donGia;
    double thanhTien;

    String tenThuoc;

    String tenDonViTinh;

    String tenCachDung;
    String tenDangThuoc;

    String note;
    public Receipt()
    {

    }

    public Receipt(int maThuoc, String tenThuoc, String tenDonViTinh, String tenDangThuoc, String tenCachDung, double donGia,
                   int ngay, int sang, int trua, int chieu, int toi, int soLuong) {
        this.maThuoc = maThuoc;
        //this.maKeThuoc = maKeThuoc;
        this.ngay = ngay;
        this.sang = sang;
        this.trua = trua;
        this.chieu = chieu;
        this.toi = toi;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong*donGia;

        this.tenThuoc = tenThuoc;
        this.tenCachDung = tenCachDung;
        this.tenDangThuoc= tenDangThuoc;
        this.tenDonViTinh= tenDonViTinh;
    }

    public int getMakhambenh() {
        return makhambenh;
    }

    public void setMakhambenh(int makhambenh) {
        this.makhambenh = makhambenh;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

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
    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public int getMaKeThuoc() {
        return maKeThuoc;
    }

    public void setMaKeThuoc(int maKeThuoc) {
        this.maKeThuoc = maKeThuoc;
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
    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    public int getSothuTu() {
        return sothuTu;
    }

    public void setSothuTu(int sothuTu) {
        this.sothuTu = sothuTu;
    }

    public int getMahd() {
        return mahd;
    }

    public void setMahd(int mahd) {
        this.mahd = mahd;
    }

    public int getTienkham() {
        return tienkham;
    }

    public void setTienkham(int tienkham) {
        this.tienkham = tienkham;
    }

    public int getTienthuoc() {
        return tienthuoc;
    }

    public void setTienthuoc(int tienthuoc) {
        this.tienthuoc = tienthuoc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
