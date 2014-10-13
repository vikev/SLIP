package net.vikev.android.plates.fragments;

import static net.vikev.android.plates.MyApplication.setEditTextValue;

import java.util.List;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TestFragment extends Fragment {
    ItemsService itemsService = new ItemsServiceImpl();
    ScalesService scalesService = new ScalesServiceImpl();

    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.testing_fragment, container, false);

        new GetItem().execute(mainView);

        return mainView;
    }

    public class GetItem extends AsyncTask<View, Void, List<Scale>> {

        @Override
        protected List<Scale> doInBackground(View... params) {
            return scalesService.getAllScales();
        }

        protected void onPostExecute(List<Scale> scales) {
            MyApplication.scales = scales;
            String text = "";

            for (Scale scale : scales) {
                text += "Scale id: " + scale.getId() + ", remaining" + scale.getQuantity() + '%' + '\n';
                text += "     Item id: " + scale.getItem().getId() + '\n';
                // text += "     Item name: " + scale.getItem().getName() +
                // '\n';
                text += "     Item quantity: " + scale.getItem().getQuantity() + '\n';
                // text += "     Item image url: " +
                // scale.getItem().getImage_location() + '\n';
            }

            setEditTextValue(mainView, R.id.editText_test, text);
        }
    }
}
