package com.example.privateclinic.Models;

public class Regulation {
    private int maqd;
    private String tenqd;

    private int giatri;

    public int getMaQD() { return maqd; }

    public void setMaQD(int maqd) {
        this.maqd = maqd;
    }

    public String getTenQD() {
        return tenqd;
    }

    public void setTenQD(String tenqd) {
        this.tenqd = tenqd;
    }

    public int getGiaTri() { return giatri; }

    public void setGiaTri(int giatri) {
        this.giatri = giatri;
    }

    public Regulation() {
    }


}
