package com.sathishkumar.mywardrobe.Events;

import org.greenrobot.eventbus.EventBus;

/*Created by Sathish 30-07-2020*/

public class BaseEvent {

    public void post() {
        EventBus.getDefault().post(this);
    }
}