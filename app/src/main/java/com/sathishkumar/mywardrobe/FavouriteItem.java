package com.sathishkumar.mywardrobe;

import android.content.ContentValues;
import android.database.Cursor;

import com.sathishkumar.mywardrobe.Database.Db;

import rx.functions.Func1;

/*Created by Sathish 30-07-2020*/

public class FavouriteItem {
    public static final String TABLE = "favourite_item";

    public static final String ID_S = "id_s";
    public static final String ID_P = "id_p";

    private long id_s, is_p;

    public long getId_s() {
        return id_s;
    }

    public void setId_s(long id_s) {
        this.id_s = id_s;
    }

    public long getIs_p() {
        return is_p;
    }

    public void setIs_p(long is_p) {
        this.is_p = is_p;
    }

    public static String QUERY_FAVOURITE = "SELECT * FROM " + TABLE + " WHERE " ;

    public static Func1<Cursor, FavouriteItem> MAPPER = new Func1<Cursor, FavouriteItem>() {

        @Override
        public FavouriteItem call(Cursor cursor) {

            FavouriteItem favouriteItem = new FavouriteItem();
            favouriteItem.setId_s(Db.getLong(cursor, ID_S));
            favouriteItem.setIs_p(Db.getLong(cursor, ID_P));
            return favouriteItem;
        }
    };

    public static final class Builder {
        private final ContentValues contentValues = new ContentValues();

        public FavouriteItem.Builder id_s(long id_s) {
            contentValues.put(ID_S, id_s);
            return this;
        }

        public FavouriteItem.Builder id_p(long id_p) {
            contentValues.put(ID_P, id_p);
            return this;
        }

        public ContentValues build() {
            return contentValues;
        }
    }

}
