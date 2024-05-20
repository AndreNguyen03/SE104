package com.example.privateclinic.Models;


public class Medicine {
    private int medicineId;
    private String medicineName;
    private int medicineUnit;
    private int medicineQuantity;
    private double medicinePrice;
    private int medicineForm;
    private int medicineUse;

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", medicineUnit='" + medicineUnit + '\'' +
                ", medicinePrice='" + medicinePrice + '\'' +
                ", medicineForm=" + medicineForm +
                ", medicineUse='" + medicineUse + '\'' +
                ", medicineQuantity=" + medicineQuantity +
                '}';
    }

    public Medicine() {
    }

    public Medicine(int medicineId, String medicineName, int medicineUnit, double medicinePrice, int medicineForm, int medicineUse) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicineUnit = medicineUnit;
        this.medicinePrice = medicinePrice;
        this.medicineForm = medicineForm;
        this.medicineUse = medicineUse;
    }



    public int getMedicineId() { return medicineId; }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getMedicineUnit() {
        return medicineUnit;
    }

    public void setMedicineUnit(int medicineUnit) {
        this.medicineUnit = medicineUnit;
    }

    public int getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(int medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }

    public double getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(double medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public int getMedicineForm() {
        return medicineForm;
    }

    public void setMedicineForm(int medicineForm) { this.medicineForm = medicineForm;
    }

    public int getMedicineUse() {
        return medicineUse;
    }

    public void setMedicineUse(int medicineUse) {
        this.medicineUse = medicineUse;
    }

}
