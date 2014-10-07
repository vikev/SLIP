package net.vikev.android.plates.services;

import java.io.IOException;
import java.util.List;

import net.vikev.android.plates.entities.Scale;

public class ScalesServiceImpl implements ScalesService {
    HttpService httpService = new HttpServiceImpl(); 
    
    public List<Scale> getAllScales() {
        
        return null;
    }

    public Scale getScale(String id) {
        // TODO Auto-generated method stub
        return null;
    }
}
