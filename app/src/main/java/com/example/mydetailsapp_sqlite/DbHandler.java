package com.example.mydetailsapp_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qendev on 11-10-2020.
 */


public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mydetailsdb";
    private static final String TABLE_Users = "userdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOC = "location";
    private static final String KEY_DESG = "designation";

    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    //Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase mysqlitedb){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_LOC + " TEXT,"
                + KEY_DESG + " TEXT"+ ")";
        mysqlitedb.execSQL(CREATE_TABLE);
    }
    //Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase sqlitedb, int oldVersion, int newVersion){
        // Drop older table if exist
        sqlitedb.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        // Create tables again
        onCreate(sqlitedb);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertUserDetails(String name, String location, String designation){
        //Get the Data Repository in write mode
        SQLiteDatabase sqlite = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, name);
        cValues.put(KEY_LOC, location);
        cValues.put(KEY_DESG, designation);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = sqlite.insert(TABLE_Users,null, cValues);
        sqlite.close();
    }

    // Get User Details
    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase sqdb = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT name, location, designation FROM "+ TABLE_Users;
        Cursor cursor = sqdb.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("name",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("designation",cursor.getString(cursor.getColumnIndex(KEY_DESG)));
            user.put("location",cursor.getString(cursor.getColumnIndex(KEY_LOC)));
            userList.add(user);
        }
        return  userList;
    }
    // Get User Details based on userid
    public ArrayList<HashMap<String, String>> GetUserByUserId(int userid){
        SQLiteDatabase sdb = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT name, location, designation FROM "+ TABLE_Users;
        Cursor cursor = sdb.query(TABLE_Users, new String[]{KEY_NAME, KEY_LOC, KEY_DESG}, KEY_ID+ "=?",new String[]{String.valueOf(userid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("name",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("designation",cursor.getString(cursor.getColumnIndex(KEY_DESG)));
            user.put("location",cursor.getString(cursor.getColumnIndex(KEY_LOC)));
            userList.add(user);
        }
        return  userList;
    }

    // Delete User Details
    public void DeleteUser(int userid){
        SQLiteDatabase sDB = this.getWritableDatabase();
        sDB.delete(TABLE_Users, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        sDB.close();
    }

    // Update User Details
    public int UpdateUserDetails(String location, String designation, int id){
        SQLiteDatabase SDB = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_LOC, location);
        cVals.put(KEY_DESG, designation);
        int count = SDB.update(TABLE_Users, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;
    }
}
