package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.setEditTextValue;

import java.util.ArrayList;
import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.fragments.TestFragment.GetItem;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
    private MainFragment curfrag=this;
    ScalesService scalesService = new ScalesServiceImpl();
    private Handler mHandler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        scaleView = (ListView) mainView.findViewById(R.id.list);

        adapter = new CustomListAdapter(this.getActivity(), scales);
        scaleView.setAdapter(adapter);
        setClickListener();
   
    	 new Thread(new Runnable() {
             @Override
             public void run() {
                 // TODO Auto-generated method stub
                 while (true) {
                     try {
                         Thread.sleep(1000);
                         mHandler.post(new Runnable() {

                             @Override
                             public void run() {
                             
                             	
                             	new GetItem().execute(mainView);
                         
                             	
                             	
                        
                                 
                             }
                         });
                     } catch (Exception e) {
                         // TODO: handle exception
                     }
                 }
             }
         	}).start();
        
        
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
    public void refreshData(List<Scale> scaleData) {
 	   scales = new ArrayList<Scale>(scaleData);
 	  adapter = new CustomListAdapter(this.getActivity(), scales);
 	   scaleView.invalidateViews();
 	   scaleView.setAdapter(adapter);
 	

    }
    public class GetItem extends AsyncTask<View, Void, List<Scale>> {

        @Override
        protected List<Scale> doInBackground(View... params) {
            return scalesService.getAllScales();
        }

        protected void onPostExecute(List<Scale> scales) {
            
            curfrag.refreshData(scales);
        }
    }
}