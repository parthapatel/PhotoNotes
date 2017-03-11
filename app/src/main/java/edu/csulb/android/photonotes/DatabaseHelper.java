package edu.csulb.android.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by parth on 3/7/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PhotoNotes";
    private static final String Photos_Table = "Photos";
    private static final String P_Id = "Account_Id";
    private static final String P_Location = "Account_Location";
    private static final String P_Caption = "Account_Caption";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + Photos_Table + "("
                + P_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + P_Location + " TEXT,"
                + P_Caption + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Photos_Table);
        onCreate(db);
    }

    public boolean insertData(NotesData Dataset) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        ContentValues values = new ContentValues();
        values.put(P_Id, Dataset.id);
        values.put(P_Caption, Dataset.caption);
        values.put(P_Location, Dataset.location);

        long store_id = db.insert(Photos_Table, null, values);
        System.out.println(store_id);
        db.close();

        if(store_id>0)
            return true;

        return false;

    }

    public ArrayList<NotesData> getList() {
        ArrayList<NotesData> nds = new ArrayList<NotesData>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                NotesData nd = new NotesData();
//                System.out.println(nd.id);
                nd.id = cursor.getString(0);
                nd.caption = cursor.getString(2);
                nd.location = cursor.getString(1);
                nds.add(nd);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return nds;
    }


    public NotesData getPhoto(String id) {
        NotesData nd = new NotesData();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String query = "select * from " + Photos_Table + " where `" + P_Id + "` = '" + id + "'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
//                System.out.println(nd.id);
                nd.id = cursor.getString(0);
                nd.caption = cursor.getString(2);
                nd.location = cursor.getString(1);
            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return nd;
    }
}
