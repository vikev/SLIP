package net.vikev.android.plates;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyApplication extends Application {
    private static Context context;
    private static SharedPreferences pref;
    

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

    public static void setSharedPrefPair(String key, String value){
        Editor editor = MyApplication.pref.edit();
        editor.putString(key, value);
        editor.commit();
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
    
    public static void toastShort(String msg) {
        Toast toast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toastLong(String msg) {
        Toast toast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        toast.show();
    }
    
    public static void setEditTextValue(View view, int editTextId, String text) {
        ((EditText) view.findViewById(editTextId)).setText(text);
    }
    
    public static String getEditTextValue(View view, int editTextId) {
        return ((EditText) view.findViewById(editTextId)).getText().toString();
    }
}