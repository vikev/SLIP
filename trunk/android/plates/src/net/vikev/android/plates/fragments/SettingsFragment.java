package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.*;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {
    private View mainView;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.server_settings_fragment, container, false);

        setFieldValues();
        setSaveButtonClickListener();

        return mainView;
    }

    private void setFieldValues() {
    
        setEditTextValue(mainView,R.id.editText_username,getUsername());
        setEditTextValue(mainView,R.id.editText_password,getPassword());
    }

    private void setSaveButtonClickListener() {
        Button updateBtn = (Button) mainView.findViewById(R.id.btn_save);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveSettings();
                toastShort("Settings saved");
            }
        });
    }

    private void saveSettings() {
        setServerUrl(getEditTextValue(mainView,R.id.editText_server_url));
        setUsername(getEditTextValue(mainView,R.id.editText_username));
        setPassword(getEditTextValue(mainView,R.id.editText_password));
    }
}
