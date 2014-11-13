package net.vikev.android.plates.services;

import java.util.List;

import net.vikev.android.plates.entities.Scale;

public interface ScalesService {
    List<Scale> getAllScales();
    void sendToWebService(String barcode);
    Scale getScale(String id);
    void sendScaleData(String ID, String name, String mass);
}
