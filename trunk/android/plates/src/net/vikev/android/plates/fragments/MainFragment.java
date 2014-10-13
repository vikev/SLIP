package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.entities.Scale;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainFragment extends Fragment {
	private View mainView;
	private ListView scaleView;
	private List<Scale> scales = new ArrayList<Scale>();
	private CustomListAdapter adapter;
	Scale temp = new Scale();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		mainView = inflater.inflate(R.layout.fragment_main, container, false);
		scaleView = (ListView) mainView.findViewById(R.id.list);
		
		temp = new Scale();
		temp.setId("2");
		Item tempo = new Item();
		tempo.setId("1");
		tempo.setName("test");
		tempo.setQuantity(25);
		temp.setItem(tempo);
		scales.add(temp);
		temp = new Scale();
		temp.setId("5");
		tempo = new Item();
		tempo.setId("3");
		tempo.setName("test2");
		tempo.setQuantity(55);
		temp.setItem(tempo);
		scales.add(temp);
		adapter = new CustomListAdapter(this.getActivity(), scales);
		scaleView.setAdapter(adapter);
		//

		setClickListener();
		
		return mainView;
	}

	private void setClickListener() {
		scaleView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 android.support.v4.app.FragmentTransaction transaction =
				 getFragmentManager().beginTransaction();
				Scale tobegiven= (Scale) scaleView.getItemAtPosition(position);
				 transaction.replace(R.id.content_frame, new SingleScaleFragment(tobegiven) ).addToBackStack( "tag" );
				 transaction.commit();
				//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TestFragment()).commit();
			}
		});

	}

}