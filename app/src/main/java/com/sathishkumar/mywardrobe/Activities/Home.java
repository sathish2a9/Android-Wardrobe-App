package com.sathishkumar.mywardrobe.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.sathishkumar.mywardrobe.Events.ShuffleEvent;
import com.sathishkumar.mywardrobe.FavouriteItem;
import com.sathishkumar.mywardrobe.Fragments.PantFragment;
import com.sathishkumar.mywardrobe.Fragments.ShirtFragment;
import com.sathishkumar.mywardrobe.OnImageChangeListener;
import com.sathishkumar.mywardrobe.R;
import com.sathishkumar.mywardrobe.WardrobeApp;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/*Created by Sathish 30-07-2020*/

public class Home extends AppCompatActivity implements OnImageChangeListener {

    public static long ID_S, ID_P;
    public boolean isFavourite;

    @Inject
    BriteDatabase db;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.btn_shuffle)
    ImageButton shuffleBtn;

    @BindView(R.id.btn_favourite)
    ImageButton favouriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        WardrobeApp.getComponent(this).inject(this);

        getSupportActionBar().setTitle(R.string.home_title);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.shirtLayout, ShirtFragment.newInstance())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.pantLayout, PantFragment.newInstance())
                .commit();
    }

    @OnClick(R.id.btn_shuffle)
    public void shuffle(View view) {
        new ShuffleEvent().post();
    }

    @OnClick(R.id.btn_favourite)
    public void favourite(View view) {
        if (!isFavourite) {
            if (ID_S != 0 && ID_P != 0) {
                try {
                    insertFavouriteItem(ID_S, ID_P);
                } catch (UnsupportedEncodingException e) {

                }
            }
        } else {
            //Todo -- need to delete pair from db
        }
    }

    @Override
    public void onImageChange(String type, long id) {
        if (type != null && id != 0) {
            if (type.equals("shirt")) {
                ID_S = id;
            } else if (type.equals("pant")) {
                ID_P = id;
            }
            if (db != null) {
                Observable<SqlBrite.Query> users = db.createQuery(FavouriteItem.TABLE, FavouriteItem.QUERY_FAVOURITE + "ID_S = " + ID_S + " AND ID_P = " + ID_P);
                users.subscribe(query -> {
                    Cursor cursor = query.run();
                    try {
                        if (cursor.getCount() > 0) {
                            isFavourite = true;
                            favouriteBtn.setBackgroundResource(R.drawable.fav_selected);
                        } else {
                            isFavourite = false;
                            favouriteBtn.setBackgroundResource(R.drawable.fav_unselected);
                        }
                    } catch (Exception e) {
                    } finally {
                        cursor.close();
                    }
                });
            }
        }
    }


    public void insertFavouriteItem(long id_s, long id_p) throws UnsupportedEncodingException {
        db.insert(FavouriteItem.TABLE, new FavouriteItem.Builder()
                .id_s(id_s)
                .id_p(id_p)
                .build());
        favouriteBtn.setBackgroundResource(R.drawable.fav_selected);
        Toast.makeText(getApplicationContext(), "Your combination is saved", Toast.LENGTH_SHORT).show();
    }

}
