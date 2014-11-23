package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.setEditTextValue;
import static net.vikev.android.plates.MyApplication.toastShort;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.services.ItemsService;
import net.vikev.android.plates.services.ItemsServiceImpl;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class SingleScaleFragment extends Fragment {
    private View mainView;
    private static Scale scale;
    private ScalesService scalesService = new ScalesServiceImpl();
    private ItemsService itemsService = new ItemsServiceImpl();
    private Fragment curFragment = this;

    public SingleScaleFragment(Scale scale) {
        SingleScaleFragment.scale = scale;
    }

    public SingleScaleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.single_fragment, container, false);
        setScaleSettingsButton();
        System.out.println("thisisname" + scale.getItem().getName());

        setScanButtonClickListener();
        setBarcodeChangeListener();

        try {
            ((TextView) mainView.findViewById(R.id.ScaleID)).setText(scale.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setEditTextValue(mainView, R.id.ItemName, scale.getItem().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setEditTextValue(mainView, R.id.PictureLocationEdit, scale.getItem().getImage_location());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setEditTextValue(mainView, R.id.QuantityEdit, Integer.toString(scale.getItem().getQuantity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mainView;
    }

    class GetAsyncItemByBarcode extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                final Item item = itemsService.fetchByBarcode(params[0]);
                if (!MyApplication.isNullOrEmpty(item.getName())) {
                    curFragment.getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ((EditText) mainView.findViewById(R.id.ItemName)).setText(item.getName());
                        }
                    });
                }

                if (item.getQuantity() != null) {
                    curFragment.getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ((EditText) mainView.findViewById(R.id.QuantityEdit)).setText(item.getQuantity());
                        }
                    });
                }
            } catch (Exception e) {
                // nothing really; This is a best effort service
            }

            return null;
        }

    }

    private GetAsyncItemByBarcode getAsyncItemByBarcode = new GetAsyncItemByBarcode();

    private void setBarcodeChangeListener() {
        ((EditText) mainView.findViewById(R.id.Barcode)).addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAsyncItemByBarcode.cancel(true);
                getAsyncItemByBarcode = new GetAsyncItemByBarcode();
                getAsyncItemByBarcode.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });

    }

    private void setScanButtonClickListener() {
        Button scanBtn = (Button) mainView.findViewById(R.id.button_barcodeScan);
        scanBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(curFragment.getActivity());
                scanIntegrator.initiateScan();
            }
        });
    }

    private void setScaleSettingsButton() {
        Button updateBtn = (Button) mainView.findViewById(R.id.saveScale);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveSettings();
                toastShort("Settings saved");
            }
        });
    }

    private void saveSettings() {
        new SendItem().execute();
    }

    public class SendItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            TextView IDText = (TextView) mainView.findViewById(R.id.ScaleID);
            String ID = IDText.getText().toString();
            EditText nameText = (EditText) mainView.findViewById(R.id.ItemName);
            String name = nameText.getText().toString();
            EditText quantityText = (EditText) mainView.findViewById(R.id.QuantityEdit);
            String mass = quantityText.getText().toString();
            scalesService.sendScaleData(ID, name, mass);
            return null;
        }
    }
}
