package com.example.tlucontact.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tlu_contact.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và cột cho Department
    public static final String TABLE_DEPARTMENT = "department";
    public static final String COLUMN_DEPARTMENT_ID = "id";
    public static final String COLUMN_DEPARTMENT_NAME = "name";
    public static final String COLUMN_DEPARTMENT_PHONE = "phone";
    public static final String COLUMN_DEPARTMENT_ADDRESS = "address";

    // Tên bảng và cột cho Contact
    public static final String TABLE_CONTACT = "contact";
    public static final String COLUMN_CONTACT_ID = "id";
    public static final String COLUMN_CONTACT_NAME = "name";
    public static final String COLUMN_CONTACT_POSITION = "position";
    public static final String COLUMN_CONTACT_PHONE = "phone";
    public static final String COLUMN_CONTACT_EMAIL = "email";
    public static final String COLUMN_DEPARTMENT_ID_FK = "department_id"; // Khóa ngoại liên kết Department

    // Câu lệnh tạo bảng Department
    private static final String CREATE_TABLE_DEPARTMENT =
            "CREATE TABLE " + TABLE_DEPARTMENT + " (" +
                    COLUMN_DEPARTMENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_DEPARTMENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_DEPARTMENT_PHONE + " TEXT, " +
                    COLUMN_DEPARTMENT_ADDRESS + " TEXT);";

    // Câu lệnh tạo bảng Contact
    private static final String CREATE_TABLE_CONTACT =
            "CREATE TABLE " + TABLE_CONTACT + " (" +
                    COLUMN_CONTACT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_CONTACT_NAME + " TEXT NOT NULL, " +
                    COLUMN_CONTACT_POSITION + " TEXT, " +
                    COLUMN_CONTACT_PHONE + " TEXT, " +
                    COLUMN_CONTACT_EMAIL + " TEXT, " +
                    COLUMN_DEPARTMENT_ID_FK + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_DEPARTMENT_ID_FK + ") REFERENCES " + TABLE_DEPARTMENT + "(" + COLUMN_DEPARTMENT_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEPARTMENT);
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENT);
        onCreate(db);
    }
}
