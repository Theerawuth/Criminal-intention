package com.augmentis.ayp.crimin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.augmentis.ayp.crimin.CrimeDbSchema.CrimeTable;

/**
 * Created by Theerawuth on 8/1/2016.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 5;

    private static final String DATABASE_NAME = "crimeBase.db";
    private static final String TAG = "CrimeBaseHelper";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create Database");
        db.execSQL("CREATE table " + CrimeTable.NAME
                + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "Running upgrade db..");

        //1. rename table to (oldversion)
        db.execSQL("alter table " + CrimeTable.NAME + " rename to " + CrimeTable.NAME + "_" + oldVersion);

        //2. drop table
        db.execSQL("drop table if exists " + CrimeTable.NAME);

        //3. create new table
        db.execSQL("CREATE table " + CrimeTable.NAME
                + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );

        //4. insert data from temp table
        db.execSQL("insert into " + CrimeTable.NAME
                + " ("
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + ") "
                + " select "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + " from "
                + CrimeTable.NAME + "_"
                + oldVersion
        );

        //5. drop temp table
        db.execSQL("drop table if exists "
                + CrimeTable.NAME
                + "_"
                + oldVersion
        );
    }
}
