package com.mycontacts.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mycontacts.model.Contact;

import java.util.ArrayList;

public class DatabaseContacts {

    Context context;
    DatabaseHelper helper;

    public DatabaseContacts(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
    }

    /**
     * By invoking this method we can add a contact to the DB
     *
     * @param contact - contact to be added in the database
     */
    public void addContact(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_PHONE, contact.getNumber());
        values.put(DatabaseHelper.COLUMN_EMAIL, contact.getEmail());

        db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * By invoking this method we can update the contact info in the db
     *
     * @param contact - contact to be updated in the database
     */
    public void updateContact(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = DatabaseHelper.COLUMN_ID + " =?";
        String[] selectionArgs = {contact.getId() + ""};

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_PHONE, contact.getNumber());
        values.put(DatabaseHelper.COLUMN_EMAIL, contact.getEmail());

        db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    /**
     * By invoking this method we can save the list of contacts to the db
     *
     * @param contactArrayList - list of contacts to be saved to the DB
     */
    public void saveContacts(ArrayList<Contact> contactArrayList) {
        SQLiteDatabase db = helper.getWritableDatabase();

        for (int i = 0; i < contactArrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, contactArrayList.get(i).getName());
            values.put(DatabaseHelper.COLUMN_PHONE, contactArrayList.get(i).getNumber());
            values.put(DatabaseHelper.COLUMN_EMAIL, contactArrayList.get(i).getEmail());

            db.insert(DatabaseHelper.TABLE_NAME, null, values);
        }
        db.close();
    }


    /**
     * By invoking this method we can fetch the list of contacts from the DB
     *
     * @return list of contacts which are saved in the DB
     */
    public ArrayList<Contact> getContactsData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        Contact contact;
        if (cursor.moveToFirst()) {
            do {
                contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));

                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    /**
     * By invoking this method we can fetch the contact object for a particular contact id
     *
     * @param id - contact id to be fetched from db
     * @return - contact object of the specified contact id
     */
    public Contact getContact(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Contact contact;

        String where = DatabaseHelper.COLUMN_ID + "=" + id;
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                        DatabaseHelper.COLUMN_PHONE, DatabaseHelper.COLUMN_EMAIL},
                where, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setNumber(cursor.getString(2));
        contact.setEmail(cursor.getString(3));
        return contact;
    }

    /**
     * By invoking this method we can delete a contact of specified contact id from the db
     *
     * @param id - contact id to be deleted from the db
     */
    public void deleteContact(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID + " =?";
        String[] selectionArgs = {id + ""};
        db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}