package net.vikev.android.plates.fragments;

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
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private Context context;
    private View mainView;
    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.server_settings_fragment, container, false);
        context = container.getContext();
        pref = mainView.getContext().getSharedPreferences("net.vikev.android.plates", Context.MODE_PRIVATE);

        setFieldValues();
        setSaveButtonClickListener();

        return mainView;
    }

    private void setFieldValues() {
        ((EditText) mainView.findViewById(R.id.editText_server_url)).setText(pref.getString("server_url", ""));
        ((EditText) mainView.findViewById(R.id.editText_username)).setText(pref.getString("username", ""));
        ((EditText) mainView.findViewById(R.id.editText_password)).setText(pref.getString("password", ""));
    }

    private void setSaveButtonClickListener() {
        Button updateBtn = (Button) mainView.findViewById(R.id.btn_save);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openConfirmationDialog();
            }
        });
    }

    private void openConfirmationDialog() {
        saveSettings();
        Toast toast = Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void saveSettings() {
        Editor editor = pref.edit();
        editor.putString("server_url", ((EditText) mainView.findViewById(R.id.editText_server_url)).getText().toString());
        editor.putString("username", ((EditText) mainView.findViewById(R.id.editText_username)).getText().toString());
        editor.putString("password",((EditText) mainView.findViewById(R.id.editText_password)).getText().toString());
        editor.commit();
    }

}
