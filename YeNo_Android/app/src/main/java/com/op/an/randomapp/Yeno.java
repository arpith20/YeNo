package com.op.an.randomapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;


/**
 * Created by m on 7/19/2015.
 */
public class Yeno extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "9Ok45IFHjdoj9Uw6XDGXhGiKunOP4qcCsHktvDfm", "eO5E7if9BkM9vg85fUoZhJeTl23pikUd8sCpbZZz");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Log.d("case","this seems to have worked");
    }
}

