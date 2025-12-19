package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wattup.database.DatabaseHelper;
import com.example.wattup.logic.BillCalculator;
import com.example.wattup.model.BillRecord;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private EditText editTextUnits;
    private RadioGroup radioGroupRebate;
    private Button buttonCalculate, buttonSave;
    private TextView textViewTotalCharges;
    private TextView textViewFinalCost;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper dbHelper;
    private BillCalculator.CalculationResult currentResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        initializeViews();
        setupMonthSpinner();
        setupBottomNavigation();
        setupListeners();
    }

    private void initializeViews() {
        spinnerMonth = findViewById(R.id.spinner_month);
        editTextUnits = findViewById(R.id.edittext_units);
        radioGroupRebate = findViewById(R.id.radio_group_rebate);
        textViewTotalCharges = findViewById(R.id.textview_total_charges);
        textViewFinalCost = findViewById(R.id.textview_final_cost);
        buttonCalculate = findViewById(R.id.button_calculate);
        buttonSave = findViewById(R.id.button_save);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupListeners() {
        buttonCalculate.setOnClickListener(v -> performCalculation());
        buttonSave.setOnClickListener(v -> saveToDatabase());
    }

    private void performCalculation() {
        String unitsText = editTextUnits.getText().toString().trim();

        if (unitsText.isEmpty()) {
            editTextUnits.setError("Please enter kWh usage");
            return;
        }

        try {
            double unitsUsed = Double.parseDouble(unitsText);
            double rebatePercentage = getSelectedRebatePercentage();

            currentResult = BillCalculator.calculate(unitsUsed, rebatePercentage);

            textViewTotalCharges.setText(String.format(Locale.getDefault(), "Total Charges: RM %.2f", currentResult.totalCharges));
            textViewFinalCost.setText(String.format(Locale.getDefault(), "Final Cost: RM %.2f (%.1f%% rebate)", currentResult.finalCost, rebatePercentage));

            Toast.makeText(this, "Calculation Updated", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            editTextUnits.setError("Invalid number format");
        }
    }

    private void saveToDatabase() {
        if (currentResult == null) {
            Toast.makeText(this, "Please calculate the bill first!", Toast.LENGTH_LONG).show();
            return;
        }

        String unitsText = editTextUnits.getText().toString().trim();
        if (unitsText.isEmpty()) return;

        double unitsUsed = Double.parseDouble(unitsText);
        String month = spinnerMonth.getSelectedItem().toString();
        double rebatePercentage = getSelectedRebatePercentage();

        BillRecord newRecord = new BillRecord(
                month,
                unitsUsed,
                currentResult.totalCharges,
                rebatePercentage,
                currentResult.finalCost
        );

        long id = dbHelper.addBillRecord(newRecord);

        if (id != -1) {
            showSuccessDialog(month);
            clearForm();
        } else {
            Toast.makeText(this, "Error: Could not save to database", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        editTextUnits.setText("");
        editTextUnits.clearFocus();
        spinnerMonth.setSelection(0);
        radioGroupRebate.check(R.id.radio_rebate_0);

        textViewTotalCharges.setText("Total Charges: RM 0.00");
        textViewFinalCost.setText("Final Cost: RM 0.00");

        currentResult = null;
    }

    private void showSuccessDialog(String month) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Bill Saved Successfully")
                .setMessage("Your estimated bill for " + month + " has been added to your history.")
                .setPositiveButton("View History", (dialog, which) -> {
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                })
                .setNegativeButton("Dismiss", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private double getSelectedRebatePercentage() {
        int selectedId = radioGroupRebate.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_rebate_1) return 1.0;
        if (selectedId == R.id.radio_rebate_2) return 2.0;
        if (selectedId == R.id.radio_rebate_3) return 3.0;
        if (selectedId == R.id.radio_rebate_4) return 4.0;
        if (selectedId == R.id.radio_rebate_5) return 5.0;

        return 0.0;
    }

    private void setupMonthSpinner() {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                return true;
            } else if (itemId == R.id.nav_about) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            }
            return itemId == R.id.nav_home;
        });
    }
}