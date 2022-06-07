package ru.serguun42.android.airportenhanced.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CredentialsDB";
    private static final String TABLE_NAME = "Credentials";
    private static final String KEY_ID = "id";
    private static final String KEY_TOKEN = "token";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Credentials ( " + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TOKEN + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public Credentials getCredential() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_TOKEN + " FROM " + TABLE_NAME + " LIMIT 1;", null);

        if (cursor != null)
            cursor.moveToFirst();

        Credentials credentials = new Credentials();
        if (cursor.getCount() > 0)
            credentials.setToken(cursor.getString(0));

        return credentials;
    }

    public void storeCredentials(Credentials credentials) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "", new String[]{});

        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, credentials.getToken());
        db.insert(TABLE_NAME, null, values);

        db.close();
    }
}