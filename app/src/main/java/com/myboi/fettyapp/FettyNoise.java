package com.myboi.fettyapp;

import android.content.Context;

/**
 * Created by divy9677 on 3/27/16.
 */
public class FettyNoise implements Playable {

    private String mp3Name;

    public FettyNoise(String mp3Name) {
        this.mp3Name = mp3Name;
    }

    public String getRawResourcePath(Context context) {
        String path = "android.resource://" + context.getPackageName()
                + "/raw/" + this.mp3Name();
        return path;
    }

    public String mp3Name() {
        return this.mp3Name;
    }

}
