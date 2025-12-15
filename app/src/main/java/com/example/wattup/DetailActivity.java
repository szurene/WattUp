package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wattup.database.DatabaseHelper;
import com.example.wattup.model.BillRecord;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvMonth, tvUnit, tvTotalCharges, tvRebate, tvFinalCost;
    private DatabaseHelper dbHelper;

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

        int recordId = getIntent().getIntExtra("RECORD_ID", -1);

        if (recordId != -1) {
            loadRecordDetails(recordId);
        } else {
            finish();
        }

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadRecordDetails(int id) {
        BillRecord record = dbHelper.getBillRecord(id);

        if (record != null) {
            tvMonth.setText(String.format("Month: %s", record.getMonth()));
            tvUnit.setText(String.format(Locale.getDefault(),
                    "Units Used (kWh): %.2f", record.getUnitsUsed()));
            tvTotalCharges.setText(String.format(Locale.getDefault(),
                    "Total Charges: RM %.2f", record.getTotalCharges()));
            tvRebate.setText(String.format(Locale.getDefault(),
                    "Rebate Percentage: %.1f%%", record.getRebatePercentage()));
            tvFinalCost.setText(String.format(Locale.getDefault(),
                    "Final Cost: RM %.2f", record.getFinalCost()));
        }
    }
}