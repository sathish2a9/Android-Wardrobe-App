package com.sathishkumar.mywardrobe.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sathishkumar.mywardrobe.FavouriteItem;
import com.sathishkumar.mywardrobe.WardrobeItem;

import javax.inject.Inject;

/*Created by Sathish 30-07-2020*/


public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static DbOpenHelper mInstance = null;
    private Context context;

    private static final String CREATE_WARDROBE_ITEM = "CREATE TABLE "
            + WardrobeItem.TABLE + "("
            + WardrobeItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + WardrobeItem.TYPE + " TEXT NOT NULL,"
            + WardrobeItem.DATA + " BLOB NOT NULL"
            + ")";

    private static final String CREATE_FAVOURITE_ITEM = "CREATE TABLE "
            + FavouriteItem.TABLE + "("
            + FavouriteItem.ID_S + " INTEGER NOT NULL,"
            + FavouriteItem.ID_P + " INTEGER NOT NULL"
            + ")";


    public DbOpenHelper(Context context) {
        super(context, "wardrobe.db", null, VERSION);
        this.context = context;
    }

    public static DbOpenHelper getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DbOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_WARDROBE_ITEM);
        sqLiteDatabase.execSQL(CREATE_FAVOURITE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
