package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.setEditTextValue;
import static net.vikev.android.plates.MyApplication.toastShort;

import java.util.List;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.support.v4.app.Fragment;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanningFragment extends Fragment {
	private View mainView;
	private ScalesService scalesService = new ScalesServiceImpl();
	private ScanningFragment curFrag;
	private TextView formatTxt, contentTxt;
	private Handler mHandler = new Handler();
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.reader_fragment, container, false);
		setSendButtonClickListener();
		formatTxt = (TextView) mainView.findViewById(R.id.scan_format);
		contentTxt = (TextView) mainView.findViewById(R.id.scan_content);
		setScanButtonClickListener();
		curFrag = this;
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

								Update();

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

	public void Update() {

		formatTxt.setText("FORMAT: " + MainActivity.format);
		contentTxt.setText("CONTENT: " + MainActivity.code);

	}

	private void setScanButtonClickListener() {
		Button updateBtn = (Button) mainView.findViewById(R.id.scan_button);
		updateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentIntegrator scanIntegrator = new IntentIntegrator(curFrag
						.getActivity());
				scanIntegrator.initiateScan();
			}
		});
	}
		private void setSendButtonClickListener() {
			Button updateBtn = (Button) mainView.findViewById(R.id.send_button);
			updateBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				 new SendItem().execute();  
				}
	});
				
		
			
	}

		public class SendItem extends AsyncTask<Void, Void, Void> {

	  
	    

			@Override
			protected Void doInBackground(Void... params) {
				scalesService.sendToWebService(MainActivity.code);
				return null;
			}}
		
}
