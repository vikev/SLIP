package net.vikev.android.plates.fragments;

import java.util.ArrayList;
import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import net.vikev.android.plates.services.WebServerScaleRetrieverService;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainFragment extends Fragment implements OnRefreshListener {
    private View mainView;
    private ListView scaleView;
    private List<Scale> scales = WebServerScaleRetrieverService.scales;
    private CustomListAdapter adapter;
    ScalesService scalesService = new ScalesServiceImpl();
    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        scaleView = (ListView) mainView.findViewById(R.id.list);

        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.setAdapter(adapter);
        setClickListener();

        swipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        // swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
        // android.R.color.holo_green_light, android.R.color.holo_orange_light,
        // android.R.color.holo_red_light);

        return mainView;
    }

    @Override
    public void onResume() {
        refreshData(WebServerScaleRetrieverService.scales);
        super.onResume();
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

    public void refreshData(List<Scale> scaleData) {
        scales = new ArrayList<Scale>(scaleData);
        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.invalidateViews();
        scaleView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        refreshData(WebServerScaleRetrieverService.scales);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 1000);
    }
}