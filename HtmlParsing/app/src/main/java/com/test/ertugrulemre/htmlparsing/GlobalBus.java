package com.test.ertugrulemre.htmlparsing;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mycroft on 2.2.2018.
 */

public class GlobalBus {

    private static EventBus eventBus;

    public static EventBus getBus() {
        if (eventBus == null)
            eventBus = EventBus.getDefault();
        return eventBus;
    }
}
