package com.sathishkumar.mywardrobe.Database;

import android.database.Cursor;

/*Created by Sathish 30-07-2020*/

public class Db {

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }
}
