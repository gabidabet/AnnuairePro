package com.example.annuairepro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.annuairepro.model.ContactModel;
import com.example.annuairepro.service.ContactService;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";
    private static final String TABLE_CONTACTS = "contacts_table";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "first_name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_JOB = "job";
    private static  final String TAG = DataBaseHandler.class.getSimpleName();
    private Context ctx;

    private SQLiteDatabase db;
    public DataBaseHandler(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // this.db = db;
        String createTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s varchar(25) NOT NULL, " +
                        "%s varchar(25), " +
                        "%s varchar(10) NOT NULL, " +
                        "%s varchar(255), " +
                        "%s varchar(255) " +
                        ")",TABLE_CONTACTS,KEY_ID,KEY_FIRSTNAME,KEY_LASTNAME,KEY_PHONE,KEY_EMAIL,KEY_JOB
        );
        Log.d(TAG, "onCreate: " + createTable);
        try{
            db.execSQL(createTable);

           /* for (ContactModel cm : initList
            ) {
                addContact(cm);
            }*/
        }catch(Exception e){

            Log.d(TAG,"SQL PROBLEM CREATING TABLE");
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE ="DROP TABLE IF EXISTS "+ TABLE_CONTACTS;
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addContact(ContactModel cm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,cm.getId());
        values.put(KEY_FIRSTNAME,cm.getFirstName());
        values.put(KEY_LASTNAME,cm.getLastName());
        values.put(KEY_EMAIL,cm.getEmail());
        values.put(KEY_JOB,cm.getJob());
        values.put(KEY_PHONE,cm.getNumber());

        db.insert(TABLE_CONTACTS,null, values);

        db.close();
    }
    public ContactModel getContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectId = "SELECT * FROM " + TABLE_CONTACTS + " where id = " + id;
        Cursor cursor = db.rawQuery(selectId,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        ContactModel cm = new ContactModel();
        cm.setId(cursor.getInt(0));
        cm.setFirstName(cursor.getString(1));
        cm.setLastName(cursor.getString(2));
        cm.setNumber(cursor.getString(3));
        cm.setEmail(cursor.getString(4));
        cm.setJob(cursor.getString(5));
        db.close();

        return cm;
    }
    public List<ContactModel> getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<ContactModel> contactsList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel cm = new ContactModel();
                cm.setId(cursor.getInt(0));
                cm.setFirstName(cursor.getString(1));
                cm.setLastName(cursor.getString(2));
                cm.setNumber(cursor.getString(3));
                cm.setEmail(cursor.getString(4));
                cm.setJob(cursor.getString(5));
                // Adding note to list
                contactsList.add(cm);
            } while (cursor.moveToNext());
        }

        db.close();
        return contactsList;
    }
    public void updateContact(ContactModel cm){
        SQLiteStatement statement = db.compileStatement("" +
                "UPDATE " + TABLE_CONTACTS + " SET " + KEY_FIRSTNAME + " = ? ," +
                " " + KEY_LASTNAME + " = ? ," +
                " " + KEY_EMAIL + " = ? ," +
                " " + KEY_PHONE + " = ? ," +
                " " + KEY_JOB + " = ? WHERE " +
                "id = ?");
        statement.bindString(1, cm.getFirstName());
        statement.bindString(2, cm.getLastName());
        statement.bindString(3, cm.getEmail());
        statement.bindString(4, cm.getNumber());
        statement.bindString(5, cm.getJob());
        statement.bindLong(6, cm.getId());
        statement.executeInsert();
    }
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
