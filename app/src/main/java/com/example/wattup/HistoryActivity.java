package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.wattup.database.DatabaseHelper;
import com.example.wattup.model.BillRecord;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private ListView listViewHistory;
    private Spinner spinnerFilterMonth;
    private DatabaseHelper dbHelper;
    private List<BillRecord> allRecordList;
    private List<BillRecord> filteredRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DatabaseHelper(this);
        listViewHistory = findViewById(R.id.listview_history);
        spinnerFilterMonth = findViewById(R.id.spinner_filter_month);

        setupToolbar();
        setupSpinnerFilter();
        setupListItemClickListener();

    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);


        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupSpinnerFilter() {
        String[] months = {"All Months", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                months
        );
        spinnerFilterMonth.setAdapter(adapter);

        spinnerFilterMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = (String) parent.getItemAtPosition(position);
                filterHistoryData(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterHistoryData("All Months");
            }
        });
    }

    private void loadHistoryData() {
        allRecordList = dbHelper.getAllBillRecords();

        filterHistoryData(spinnerFilterMonth.getSelectedItem() != null ?
                (String) spinnerFilterMonth.getSelectedItem() : "All Months");
    }

    private void filterHistoryData(String selectedMonth) {
        filteredRecordList = new ArrayList<>();
        ArrayList<String> displayList = new ArrayList<>();

        if ("All Months".equals(selectedMonth)) {
            filteredRecordList.addAll(allRecordList);
        } else {
            for (BillRecord record : allRecordList) {
                if (record.getMonth().equalsIgnoreCase(selectedMonth)) {
                    filteredRecordList.add(record);
                }
            }
        }

        for (BillRecord record : filteredRecordList) {
            String display = String.format(
                    Locale.getDefault(),
                    "%s  •  RM %.2f",
                    record.getMonth(),
                    record.getFinalCost()
            );
            displayList.add(display);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listViewHistory.setAdapter(adapter);
    }

    private void setupListItemClickListener() {
        listViewHistory.setOnItemClickListener((parent, view, position, id) -> {
            BillRecord selectedRecord = filteredRecordList.get(position);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("RECORD_ID", selectedRecord.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistoryData();
    }
}