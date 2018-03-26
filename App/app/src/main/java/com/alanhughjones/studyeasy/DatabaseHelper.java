package com.alanhughjones.studyeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alanh on 24/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // columns of the subject table
    public static final String SUBJECT_TABLE = "subject_table";
    public static final String SUBJECT_ID = "SUBJECT_ID";
    public static final String SUBJECT_NAME = "SUBJECT_NAME";

    // columns of the task table
    public static final String TASK_TABLE = "task_table";
    public static final String TASK_ID = "task_id";
    public static final String TASK_DESC = "task_desc";
    public static final String TASK_DATE = "task_date";
    public static final String TASK_SUBJECT_ID = "subject_id_fk";

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2;

    //SQL statement of the subjects table creation
    private static final String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + SUBJECT_TABLE + " ("
            + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUBJECT_NAME + " TEXT);";

    //SQL statement of the task table creation
    private static final String SQL_CREATE_TABLE_TASKS = "CREATE TABLE " + TASK_TABLE + " ("
            + TASK_ID + " INTEGER AUTOINCREMENT, "
            + TASK_SUBJECT_ID + " INTEGER AUTOINCREMENT, "
            + TASK_DESC + " TEXT, "
            + TASK_DATE + " REAL, "
            + "PRIMARY KEY (" + TASK_ID + ", " + TASK_SUBJECT_ID + ") ,"
            + "FOREIGN KEY(" + TASK_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + "(" + SUBJECT_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        db.execSQL(SQL_CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists " + TASK_TABLE);
            db.execSQL("drop table if exists " + SUBJECT_TABLE);
            onCreate(db);
    }

    public boolean insertSubject(String subject) {
        SQLiteDatabase db = this.getWritableDatabase(); // just for testing
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECT_NAME,subject);

        Log.d(TAG, "insertSubject: Adding " + subject + " to " + SUBJECT_TABLE);

        long result = db.insert(SUBJECT_TABLE,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + SUBJECT_TABLE;
        Cursor result = db.rawQuery(query,null);
        return result;
    }

    // Returns only the ID that matches the subject passed in
    public Cursor getItemID(String subj_name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + SUBJECT_ID  + " FROM " + SUBJECT_TABLE +
                " WHERE " + SUBJECT_NAME + " = '" + subj_name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
