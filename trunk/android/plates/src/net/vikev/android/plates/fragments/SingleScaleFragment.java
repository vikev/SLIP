package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.*;
import net.vikev.android.plates.MyApplication;
import net.vikev.android.plates.R;
import net.vikev.android.plates.entities.Scale;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SingleScaleFragment extends Fragment {
    private View mainView;
    private Scale scale;
  
    public SingleScaleFragment(Scale scale)
    {
    	this.scale=scale;
    	
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mainView = inflater.inflate(R.layout.single_fragment, container, false);

    	System.out.println (scale.getId());
    	
    	try{
    	setEditTextValue(mainView,R.id.ScaleIDEdit,scale.getId());
    	}
    	catch(Exception e){}
    	try{
        setEditTextValue(mainView,R.id.IDEdit,scale.getItem().getId());
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
    Button updateBtn = (Button) mainView.findViewById(R.id.btn_save);
    updateBtn.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
           // saveSettings();
            toastShort("Settings saved");
        }
    });
}

private void saveSettings() {
    //record somewhere updated stuff.
}
}
