package net.vikev.android.plates.services;

import java.util.List;

import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;

public interface ScalesService {
    List<Scale> getAllScales() throws CouldNotParseJSONException, CouldNotReachWebServiceException;
    void sendToWebService(String barcode);
    Scale getScale(String id);
   void sendNewScale(String name,String mac);

    void sendScaleData(String ID, String name, String mass, String barcode, int picture_id);

}
