package net.vikev.android.plates.services;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebServerScaleRetrieverService extends Service {
    private static boolean running = false;
    private boolean stop = false;
    private ScalesService scalesService = new ScalesServiceImpl();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void start() {
        WebServerScaleRetrieverService.running = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!stop && MyApplication.getUpdateInterval() > 0) {
                    if (MyApplication.isNetworkAvailable()) {
                        fetchAndUpdateScales();
                    }
                    try {
                        Thread.sleep(MyApplication.getUpdateInterval() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void fetchAndUpdateScales() {
        try {
            MyApplication.updateScales(scalesService.getAllScales());
        } catch (CouldNotParseJSONException | CouldNotReachWebServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        start();
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    public void onDestroy() {
        stop = true;
        super.onDestroy();
    }

}
