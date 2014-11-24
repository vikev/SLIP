package net.vikev.android.plates.fragments;

import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotGetScalesException;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import net.vikev.android.plates.services.WebServerScaleRetrieverService;
import android.os.AsyncTask;
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
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        scaleView = (ListView) mainView.findViewById(R.id.list);
        /*if (MainActivity.first)
        {
         Scale temp = new Scale();
         Item tempo = new Item();
         tempo.setName("Milk");
         tempo.setQuantity(100);
         temp.setQuantity(20);
         temp.setName("Fridge Scale Bottom");
         temp.setItem(tempo);
         scales.add(temp);
         temp = new Scale();
         tempo = new Item();
         tempo.setName("Butter");
         tempo.setQuantity(100);
         temp.setQuantity(75);
         temp.setName("Fridge Scale Middle");
         temp.setItem(tempo);
         scales.add(temp);
         temp = new Scale();
         tempo = new Item();
         tempo.setName("Pasta");
         tempo.setQuantity(100);
         temp.setQuantity(50);
         temp.setName("Cupboard Scale");
         temp.setItem(tempo);
         scales.add(temp);
         temp = new Scale();
         tempo = new Item();
         tempo.setName("Currently Empty");
         tempo.setQuantity(100);
         temp.setQuantity(0);
         temp.setName("Fridge Scale Top");
         temp.setItem(tempo);
         scales.add(temp);
         MainActivity.first=false;
        }*/
        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.setAdapter(adapter);
        setClickListener();

        swipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        onRefresh();

        return mainView;
    }

    @Override
    public void onResume() {
        refreshData();
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

    public void refreshData() {
        scales = WebServerScaleRetrieverService.scales;
        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.invalidateViews();
        scaleView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        System.out.println("Refreshing");
        swipeLayout.setRefreshing(true);
        new Update().execute();
    }
    
    class Update extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                WebServerScaleRetrieverService.fetchAndUpdateScales();
            } catch (CouldNotGetScalesException e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        MyApplication.toastShort("Couldn't update. Check your internet connection and settings.");
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshData();
                    swipeLayout.setRefreshing(false);
                }
            }, 1000);
        }
    }
}