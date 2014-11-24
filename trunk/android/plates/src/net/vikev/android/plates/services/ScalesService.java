package net.vikev.android.plates.services;

import java.util.List;

import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;

public interface ScalesService {
    List<Scale> getAllScales() throws CouldNotParseJSONException, CouldNotReachWebServiceException;
    void sendToWebService(String barcode);
    Scale getScale(String id);
<<<<<<< HEAD
    void sendScaleData(String ID, String name, String mass);
    void sendNewScale(String name,String mac);
=======
    void sendScaleData(String ID, String name, String mass, String barcode);
>>>>>>> 23c313db7f4a229e86ec1d2261a7c1af2e672e97
}
