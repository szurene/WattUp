package com.example.wattup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wattup.model.BillRecord;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WattUpDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BILLS = "bills";

    private static final String KEY_ID = "id";
    private static final String KEY_MONTH = "month";
    private static final String KEY_UNITS = "units_used";
    private static final String KEY_TOTAL_CHARGES = "total_charges";
    private static final String KEY_REBATE = "rebate_percentage";
    private static final String KEY_FINAL_COST = "final_cost";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MONTH + " TEXT NOT NULL,"
                + KEY_UNITS + " REAL NOT NULL,"
                + KEY_TOTAL_CHARGES + " REAL NOT NULL,"
                + KEY_REBATE + " REAL NOT NULL,"
                + KEY_FINAL_COST + " REAL NOT NULL"
                + ")";
        db.execSQL(CREATE_BILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public long addBillRecord(BillRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MONTH, record.getMonth());
        values.put(KEY_UNITS, record.getUnitsUsed());
        values.put(KEY_TOTAL_CHARGES, record.getTotalCharges());
        values.put(KEY_REBATE, record.getRebatePercentage());
        values.put(KEY_FINAL_COST, record.getFinalCost());

        long id = db.insert(TABLE_BILLS, null, values);
        db.close();
        return id;
    }

    public BillRecord getBillRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_BILLS,
                new String[]{KEY_ID, KEY_MONTH, KEY_UNITS, KEY_TOTAL_CHARGES, KEY_REBATE, KEY_FINAL_COST},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        BillRecord record = null;

        if (cursor != null && cursor.moveToFirst()) {
            record = new BillRecord(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_MONTH)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_UNITS)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_CHARGES)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_REBATE)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_FINAL_COST))
            );
            cursor.close();
        }

        db.close();
        return record;
    }

    public List<BillRecord> getAllBillRecords() {
        List<BillRecord> recordList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BILLS + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BillRecord record = new BillRecord(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_MONTH)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_UNITS)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_CHARGES)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_REBATE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_FINAL_COST))
                );
                recordList.add(record);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return recordList;
    }
}