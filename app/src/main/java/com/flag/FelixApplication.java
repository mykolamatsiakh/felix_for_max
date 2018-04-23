package com.flag;

import android.app.Application;

public class FelixApplication extends Application {
    private int firstStart = 0;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Gets the default {@link} for this {@link Application}.
     *
     * @return tracker
     */

    public int getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(int firstStart) {
        this.firstStart = firstStart;
    }
}

