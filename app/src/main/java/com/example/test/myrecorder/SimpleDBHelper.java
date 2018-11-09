package com.example.test.myrecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleDBHelper extends SQLiteOpenHelper {

    public static final String DBName = "Record.db";

    public static final String MY_RECORD_TABLE = "myRecord";

    private static final String CREATE_DAY_OF_CALORIE_TABLE
            = "create table " + MY_RECORD_TABLE + "(id integer primary key autoincrement," +
                                                    "savedName text," +
                                                    "displayName text," +
                                                    "isUploaded integer," +
                                                    "durationSeconds interger," +
                                                    "recordedDate text," +
                                                    "isDeleted integer)";

//    private static final String UPDATE_STUDENT_TABLE
//            = "alter table " + DAY_OF_CALORIE + " add height integer";


    public SimpleDBHelper(Context context, int version) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DAY_OF_CALORIE_TABLE);
       // sqLiteDatabase.execSQL("alter table "+MY_RECORD_TABLE+" add isHidden default 0;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        switch (i) {
            case 1:
                //upgrade logic from 1 to 2
//                sqLiteDatabase.execSQL(CREATE_DAY_OF_CALORIE_TABLE);
            case 2:

                // upgrade logic from 2 to 3

                break;
            default:
                throw new IllegalStateException("unknown oldVersion " + i);
        }

    }
}