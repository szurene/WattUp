package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private EditText editTextUnits;
    private RadioGroup radioGroupRebate;
    private Button buttonCalculate;
    private TextView textViewTotalCharges;
    private TextView textViewFinalCost;
    private BottomNavigationView bottomNavigationView;

    private final int RADIO_REBATE_0 = R.id.radio_rebate_0;
    private final int RADIO_REBATE_1 = R.id.radio_rebate_1;
    private final int RADIO_REBATE_2 = R.id.radio_rebate_2;
    private final int RADIO_REBATE_3 = R.id.radio_rebate_3;
    private final int RADIO_REBATE_4 = R.id.radio_rebate_4;
    private final int RADIO_REBATE_5 = R.id.radio_rebate_5;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        spinnerMonth = findViewById(R.id.spinner_month);
        editTextUnits = findViewById(R.id.edittext_units);
        radioGroupRebate = findViewById(R.id.radio_group_rebate);
        textViewTotalCharges = findViewById(R.id.textview_total_charges);
        textViewFinalCost = findViewById(R.id.textview_final_cost);
        buttonCalculate = findViewById(R.id.button_calculate);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupMonthSpinner();
        setupBottomNavigation();
        setupListeners();
    }

    private void setupMonthSpinner() {
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
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
            } else if (itemId == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        buttonCalculate.setOnClickListener(v -> performCalculationAndSave());
    }

    private double getSelectedRebatePercentage() {
        int selectedId = radioGroupRebate.getCheckedRadioButtonId();

        if (selectedId == RADIO_REBATE_0) return 0.0;
        if (selectedId == RADIO_REBATE_1) return 1.0;
        if (selectedId == RADIO_REBATE_2) return 2.0;
        if (selectedId == RADIO_REBATE_3) return 3.0;
        if (selectedId == RADIO_REBATE_4) return 4.0;
        if (selectedId == RADIO_REBATE_5) return 5.0;

        return 0.0;
    }

    private void performCalculationAndSave() {
        String unitsText = editTextUnits.getText().toString();

        if (unitsText.isEmpty()) {
            editTextUnits.setError("Units used is required.");
            Toast.makeText(this, "Please enter units used (kWh).", Toast.LENGTH_SHORT).show();
            return;
        }

        double unitsUsed;
        try {
            unitsUsed = Double.parseDouble(unitsText);
        } catch (NumberFormatException e) {
            editTextUnits.setError("Invalid number format.");
            return;
        }

        String month = spinnerMonth.getSelectedItem().toString();
        double rebatePercentage = getSelectedRebatePercentage();

        BillCalculator.CalculationResult result = BillCalculator.calculate(unitsUsed, rebatePercentage);

        textViewTotalCharges.setText(String.format(Locale.getDefault(), "Total Charges: RM %.2f", result.totalCharges));
        textViewFinalCost.setText(String.format(Locale.getDefault(), "Final Cost: RM %.2f (%.1f%% rebate)", result.finalCost, rebatePercentage));

        BillRecord newRecord = new BillRecord(
                month,
                unitsUsed,
                result.totalCharges,
                rebatePercentage,
                result.finalCost
        );

        long id = dbHelper.addBillRecord(newRecord);

        if (id != -1) {
            Toast.makeText(this, "Bill saved successfully (ID: " + id + ")", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save bill.", Toast.LENGTH_SHORT).show();
        }
    }
}