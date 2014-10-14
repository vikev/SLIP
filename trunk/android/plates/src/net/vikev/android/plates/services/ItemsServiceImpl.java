package net.vikev.android.plates.services;

import static net.vikev.android.plates.MyApplication.*;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.vikev.android.plates.entities.Item;
import net.vikev.android.plates.exceptions.CouldNotGetItemException;
import net.vikev.android.plates.exceptions.CouldNotTronsformJsonToItemException;
import net.vikev.android.plates.exceptions.UnsuportedOperationException;

public class ItemsServiceImpl implements ItemsService {
    HttpService httpService = new HttpServiceImpl();

    public Item getItem(String id) {
        throw new UnsuportedOperationException("Not implemented yet!");
        
        // try {
        // String url = getServerUrl() + "item/" + id;
        // String jsonItem = httpService.httpGET(url);
        //
        // JSONObject jsonObject = new JSONObject(jsonItem);
        //
        // Item item = new Item();
        // item.setName(jsonObject.getString("name"));
        // item.setImage_location(jsonObject.getString("img"));
        //
        // return item;
        // } catch (IOException | JSONException e) {
        // // TODO add logging
        // e.printStackTrace();
        //
        // throw new CouldNotGetItemException("Failed to get Item");
        // }
    }

    public Item addItem() {
        // TODO Auto-generated method stub
        throw new UnsuportedOperationException("Not implemented yet!");
    }

    @Override
    public Item transformJsonToItem(JSONObject jsonItem) throws CouldNotTronsformJsonToItemException {
        try {
            Item item = new Item();
            item.setId(jsonItem.getString("item_id"));
            item.setQuantity(jsonItem.getInt("mass"));
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CouldNotTronsformJsonToItemException();
        }
    }
}
