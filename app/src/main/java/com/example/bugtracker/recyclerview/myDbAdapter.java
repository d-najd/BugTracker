package com.example.bugtracker.recyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long InsertData(String name, String pass)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.title, name);
        contentValues.put(myDbHelper.description, pass);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String GetData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.id,myDbHelper.title,myDbHelper.description};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.id));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.title));
            String password = cursor.getString(cursor.getColumnIndex(myDbHelper.description));
            buffer.append(name + "/" + password + "/" + cid + "/");
            //buffer.append(cid + "/" + name + "/" + password +"/");
        }
        return buffer.toString();
    }

    public int Delete(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={id};
        Log.wtf("removing", id);
        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.id + " = ?",whereArgs);
        return count;
    }

    public int UpdateName(String oldName , String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.title,newName);
        String[] whereArgs= {oldName};
        int count = db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.title +" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "ProjectCreateTable";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String id = "_id";     // Column I (Primary Key)
        private static final String title = "Name";    //Column II
        private static final String description = "Password";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ("+ id +" INTEGER PRIMARY KEY AUTOINCREMENT, " + title + " VARCHAR(255) ," + description + " VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Message.message(context,"" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,"" + e);
            }
        }
    }
}
