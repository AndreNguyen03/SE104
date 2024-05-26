package com.example.privateclinic.Models;

import java.util.Date;

public class Examination {
    int makb;
    int manv;
    int mabn;
    java.sql.Date ngay;
    int maBenhChinh;
    int maBenhPhu;
    String trieuchung;
    String luuy;

    String tenBenhChinh;
    String tenBenhPhu;
    String trieuChung;

    public Examination() {
    }

    public Examination(int manv, int mabn, java.sql.Date ngay, int maBenhChinh, int maBenhPhu, String trieuchung, String luuy) {
        this.manv = manv;
        this.mabn = mabn;
        this.ngay = ngay;
        this.maBenhChinh = maBenhChinh;
        this.maBenhPhu = maBenhPhu;
        this.trieuchung = trieuchung;
        this.luuy = luuy;
    }

    public Examination(int makb, int manv, int mabn, java.sql.Date ngay, int maBenhChinh, int maBenhPhu, String trieuchung, String luuy, String tenBenhChinh, String tenBenhPhu, String trieuChung, String luuY) {
        this.makb = makb;
        this.manv = manv;
        this.mabn = mabn;
        this.ngay = ngay;
        this.maBenhChinh = maBenhChinh;
        this.maBenhPhu = maBenhPhu;
        this.trieuchung = trieuchung;
        this.luuy = luuy;
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

    public int getMabn() {
        return mabn;
    }

    public void setMabn(int mabn) {
        this.mabn = mabn;
    }

    public java.sql.Date getNgay() {
        return ngay;
    }

    public void setNgay(java.sql.Date ngay) {
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

}
