package net.vikev.android.plates.fragments;

import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScanningFragment extends Fragment {
    private View mainView;
    private ScalesService scalesService = new ScalesServiceImpl();
    private ScanningFragment curFrag;
    private TextView formatTxt, contentTxt;
    private Handler mHandler = new Handler();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.reader_fragment, container, false);
        setSendButtonClickListener();
        formatTxt = (TextView) mainView.findViewById(R.id.scan_format);
        contentTxt = (TextView) mainView.findViewById(R.id.scan_content);
        ((EditText) mainView.findViewById(R.id.barcodeInput)).addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MyApplication.toastShort("onTextChanged "+s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MyApplication.toastShort("beforeTextChanged "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                MyApplication.toastShort("afterTextChanged "+s);
            }
        });

        setScanButtonClickListener();
        curFrag = this;

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
                IntentIntegrator scanIntegrator = new IntentIntegrator(curFrag.getActivity());
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
        }
    }

}
