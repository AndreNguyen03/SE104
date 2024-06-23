package com.example.privateclinic.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class Examination {
    int makb;
    int manv;
    int matn;
    int mabn;
    String ngay;
    Disease mainDisease;
    Disease subDisease;
    String luuy;
    String trieuChung;

    String tenNhanVien;
    int mahd;
    int tienkham;
    int tienthuoc;
    public Examination() {
    }

   public Examination(int receptionId, int manv,String employee_name, int mabn, Disease _mainDisease, Disease _subDisease, String trieuchung, String luuy) {
       this.matn = receptionId;
       this.manv = manv;
       this.tenNhanVien = employee_name;
       this.mabn = mabn;
       this.mainDisease = _mainDisease;
       this.subDisease = _subDisease;
       this.trieuChung = trieuchung;
       this.luuy = luuy;
   }

    public int getMakb() {
        return makb;
    }

    public void setMakb(int makb) {
        this.makb = makb;
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

    public Disease getMainDisease() {
        return mainDisease;
    }

    public void setMainDisease(Disease mainDisease) {
        this.mainDisease = mainDisease;
    }

    public Disease getSubDisease() {
        return subDisease;
    }

    public void setSubDisease(Disease subDisease) {
        this.subDisease = subDisease;
    }

    public String getLuuy() {
        return luuy;
    }

    public void setLuuy(String luuy) {
        this.luuy = luuy;
    }

    public String getTrieuChung() {
        return trieuChung;
    }

    public void setTrieuChung(String trieuChung) {
        this.trieuChung = trieuChung;
    }

    public int getMatn() {
        return matn;
    }

    public void setMatn(int matn) {
        this.matn = matn;
    }

    public int getMabn() {
        return mabn;
    }

    public void setMabn(int mabn) {
        this.mabn = mabn;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
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
}
