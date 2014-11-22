package net.vikev.android.plates.services;

import java.util.ArrayList;
import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotGetItemException;
import net.vikev.android.plates.exceptions.CouldNotGetScalesException;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebServerScaleRetrieverService extends Service {
    private static boolean running = false;
    private boolean stop = false;
    private static ScalesService scalesService = new ScalesServiceImpl();
    public static List<Scale> scales = new ArrayList<>();

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
                    try {
                        if (MyApplication.isNetworkAvailable()) {
                            fetchAndUpdateScales();
                        }
                        Thread.sleep(MyApplication.getUpdateInterval() * 1000);
                    } catch (InterruptedException | CouldNotGetScalesException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    public static void fetchAndUpdateScales() throws CouldNotGetScalesException {
        try {
            updateScales(scalesService.getAllScales());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CouldNotGetScalesException();
        }
    }

    public static void updateScales(List<Scale> scales) {
        WebServerScaleRetrieverService.scales = scales;
        if (System.currentTimeMillis() - MyApplication.lastPopupTime > 60000) {
            for (Scale scale : scales) {
                if (scale.getItem() != null) {
                    if (scale.isRunningEmpty()) {
                        MyApplication.showNotification(MyApplication.getAppContext(), MainActivity.class, 0, "You are running on fumes.",
                                "It's time to shop!");
                        MyApplication.lastPopupTime = System.currentTimeMillis();
                    }
                }
            }
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
