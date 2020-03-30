package com.example.annuairepro.service;

import android.content.Context;
import android.util.Log;

import com.example.annuairepro.dao.DataBaseHandler;
import com.example.annuairepro.model.ContactModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ContactService {
    private static String TAG = ContactService.class.getSimpleName();
    List<ContactModel> contacts;
    Context ctx;
    private static  ContactService cs;
    private DataBaseHandler dbh;


    public static ContactService getInstance(Context ctx){
        if(cs == null){
            cs = new ContactService(ctx);
        }
        return cs;
    }
    private ContactService(Context ctx) {
        this.ctx = ctx;
        contacts = readContacts();
        dbh = new DataBaseHandler(ctx);
    }

    private List<ContactModel> readContacts() {
        List<ContactModel> contacts = new ArrayList<>();
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open("contacts.json"), "UTF-8"));

            String contactsString = "";
            String line;
            while ((line = reader.readLine()) != null) {
                contactsString = contactsString + line;
            }
            JSONObject root = new JSONObject(contactsString);
            JSONArray contactsJson = root.getJSONArray("contacts");
            JSONObject tem;
            for (int i = 0; i < contactsJson.length(); i++) {
                tem = contactsJson.getJSONObject(i);
                ContactModel cm = new ContactModel(tem.getInt("id"), tem.getString("first_name"), tem.getString("last_name"), tem.getString("phone"), tem.getString("email"), tem.getString("job"));
                contacts.add(cm);
                Log.d("TAG",cm.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }
    public void addContact(ContactModel cm){
        contacts.add(cm);
        dbh.addContact(cm);
    }
    public void deleteContact(int id){
        dbh.deleteContact(id);
    }
    public List<ContactModel> getContactsFromDB(){
        return dbh.getAll();
    }
    public List<ContactModel> getContacts() {
        Log.d(TAG, " " + contacts.size());
        return contacts;
    }
}
