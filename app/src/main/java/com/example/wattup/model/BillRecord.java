package com.example.wattup.model;

public class BillRecord {

    private int id;
    private String month;
    private double unitsUsed;
    private double totalCharges;
    private double rebatePercentage;
    private double finalCost;

    public BillRecord(String month, double unitsUsed, double totalCharges, double rebatePercentage, double finalCost) {
        this.month = month;
        this.unitsUsed = unitsUsed;
        this.totalCharges = totalCharges;
        this.rebatePercentage = rebatePercentage;
        this.finalCost = finalCost;
    }

    public BillRecord(int id, String month, double unitsUsed, double totalCharges, double rebatePercentage, double finalCost) {
        this.id = id;
        this.month = month;
        this.unitsUsed = unitsUsed;
        this.totalCharges = totalCharges;
        this.rebatePercentage = rebatePercentage;
        this.finalCost = finalCost;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMonth() { return month; }
    public double getUnitsUsed() { return unitsUsed; }
    public double getTotalCharges() { return totalCharges; }
    public double getRebatePercentage() { return rebatePercentage; }
    public double getFinalCost() { return finalCost; }
}