package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.*;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.activities.MainActivity;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.fragments.ScanningFragment.SendItem;
import net.vikev.android.plates.services.ScalesService;
import net.vikev.android.plates.services.ScalesServiceImpl;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SingleScaleFragment extends Fragment {
    private View mainView;
    private Scale scale;
    private ScalesService scalesService = new ScalesServiceImpl();
    public SingleScaleFragment(Scale scale)
    {
    	this.scale=scale;
    	
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mainView = inflater.inflate(R.layout.single_fragment, container, false);
    	setScaleSettingsButton();
    	System.out.println ("thisisname" + scale.getItem().getName());
    	
    	try{
    	
    	((TextView) mainView.findViewById(R.id.ScaleID)).setText(scale.getId());
    	}
    	catch(Exception e){}
    	
    	
    	try{
        setEditTextValue(mainView,R.id.ItemName,scale.getItem().getName());
    	}
    	catch(Exception e){}
    	try{
    	setEditTextValue(mainView,R.id.PictureLocationEdit,scale.getItem().getImage_location());
    	}
    	catch(Exception e){}
    	try{
    	setEditTextValue(mainView,R.id.QuantityEdit,Integer.toString(scale.getItem().getQuantity()));
    	}
    	catch(Exception e){}
     //   setScaleSettingsButton();

        return mainView;
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
	}}

}


