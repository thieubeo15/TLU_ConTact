package com.example.tlucontact.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlucontact.data.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public ContactDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_ID, contact.getId());
        values.put(DatabaseHelper.COLUMN_CONTACT_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_CONTACT_POSITION, contact.getPosition());
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, contact.getPhone());
        values.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, contact.getEmail());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ID_FK, contact.getDepartmentId());

        return db.insert(DatabaseHelper.TABLE_CONTACT, null, values);
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CONTACT, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_POSITION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT_ID_FK))
                );
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return contacts;
    }

    // Thêm phương thức xóa liên hệ
    public int deleteContact(String contactId) {
        return db.delete(DatabaseHelper.TABLE_CONTACT,
                DatabaseHelper.COLUMN_CONTACT_ID + " = ?",
                new String[]{contactId});
    }

    // Thêm phương thức sửa liên hệ
    public int updateContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_NAME, contact.getName());
        values.put(DatabaseHelper.COLUMN_CONTACT_POSITION, contact.getPosition());
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, contact.getPhone());
        values.put(DatabaseHelper.COLUMN_CONTACT_EMAIL, contact.getEmail());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ID_FK, contact.getDepartmentId());

        return db.update(DatabaseHelper.TABLE_CONTACT,
                values,
                DatabaseHelper.COLUMN_CONTACT_ID + " = ?",
                new String[]{contact.getId()});
    }
}
