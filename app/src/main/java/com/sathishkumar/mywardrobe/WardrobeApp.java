package com.sathishkumar.mywardrobe;

import android.app.Application;
import android.content.Context;

/*Created by Sathish 30-07-2020*/

public class WardrobeApp extends Application {

    private WardrobeComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerWardrobeComponent.builder().wardrobeModule(new WardrobeModule(this)).build();
    }

    public static WardrobeComponent getComponent(Context context) {
        return ((WardrobeApp) context.getApplicationContext()).component;
    }
}
