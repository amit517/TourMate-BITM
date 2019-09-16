package com.amitKundu.tourmate;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TourMate extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
