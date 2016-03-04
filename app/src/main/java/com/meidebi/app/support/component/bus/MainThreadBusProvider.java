package com.meidebi.app.support.component.bus;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2015/2/23.
 */
public class MainThreadBusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private MainThreadBusProvider() {
        // No instances.
    }
}
