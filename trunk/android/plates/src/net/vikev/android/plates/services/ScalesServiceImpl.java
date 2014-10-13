package net.vikev.android.plates.services;

import static net.vikev.android.plates.MyApplication.getServerUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotGetItemException;

public class ScalesServiceImpl implements ScalesService {
    HttpService httpService = new HttpServiceImpl();
    ItemsService itemsService = new ItemsServiceImpl();

    public List<Scale> getAllScales() {
        try {
            List<Scale> scales = new ArrayList<>();
            String url = getServerUrl() + "api/all/";
            String jsonResponse = httpService.httpGET(url);

            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray jsonScales = jsonObject.getJSONArray("scales");

            for (int i = 0; i < jsonScales.length(); i++) {
                JSONObject jsonScale = (JSONObject) jsonScales.get(i);
                Scale scale = new Scale();
                scale.setId(jsonScale.getString("scale_id"));

                JSONObject jsonItem = (JSONObject) jsonScale.get("item");

                Item item = new Item();
                // itemsService.getItem(jsonScale.getString("itemId"));
                item.setId(jsonItem.getString("item_id"));
                item.setQuantity(jsonItem.getInt("mass"));
                // item.setImage_location(jsonItem.getString("img"));
                scale.setItem(item);
                scales.add(scale);
            }

            return scales;
        } catch (IOException | JSONException e) {
            // TODO add logging
            e.printStackTrace();

            throw new CouldNotGetItemException();
        }
    }

    public Scale getScale(String id) {
        // TODO Auto-generated method stub
        return null;
    }
}
