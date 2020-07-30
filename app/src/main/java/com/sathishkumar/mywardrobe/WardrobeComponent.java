package com.sathishkumar.mywardrobe;

import com.sathishkumar.mywardrobe.Activities.Home;
import com.sathishkumar.mywardrobe.Fragments.PantFragment;
import com.sathishkumar.mywardrobe.Fragments.ShirtFragment;

import javax.inject.Singleton;

import dagger.Component;

/*Created by Sathish 30-07-2020*/

@Singleton
@Component(modules = WardrobeModule.class)
public interface WardrobeComponent {
    void inject(ShirtFragment fragment);

    void inject(PantFragment fragment);

    void inject(Home home);
}
