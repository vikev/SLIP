package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.getEditTextValue;
import static net.vikev.android.plates.MyApplication.getPassword;
import static net.vikev.android.plates.MyApplication.getServerUrl;
import static net.vikev.android.plates.MyApplication.getUsername;
import static net.vikev.android.plates.MyApplication.setEditTextValue;
import static net.vikev.android.plates.MyApplication.setPassword;
import static net.vikev.android.plates.MyApplication.setServerUrl;
import static net.vikev.android.plates.MyApplication.setUsername;
import static net.vikev.android.plates.MyApplication.toastShort;
import static net.vikev.android.plates.MyApplication.setUpdateInterval;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.fragments.SingleScaleFragment.SendItem;
import net.vikev.android.plates.services.ItemsService;
import net.vikev.android.plates.services.ItemsServiceImpl;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class NewScaleAddFragment extends Fragment {
    private View mainView;
    private ScalesService scalesService = new ScalesServiceImpl();
  

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_scale_add, container, false);
       

     
        setSaveButtonClickListener();

        return mainView;
    }


  

 
 
    private void setSaveButtonClickListener() {
        Button updateBtn = (Button) mainView.findViewById(R.id.btn_save_scale);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	new SendItem().execute();
                toastShort("Scale created");
            }
        });
    }
    public class SendItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
        
            scalesService.sendNewScale(getEditTextValue(mainView, R.id.editText_scaleName), getEditTextValue(mainView, R.id.editText_scaleMac));
            return null;
        }
    }

  
}
