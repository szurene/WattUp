package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.example.wattup.database.DatabaseHelper;
import com.example.wattup.model.BillRecord;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private ListView listViewHistory;
    private DatabaseHelper dbHelper;
    private List<BillRecord> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DatabaseHelper(this);
        listViewHistory = findViewById(R.id.listview_history);

        loadHistoryData();
        setupListItemClickListener();
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadHistoryData() {
        recordList = dbHelper.getAllBillRecords();
        ArrayList<String> displayList = new ArrayList<>();

        for (BillRecord record : recordList) {
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
            BillRecord selectedRecord = recordList.get(position);
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