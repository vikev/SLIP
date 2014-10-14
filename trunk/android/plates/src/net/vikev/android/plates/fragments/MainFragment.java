package net.vikev.android.plates.fragments;

import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Scale;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainFragment extends Fragment {
    private View mainView;
    private ListView scaleView;
    private List<Scale> scales = MyApplication.scales;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        scaleView = (ListView) mainView.findViewById(R.id.list);

        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.setAdapter(adapter);

        setClickListener();

        return mainView;
    }

    private void setClickListener() {
        scaleView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Scale tobegiven = (Scale) scaleView.getItemAtPosition(position);
                transaction.replace(R.id.content_frame, new SingleScaleFragment(tobegiven)).addToBackStack("tag");
                transaction.commit();
            }
        });

    }

}