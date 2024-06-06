package com.example.privateclinic.Models;

import java.time.LocalDateTime;
import java.util.Date;

public class Examination {
    int makb;
    int manv;
    int matn;
    int mabn;
    LocalDateTime ngay;
    int maBenhChinh;
    int maBenhPhu;
    String trieuchung;
    String luuy;

    String tenBenhChinh;
    String tenBenhPhu;
    String trieuChung;
    String tenNhanVien;
    public Examination() {
    }

    public Examination(int receptionId, int manv, int mabn, int maBenhChinh, int maBenhPhu, String trieuchung, String luuy) {
        this.matn = receptionId;
        this.manv = manv;
        this.mabn = mabn;
        this.maBenhChinh = maBenhChinh;
        this.maBenhPhu = maBenhPhu;
        this.trieuchung = trieuchung;
        this.luuy = luuy;
    }

    public Examination(int makb, int manv, int mabn, int maBenhChinh, int maBenhPhu, String trieuchung, String _luuy, String tenBenhChinh, String tenBenhPhu, String trieuChung, String luuY) {
        this.makb = makb;
        this.manv = manv;
        this.mabn = mabn;
        this.maBenhChinh = maBenhChinh;
        this.maBenhPhu = maBenhPhu;
        this.trieuchung = trieuchung;
        this.tenBenhChinh = tenBenhChinh;
        this.tenBenhPhu = tenBenhPhu;
        this.trieuChung = trieuChung;
        this.luuy = luuY;
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

    public LocalDateTime getNgay() {
        return ngay;
    }

    public void setNgay(LocalDateTime ngay) {
        this.ngay = ngay;
    }

    public int getMaBenhChinh() {
        return maBenhChinh;
    }

    public void setMaBenhChinh(int maBenhChinh) {
        this.maBenhChinh = maBenhChinh;
    }

    public int getMaBenhPhu() {
        return maBenhPhu;
    }

    public void setMaBenhPhu(int maBenhPhu) {
        this.maBenhPhu = maBenhPhu;
    }

    public String getTrieuchung() {
        return trieuchung;
    }

    public void setTrieuchung(String trieuchung) {
        this.trieuchung = trieuchung;
    }

    public String getLuuy() {
        return luuy;
    }

    public void setLuuy(String luuy) {
        this.luuy = luuy;
    }
    public String getTenBenhChinh() {
        return tenBenhChinh;
    }

    public void setTenBenhChinh(String tenBenhChinh) {
        this.tenBenhChinh = tenBenhChinh;
    }

    public String getTenBenhPhu() {
        return tenBenhPhu;
    }

    public void setTenBenhPhu(String tenBenhPhu) {
        this.tenBenhPhu = tenBenhPhu;
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
}
