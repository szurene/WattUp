package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wattup.database.DatabaseHelper;
import com.example.wattup.logic.BillCalculator;
import com.example.wattup.model.BillRecord;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvMonth, tvUnit, tvTotalCharges, tvRebate, tvFinalCost;
    private Button btnEdit, btnDelete;
    private DatabaseHelper dbHelper;
    private BillRecord currentRecord;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this);

        tvMonth = findViewById(R.id.tv_detail_month);
        tvUnit = findViewById(R.id.tv_detail_unit);
        tvTotalCharges = findViewById(R.id.tv_detail_total_charges);
        tvRebate = findViewById(R.id.tv_detail_rebate);
        tvFinalCost = findViewById(R.id.tv_detail_final_cost);

        // Initialize Buttons
        btnEdit = findViewById(R.id.btn_edit_detail);
        btnDelete = findViewById(R.id.btn_delete_detail);

        recordId = getIntent().getIntExtra("RECORD_ID", -1);

        if (recordId != -1) {
            loadRecordDetails(recordId);
        } else {
            finish();
        }

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> showDeleteConfirmation());

        btnEdit.setOnClickListener(v -> showEditPopup());
    }

    private void loadRecordDetails(int id) {
        currentRecord = dbHelper.getBillRecord(id);

        if (currentRecord != null) {
            tvMonth.setText(String.format("Month: %s", currentRecord.getMonth()));
            tvUnit.setText(String.format(Locale.getDefault(), "Units Used (kWh): %.2f", currentRecord.getUnitsUsed()));
            tvTotalCharges.setText(String.format(Locale.getDefault(), "Total Charges: RM %.2f", currentRecord.getTotalCharges()));
            tvRebate.setText(String.format(Locale.getDefault(), "Rebate Percentage: %.1f%%", currentRecord.getRebatePercentage()));
            tvFinalCost.setText(String.format(Locale.getDefault(), "Final Cost: RM %.2f", currentRecord.getFinalCost()));
        }
    }

    private void showDeleteConfirmation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Record")
                .setMessage("Are you sure you want to delete the bill record for " + currentRecord.getMonth() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteBillRecord(recordId);
                    Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
                    finish(); // Return to History
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditPopup() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_bill, null);
        TextInputEditText etUnits = dialogView.findViewById(R.id.et_edit_units);
        Spinner spinnerRebate = dialogView.findViewById(R.id.spinner_edit_rebate);

        String[] options = {"0", "1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerRebate.setAdapter(adapter);

        etUnits.setText(String.valueOf(currentRecord.getUnitsUsed()));
        spinnerRebate.setSelection((int) currentRecord.getRebatePercentage());

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save Changes", (dialog, which) -> {
                    String input = etUnits.getText().toString();
                    if (!input.isEmpty()) {
                        double newUnits = Double.parseDouble(input);
                        double newRebate = Double.parseDouble(spinnerRebate.getSelectedItem().toString());

                        BillCalculator.CalculationResult result = BillCalculator.calculate(newUnits, newRebate);

                        dbHelper.updateBillRecord(recordId, newUnits, result.totalCharges, newRebate, result.finalCost);

                        loadRecordDetails(recordId);
                        Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}