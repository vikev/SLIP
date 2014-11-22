package net.vikev.android.plates.services;

import net.vikev.android.plates.MyApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WebServerScaleRetrieverBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Starting service.");
        MyApplication.startWebServerScaleRetrieverService();       
    }
    
}
