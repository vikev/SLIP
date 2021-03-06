package net.vikev.android.plates.services;

import org.json.JSONObject;

import net.vikev.android.plates.entities.Item;

public interface ItemsService {
    Item getItem(String id);
    Item addItem();
    Item transformJsonToItem(JSONObject jsonItem);
    Item fetchByBarcode(String barcode);
}
