package net.vikev.android.plates.services;

import static net.vikev.android.plates.MyApplication.*;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.exceptions.CouldNotGetItemException;

public class ItemsServiceImpl implements ItemsService {
    HttpService httpService = new HttpServiceImpl();

    public Item getItem(String id) {
        try {
            String url = getServerUrl() + "item/" + id;
            String jsonItem = httpService.httpGET(url);
            
            JSONObject jsonObject = new JSONObject(jsonItem);
            
            Item item = new Item();
            item.setName(jsonObject.getString("name"));
            item.setImage_location(jsonObject.getString("img"));
            
            return item;
        } catch (IOException | JSONException e) {
            // TODO add logging
            e.printStackTrace();
            
            throw new CouldNotGetItemException();
        }
    }

    public Item addItem() {
        // TODO Auto-generated method stub
        return null;
    }
}
