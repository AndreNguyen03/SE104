package com.example.privateclinic.Models;

public class Disease {
    int maBenh;
    String tenBenh;
    String maICD;


    public Disease() {
    }

    public Disease(int maBenh, String tenBenh, String maICD ) {
        this.maBenh = maBenh;
        this.tenBenh = tenBenh;
        this.maICD = maICD;
    }

    public int getMaBenh() {
        return maBenh;
    }

    public void setMaBenh(int maBenh) {
        this.maBenh = maBenh;
    }

    public String getTenBenh() {
        return tenBenh;
    }

    public void setTenBenh(String tenbenh) {
        this.tenBenh = tenbenh;
    }

    public String getMaICD() {
        return maICD;
    }

    public void setMaICD(String maICD) {
        this.maICD = maICD;
    }
}
