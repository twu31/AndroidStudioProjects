package com.example.tonywu.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";


    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + SYMBOL + " TEXT not null unique, " + COMPANY +" TEXT not null)";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(SQL_CREATE_TABLE);
        Log.d("print SQL_TABLE", SQL_CREATE_TABLE);
    }

    public void addStock(Stock stock){
        Log.d("dbhelper", "addstock: Adding "+stock.getSymbol());
        ContentValues values = new ContentValues();
        values.put(SYMBOL,stock.getSymbol());
        values.put(COMPANY,stock.getName());

        database.insert(TABLE_NAME,null,values);

        Log.d("dbhelper", "addstock: Addcomplete");
    }

    public void deleteStock(String symbol){
        Log.d("dbhelper", "Deletestock:deleting stock"+symbol);
        int cnt = database.delete(TABLE_NAME, SYMBOL + " =?", new String[]{symbol});
        Log.d("dbhelper", "deleting "+cnt);
    }

    public ArrayList<String[]> loadStocks(){
        ArrayList<String[]> stocks=new ArrayList<>();
        Log.d("print in databaseHelper","SYMBOL"+SYMBOL+"Company"+COMPANY);
        Cursor cursor = database.query(TABLE_NAME, new String[]{SYMBOL, COMPANY},null,null,null,null,null);
        if (cursor !=null){
            cursor.moveToFirst();
            for(int i = 0; i<cursor.getCount(); i++){

                String sym = cursor.getString(0);
                String com = cursor.getString(1);
                stocks.add(new String[]{sym,com});
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n){

    }
}
