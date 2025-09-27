package com.example.ujian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "go21.db";
    private static final int DB_VERSION = 1;

    // users
    public static final String TABLE_USERS = "users";
    public static final String U_ID = "id";
    public static final String U_USERNAME = "username";
    public static final String U_PASSWORD = "password";

    // drivers
    public static final String TABLE_DRIVERS = "drivers";
    public static final String D_ID = "id";
    public static final String D_NAME = "name";
    public static final String D_VEHICLE = "vehicle";
    public static final String D_PHONE = "phone";
    public static final String D_STATUS = "status"; // online/offline

    // trips
    public static final String TABLE_TRIPS = "trips";
    public static final String T_ID = "id";
    public static final String T_DRIVER_ID = "driver_id";
    public static final String T_FROM = "from_loc";
    public static final String T_TO = "to_loc";
    public static final String T_TIME = "time";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_USERNAME + " TEXT UNIQUE, " +
                U_PASSWORD + " TEXT)";
        db.execSQL(createUsers);

        String createDrivers = "CREATE TABLE " + TABLE_DRIVERS + " (" +
                D_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D_NAME + " TEXT, " +
                D_VEHICLE + " TEXT, " +
                D_PHONE + " TEXT, " +
                D_STATUS + " TEXT)";
        db.execSQL(createDrivers);

        String createTrips = "CREATE TABLE " + TABLE_TRIPS + " (" +
                T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                T_DRIVER_ID + " INTEGER, " +
                T_FROM + " TEXT, " +
                T_TO + " TEXT, " +
                T_TIME + " TEXT)";
        db.execSQL(createTrips);

        // optional: create a default user for testing
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, "admin");
        cv.put(U_PASSWORD, "admin123");
        db.insert(TABLE_USERS, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        onCreate(db);
    }

    // ----- user methods -----
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, username);
        cv.put(U_PASSWORD, password);
        long id = -1;
        try {
            id = db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (Exception e) {
            return false;
        }
        return id != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{U_ID},
                U_USERNAME + "=? AND " + U_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    // ----- driver CRUD -----
    public long addDriver(Driver d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(D_NAME, d.getName());
        cv.put(D_VEHICLE, d.getVehicle());
        cv.put(D_PHONE, d.getPhone());
        cv.put(D_STATUS, d.getStatus());
        return db.insert(TABLE_DRIVERS, null, cv);
    }

    public boolean updateDriver(Driver d) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(D_NAME, d.getName());
        cv.put(D_VEHICLE, d.getVehicle());
        cv.put(D_PHONE, d.getPhone());
        cv.put(D_STATUS, d.getStatus());
        int rows = db.update(TABLE_DRIVERS, cv, D_ID + "=?",
                new String[]{String.valueOf(d.getId())});
        return rows > 0;
    }

    public boolean deleteDriver(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_DRIVERS, D_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public ArrayList<Driver> getAllDrivers() {
        ArrayList<Driver> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DRIVERS + " ORDER BY " + D_ID + " DESC", null);
        if (c.moveToFirst()) {
            do {
                Driver d = new Driver();
                d.setId(c.getInt(c.getColumnIndexOrThrow(D_ID)));
                d.setName(c.getString(c.getColumnIndexOrThrow(D_NAME)));
                d.setVehicle(c.getString(c.getColumnIndexOrThrow(D_VEHICLE)));
                d.setPhone(c.getString(c.getColumnIndexOrThrow(D_PHONE)));
                d.setStatus(c.getString(c.getColumnIndexOrThrow(D_STATUS)));
                list.add(d);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public Driver getDriverById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_DRIVERS, null, D_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Driver d = null;
        if (c.moveToFirst()) {
            d = new Driver();
            d.setId(c.getInt(c.getColumnIndexOrThrow(D_ID)));
            d.setName(c.getString(c.getColumnIndexOrThrow(D_NAME)));
            d.setVehicle(c.getString(c.getColumnIndexOrThrow(D_VEHICLE)));
            d.setPhone(c.getString(c.getColumnIndexOrThrow(D_PHONE)));
            d.setStatus(c.getString(c.getColumnIndexOrThrow(D_STATUS)));
        }
        c.close();
        return d;
    }

    // ----- trips -----
    public long addTrip(int driverId, String from, String to, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T_DRIVER_ID, driverId);
        cv.put(T_FROM, from);
        cv.put(T_TO, to);
        cv.put(T_TIME, time);
        return db.insert(TABLE_TRIPS, null, cv);
    }

    public ArrayList<Trip> getTripsByDriver(int driverId) {
        ArrayList<Trip> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TRIPS, null, T_DRIVER_ID + "=?", new String[]{String.valueOf(driverId)}, null, null, T_ID + " DESC");
        if (c.moveToFirst()) {
            do {
                Trip t = new Trip();
                t.setId(c.getInt(c.getColumnIndexOrThrow(T_ID)));
                t.setDriverId(c.getInt(c.getColumnIndexOrThrow(T_DRIVER_ID)));
                t.setFrom(c.getString(c.getColumnIndexOrThrow(T_FROM)));
                t.setTo(c.getString(c.getColumnIndexOrThrow(T_TO)));
                t.setTime(c.getString(c.getColumnIndexOrThrow(T_TIME)));
                list.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}