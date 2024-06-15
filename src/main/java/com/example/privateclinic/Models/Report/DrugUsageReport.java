package com.example.privateclinic.Models.Report;

public class DrugUsageReport {
    private String drugName;
    private String unit;
    private int quantity;
    private int usageCount;

    public DrugUsageReport() {
    }

    public DrugUsageReport(String drugName, String unit, int quantity, int usageCount) {
        this.drugName = drugName;
        this.unit = unit;
        this.quantity = quantity;
        this.usageCount = usageCount;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
}
