package com.projects.mychat;

import android.app.Application;

import sdk.chat.app.firebase.ChatSDKFirebase;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ChatSDKFirebase.quickStart(this,"test","11aaaa",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
