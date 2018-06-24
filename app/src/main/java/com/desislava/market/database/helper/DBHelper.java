package com.desislava.market.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private SQLiteDatabase writeable;
    public DBHelper(Context context,int version) {
        super(context,DB_NAME,null,version);
        readable=this.getReadableDatabase();
        writeable=this.getWritableDatabase();
        Log.i("DBHelper","********************************************************* with "+readable.getVersion());
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
        Log.i("onDowngrade", "**********DOWNGRADE DB version nothing is DONE HERE**************"+db.getVersion());
    }

    public void insertValue (String name, String price, String date) {
            //SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRICE_COLUMN_NAME, name);
            contentValues.put(PRICE_COLUMN_PRICE, price);
            contentValues.put(PRICE_COLUMN_DATE, date.toString());
            long newRowId = writeable.insert(PRICE_TABLE, null, contentValues);
            Log.i("insertUpdateDB", newRowId + "**** value: " + contentValues.toString());

    }


    public boolean isUpdateIsNeeded() {
        //int oldVersion = this.getReadableDatabase().getVersion();
        int oldVersion = readable.getVersion();
        Log.i("isUpdateIsNeeded" ,"old: "+oldVersion+"  ,"+"json: "+jsonVersion);
        if (4 > oldVersion) {
            Log.i("isUpdateIsNeeded if" ,"++++++++++ YES ++++++++");
            writeable.setVersion(4);
            /*this.onUpgrade(this.getWritableDatabase(), oldVersion, jsonVersion);*/
            Log.i("hehehhehe","after update : " + readable.getVersion());
            return true;
        }
        return false;


    }

    public  Map<Date, Float> getAllPrices(String productName) {
        Log.i("DB getAllPrices", "*** Enter ***");
        String query = "SELECT " + PRICE_COLUMN_PRICE + " , " + PRICE_COLUMN_DATE + " FROM " + PRICE_TABLE + " where price_chart.name='"+productName+"'" ;
        Map<Date, Float> datePrices = new HashMap<>();
        Cursor c = readable.rawQuery(query, null);
        Log.i("NOE IT IS DONWGRADED:","--------------" + readable.getVersion());//this.getReadableDatabase().getVersion()
        if (c.moveToFirst()) {
            do {
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd-MM", Locale.ITALY).parse(c.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePrices.put(date, c.getFloat(0));

            } while (c.moveToNext());
        }

        c.close();
        Log.i("getAllPrices ", "*** LEAVE with map size : *** " + datePrices.size());
        return datePrices;

    }

}
