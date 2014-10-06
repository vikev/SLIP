package net.vikev.android.plates.fragments;

import net.vikev.android.plates.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.server_settings_fragment, container, false);
        System.out.println("Holy shit!");
        return mainView;
    }
}
