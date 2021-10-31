package com.aatesting.bugtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aatesting.bugtracker.Message;


public class ProjectsDatabase {
    MyDbHelper myhelper;
    public ProjectsDatabase(Context context)
    {
        myhelper = new MyDbHelper(context);
    }

    public long InsertData(String name, String pass)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDbHelper.title, name);
        contentValues.put(MyDbHelper.description, pass);
        contentValues.put(MyDbHelper.title, name);
        contentValues.put(MyDbHelper.date, "");
        long id = dbb.insert(MyDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String GetData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {MyDbHelper.id, MyDbHelper.title, MyDbHelper.description};
        Cursor cursor = db.query(MyDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid = cursor.getInt(cursor.getColumnIndex(MyDbHelper.id));
            String title = cursor.getString(cursor.getColumnIndex(MyDbHelper.title));
            String description = cursor.getString(cursor.getColumnIndex(MyDbHelper.description));
            buffer.append(title + "/" + description + "/" + cid + "/");
        }
        return buffer.toString();
    }

    public int Delete(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={id};
        Log.wtf("removing", id);
        int count =db.delete(MyDbHelper.TABLE_NAME , MyDbHelper.id + " = ?",whereArgs);
        return count;
    }

    public int UpdateName(String oldName, String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDbHelper.title,newName);
        String[] whereArgs= {oldName};
        int count = db.update(MyDbHelper.TABLE_NAME,contentValues, MyDbHelper.title + " = ?",whereArgs );
        return count;
    }

    static class MyDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "ProjectCreateTable";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 6;    // Database Version
        private static final String id = "_id";     // Column I (Primary Key)
        private static final String title = "Name";    //Column II
        private static final String description = "Description";    // Column III
        private static final String date = "Date";    // Column IV
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + title + " TEXT,"
                + description + " TEXT,"
                + date + " TEXT)";
        private static final String DROP_TABLE =" DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public MyDbHelper(Context context) {
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
            //for adding new string column
            //db.execSQL("ALTER TABLE myTable ADD COLUMN test1 TEXT");

            //delete database
            //context.deleteDatabase(DATABASE_NAME);

            if (newVersion > oldVersion) {
                context.deleteDatabase(DATABASE_NAME);
            }

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