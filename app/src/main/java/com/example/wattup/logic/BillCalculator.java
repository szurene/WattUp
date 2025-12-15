package com.example.wattup.logic;

public class BillCalculator {

    public static class CalculationResult {
        public double totalCharges;
        public double finalCost;

        public CalculationResult(double totalCharges, double finalCost) {
            this.totalCharges = totalCharges;
            this.finalCost = finalCost;
        }
    }

    public static CalculationResult calculate(double unitsUsed, double rebatePercentage) {

        double remainingUnits = unitsUsed;
        double totalCharges = 0.0;

        double block1Limit = 200.0;
        double block1Rate = 0.218;

        double block2Limit = 100.0;
        double block2Rate = 0.334;

        double block3Limit = 300.0;
        double block3Rate = 0.516;

        double block4Rate = 0.546;

        double chargeableUnits;

        chargeableUnits = Math.min(remainingUnits, block1Limit);
        totalCharges += chargeableUnits * block1Rate;
        remainingUnits -= chargeableUnits;

        if (remainingUnits > 0) {
            chargeableUnits = Math.min(remainingUnits, block2Limit);
            totalCharges += chargeableUnits * block2Rate;
            remainingUnits -= chargeableUnits;
        }

        if (remainingUnits > 0) {
            chargeableUnits = Math.min(remainingUnits, block3Limit);
            totalCharges += chargeableUnits * block3Rate;
            remainingUnits -= chargeableUnits;
        }

        if (remainingUnits > 0) {
            totalCharges += remainingUnits * block4Rate;
        }

        double rebateAmount = totalCharges * (rebatePercentage / 100.0);
        double finalCost = totalCharges - rebateAmount;

        return new CalculationResult(totalCharges, finalCost);
    }
}