package net.vikev.android.plates;

import net.vikev.android.plates.services.WebServerScaleRetrieverService;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyApplication extends Application {
    private static Context context;
    private static SharedPreferences pref;
    public static long lastPopupTime = 0;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        MyApplication.pref = MyApplication.context.getSharedPreferences("net.vikev.android.plates", Context.MODE_PRIVATE);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static SharedPreferences getPref() {
        return MyApplication.pref;
    }

    public static void setSharedPrefPair(String key, String value) {
        Editor editor = MyApplication.pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setSharedPrefPair(String key, int value) {
        Editor editor = MyApplication.pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * The update interval in seconds.
     * 
     * @return
     */
    public static int getUpdateInterval() {
        return MyApplication.pref.getInt("update_interval", 300);
    }

    /**
     * Set how many seconds should the app wait before it tries to pull/update
     * the scales info from the web server.
     * 
     * @param updateInterval
     *            Time in seconds.
     */
    public static void setUpdateInterval(int updateInterval) {
        MyApplication.setSharedPrefPair("update_interval", updateInterval);
    }

    public static String getServerUrl() {
        return MyApplication.pref.getString("server_url", "");
    }

    public static void setServerUrl(String serverUrl) {
        MyApplication.setSharedPrefPair("server_url", serverUrl);
    }

    public static String getUsername() {
        return MyApplication.pref.getString("username", "");
    }

    public static void setUsername(String username) {
        MyApplication.setSharedPrefPair("username", username);
    }

    public static String getPassword() {
        return pref.getString("password", "");
    }

    public static void setPassword(String password) {
        MyApplication.setSharedPrefPair("password", password);
    }

    public static void setEditTextValue(View view, int editTextId, String text) {
        ((EditText) view.findViewById(editTextId)).setText(text);
    }

    public static String getEditTextValue(View view, int editTextId) {
        return ((EditText) view.findViewById(editTextId)).getText().toString();
    }

    public static void toastShort(String msg) {
        Toast toast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toastLong(String msg) {
        Toast toast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void startWebServerScaleRetrieverService() {
        if (!WebServerScaleRetrieverService.isRunning()) {
            context.startService(new Intent(context, WebServerScaleRetrieverService.class));
        }
    }

    public static void stopWebServerScaleRetrieverService() {
        context.stopService(new Intent(context, WebServerScaleRetrieverService.class));
    }

    public static void restartWebServerScaleRetrieverService() {
        stopWebServerScaleRetrieverService();
        startWebServerScaleRetrieverService();
    }

    public static void showNotification(Context context, Class<? extends Activity> clazz, int id, CharSequence title, CharSequence text) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title).setContentText(text);

        Intent resultIntent = new Intent(context, clazz);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(clazz);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }

    

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
