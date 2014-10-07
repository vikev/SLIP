package net.vikev.android.plates.services;

import net.vikev.android.plates.entities.Item;

public interface ItemsService {
    Item getItem(String id);
    Item addItem();
}
