package com.desislava.market.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.desislava.market.server.communication.ParseServerResponse.jsonVersion;

public class DBHelper extends SQLiteOpenHelper {

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
    public DBHelper(Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db != null/* && oldVersion < newVersion*/) {
            db.setVersion(newVersion);
            Log.i("onUpgrade", "**********UPDATED table version**************");
        }

     // onCreate(db);
    }


    public void insertValue (String name, String price, String date) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRICE_COLUMN_NAME, name);
            contentValues.put(PRICE_COLUMN_PRICE, price);
            contentValues.put(PRICE_COLUMN_DATE, date.toString());
            long newRowId = db.insert(PRICE_TABLE, null, contentValues);
            Log.i("insertUpdateDB", newRowId + " inserted ******************************  " + contentValues.toString());

    }


    public boolean isUpdateIsNeeded() {
        int oldVersion = this.getReadableDatabase().getVersion();

        if (jsonVersion > this.getReadableDatabase().getVersion()) {
            this.onUpgrade(this.getWritableDatabase(), oldVersion, jsonVersion);
            return true;
        }
        return false;


    }


}
