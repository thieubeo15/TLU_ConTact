package com.example.tlucontact.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.tlucontact.data.model.Department;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DepartmentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addDepartment(Department department) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ID, department.getId());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_NAME, department.getName());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_PHONE, department.getPhone());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS, department.getAddress());
        return db.insert(DatabaseHelper.TABLE_DEPARTMENT, null, values);
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DEPARTMENT, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Department department = new Department(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS))
                );
                departments.add(department);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return departments;
    }

    public int updateDepartment(Department department) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_NAME, department.getName());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_PHONE, department.getPhone());
        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS, department.getAddress());
        return db.update(DatabaseHelper.TABLE_DEPARTMENT, values, DatabaseHelper.COLUMN_DEPARTMENT_ID + " = ?", new String[]{department.getId()});
    }

    public int deleteDepartment(String departmentId) {
        return db.delete(DatabaseHelper.TABLE_DEPARTMENT, DatabaseHelper.COLUMN_DEPARTMENT_ID + " = ?", new String[]{departmentId});
    }
}
