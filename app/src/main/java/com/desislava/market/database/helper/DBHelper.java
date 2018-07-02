package com.desislava.market.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.desislava.market.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.desislava.market.server.communication.ParseServerResponse.jsonVersion;

public  class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME="price.db";
    private static String PRICE_TABLE="price_chart";
    private static String PRICE_COLUMN_ID="id";
    private static String PRICE_COLUMN_NAME="name";
    private static String PRICE_COLUMN_PRICE="price";
    private static String PRICE_COLUMN_DATE="date";

    private static String QUERY_ENTRIES="CREATE TABLE "+PRICE_TABLE+" ("+ PRICE_COLUMN_ID + " INTEGER PRIMARY KEY,"+
                                                                          PRICE_COLUMN_NAME + " TEXT," +
                                                                          PRICE_COLUMN_PRICE + " TEXT,"+
                                                                          PRICE_COLUMN_DATE + " DATE)";

    private SQLiteDatabase readable;
    private SQLiteDatabase writable;
    static SharedPreferences.Editor editor;

    public DBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
        readable = this.getReadableDatabase();
        writable = this.getWritableDatabase();
        Log.i("DBHelper", "*************************** with " + readable.getVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db != null && oldVersion < newVersion) {
            //db.setVersion(newVersion);
            Log.i("onUpgrade", "**********UPDATED DB version**************");
        }

        // onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.setVersion(oldVersion);
        Log.i("onDowngrade", "**********DOWNGRADE DB version nothing is DONE HERE**************" + db.getVersion());
    }

    public void insertValue(String name, String price, String date) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRICE_COLUMN_NAME, name);
        contentValues.put(PRICE_COLUMN_PRICE, price);
        contentValues.put(PRICE_COLUMN_DATE, date);
        long newRowId = writable.insert(PRICE_TABLE, null, contentValues);
        Log.i("insertUpdateDB", newRowId + "**** value: " + contentValues.toString());

    }


    public boolean isUpdateIsNeeded() {
        //int oldVersion = this.getReadableDatabase().getVersion();
        int oldVersion = readable.getVersion();
        Log.i("isUpdateIsNeeded", "old: " + oldVersion + "  ," + "json: " + jsonVersion);
        if (jsonVersion > oldVersion) {
            editor = MainActivity.sharedPref.edit();
            editor.putInt(MainActivity.DB_VER_STORE, jsonVersion);
            editor.apply();
            writable.setVersion(jsonVersion);
            Log.i("isUpdateIsNeeded if", "++++++++++ YES ++++++++" + readable.getVersion());
            return true;
        }

        return false;

    }

    public Map<Long, Float> getAllPrices(String productName) {
        Log.i("DB getAllPrices", "*** Enter ***");
        String query = "SELECT " + PRICE_COLUMN_PRICE + " , " + PRICE_COLUMN_DATE + " FROM " + PRICE_TABLE + " where price_chart.name='" + productName + "'";
        Map<Long, Float> datePrices = new HashMap<>();
        Cursor c = readable.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Long time = null;
                try {
                    time = new SimpleDateFormat("dd-MM", Locale.ITALY).parse(c.getString(1)).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePrices.put(time, c.getFloat(0));

            } while (c.moveToNext());
        }

        c.close();
        Log.i("getAllPrices ", "*** LEAVE with map size : *** " + datePrices.size());
        return datePrices;

    }

}
