package com.mab.womensafety;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="WOMEN_SAFETY";
    private static final int DATABASE_VERSION = 1;
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS contacts( name text, relation text, mobile text)";
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addDetails(String name,String relation,String mobile){
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO contacts VALUES ('"+name+"','"+relation+"','"+mobile+"');";
        db.execSQL(insert);
    }
    public String getMobile(String relation){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT mobile FROM contacts WHERE relation = '"+relation+"';";
        Cursor c = db.rawQuery(selectQuery,null);
        c.moveToFirst();
        if(c.getCount()>0){
            Log.i("Select Query",c.getString(0));
            return c.getString(0);
        }
        c.close();
        return null;
    }
    public String getName(String relation){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT name FROM contacts WHERE relation = '"+relation+"';";
        Cursor c = db.rawQuery(selectQuery,null);
        c.moveToFirst();
        if(c.getCount()>0){
            Log.i("Select Query",c.getString(0));
            return c.getString(0);
        }
        c.close();
        return null;
    }
    public void updateDetails(String name,String relation,String mobile){
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "Update contacts Set Name = '"+name+"', Mobile = '"+mobile+" 'where relation='"+relation+"';";
        db.execSQL(update);
    }
}
