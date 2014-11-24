package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.getEditTextValue;
import static net.vikev.android.plates.MyApplication.getPassword;
import static net.vikev.android.plates.MyApplication.getServerUrl;
import static net.vikev.android.plates.MyApplication.getUsername;
import static net.vikev.android.plates.MyApplication.setEditTextValue;
import static net.vikev.android.plates.MyApplication.setPassword;
import static net.vikev.android.plates.MyApplication.setServerUrl;
import static net.vikev.android.plates.MyApplication.setUsername;
import static net.vikev.android.plates.MyApplication.toastShort;
import static net.vikev.android.plates.MyApplication.setUpdateInterval;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private View mainView;
    private SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_server_settings, container, false);
        seekBar = (SeekBar) mainView.findViewById(R.id.seekBar_update_interval);
        seekBar.setOnSeekBarChangeListener(seekBarClickListener);

        setFieldValues();
        setSaveButtonClickListener();

        return mainView;
    }

    private OnSeekBarChangeListener seekBarClickListener = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // nothing
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // nothing
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateIntervalValue(progress);
        }

    };
    private int newInterval;

    private void updateIntervalValue(int progress) {
        TextView textView = (TextView) mainView.findViewById(R.id.textView_update_interval_value);
        switch (progress) {
            case 1:
                newInterval=1;
                textView.setText("1 second");
                break;
            case 2:
                newInterval=5;
                textView.setText("5 seconds");
                break;
            case 3:
                newInterval=10;
                textView.setText("10 seconds");
                break;
            case 4:
                newInterval=30;
                textView.setText("30 seconds");
                break;
            case 5:
                newInterval=60;
                textView.setText("1 minute");
                break;
            case 6:
                newInterval=300;
                textView.setText("5 minutes");
                break;
            case 7:
                newInterval=600;
                textView.setText("10 minutes");
                break;
            case 8:
                newInterval=1800;
                textView.setText("30 minutes");
                break;
            case 9:
                newInterval=3600;
                textView.setText("1 hour");
                break;
            case 10:
                newInterval=86400;
                textView.setText("1 day");
                break;
            case 11:
                newInterval=0;
                textView.setText("Off");
                break; 
            default:
                newInterval=0;
                textView.setText("Off");
        }
    }

    private void setFieldValues() {
        setUpdateIntervalSeekBarPosition();
        setEditTextValue(mainView, R.id.editText_serverurl, getServerUrl());
        setEditTextValue(mainView, R.id.editText_username, getUsername());
        setEditTextValue(mainView, R.id.editText_password, getPassword());
    }

    private void setUpdateIntervalSeekBarPosition() {
        switch (MyApplication.getUpdateInterval()) {
            case 0:
                seekBar.setProgress(11);
                break;
            case 1:
                seekBar.setProgress(1);
                break;
            case 5:
                seekBar.setProgress(2);
                break;
            case 10:
                seekBar.setProgress(3);
                break;
            case 30:
                seekBar.setProgress(4);
                break;
            case 60:
                seekBar.setProgress(5);
                break;
            case 300:
                seekBar.setProgress(6);
                break;
            case 600:
                seekBar.setProgress(7);
                break;
            case 1800:
                seekBar.setProgress(8);
                break;
            case 3600:
                seekBar.setProgress(9);
                break;
            case 86400:
                seekBar.setProgress(10);
                break;
            default:
                seekBar.setProgress(11);
                break;
        }

    }

    private void setSaveButtonClickListener() {
        Button updateBtn = (Button) mainView.findViewById(R.id.btn_save_scale);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveSettings();
                MyApplication.restartWebServerScaleRetrieverService();
                toastShort("Settings saved");
            }
        });
    }

    private void saveSettings() {
        setServerUrl(getEditTextValue(mainView, R.id.editText_serverurl));
        setUsername(getEditTextValue(mainView, R.id.editText_username));
        setPassword(getEditTextValue(mainView, R.id.editText_password));
        setUpdateInterval(newInterval);
    }
}
