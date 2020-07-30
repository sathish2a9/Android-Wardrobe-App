package com.sathishkumar.mywardrobe;

import android.app.Application;

import com.sathishkumar.mywardrobe.Database.DbModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/*Created by Sathish 30-07-2020*/

@Module(includes = {DbModule.class})

public class WardrobeModule {

    private final Application application;

    WardrobeModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }
}
