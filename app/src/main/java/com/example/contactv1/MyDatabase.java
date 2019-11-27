package com.example.contactv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Contact";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_AVATAR = "avatar";


    // Hàm dựng database
    public MyDatabase(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // database xuất hiện
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name Text, mobile Text, avatar Text)";
        db.execSQL(createTable);
        System.out.println("Tạo bảng");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String dropTable = "drop table if exists " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);

        System.out.println("Cập nhật bảng");
    }

    // THêm số điện thoại vào database;
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] avatar = contact.getmAvatar(); // this is a function
        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_NAME, contact.getmName());
        values.put(KEY_MOBILE, contact.getmMobile());
        values.put(KEY_AVATAR, avatar);
        db.insert(TABLE_NAME, null, values);
        System.out.println("Them contact " + contact);
        // Đóng kết nối database.
        db.close();
    }

    // Lấy tất cả số điện thoại từ đatabase
    ArrayList<Contact> getAllContacts() {
        System.out.println("Lay tat ca");
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String id = Long.toString(cursor.getLong(0));
                Contact contact = new Contact(id, cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
                contactList.add(contact);
                System.out.println(contact.getmName());
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    // Xóa số điện thoại trong database
    void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + "='" + id + "'");
        System.out.println("Hoan tat xoa");
        db.close();
    }

    void updateContact(String id, Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        byte[] avatar = contact.getmAvatar();

        values.put(KEY_NAME, contact.getmName());
        values.put(KEY_MOBILE, contact.getmMobile());
        values.put(KEY_AVATAR, avatar);

        db.update(TABLE_NAME, values, "id=" + id, null);
        System.out.println("Hoan tat update");
        db.close();
    }
}
