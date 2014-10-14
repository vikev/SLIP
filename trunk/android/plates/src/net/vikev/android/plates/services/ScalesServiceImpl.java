package net.vikev.android.plates.services;

import static net.vikev.android.plates.MyApplication.getServerUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotGetItemException;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;
import net.vikev.android.plates.exceptions.PlatesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScalesServiceImpl implements ScalesService {
    HttpService httpService = new HttpServiceImpl();
    ItemsService itemsService = new ItemsServiceImpl();

    public List<Scale> getAllScales() {
        JSONArray jsonScales = getScalesFromWebService();
        List<Scale> scales = parseScalesString(jsonScales);

        return scales;
    }

    private JSONArray getScalesFromWebService() {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(getDataFromWebService());
            JSONArray jsonScales = jsonObject.getJSONArray("scales");

            return jsonScales;
        } catch (JSONException e) {
            // TODO Add log
            e.printStackTrace();

            throw new CouldNotParseJSONException("Couldn't parse scales JSON String. " + e.getMessage());
        }
    }

    private List<Scale> parseScalesString(JSONArray jsonScales) {
        try {
            List<Scale> scales = new ArrayList<>();

            for (int i = 0; i < jsonScales.length(); i++) {
                JSONObject jsonScale;
                jsonScale = (JSONObject) jsonScales.get(i);
                Scale scale = new Scale();
                scale.setId(jsonScale.getString("scale_id"));
                scale.setQuantity((int)jsonScale.getDouble("quantity"));
                scale.setItem(itemsService.transformJsonToItem((JSONObject) jsonScale.get("item")));
                scales.add(scale);
            }

            return scales;
        } catch (JSONException e) {
            // TODO add log
            e.printStackTrace();

            throw new CouldNotParseJSONException("Couldn't parse scale JSON string. " + e.getMessage());
        }
    }

    private String getDataFromWebService() {
        try {
            String url = getServerUrl() + "api/all/";
            String jsonResponse;
            jsonResponse = httpService.httpGET(url);
            return jsonResponse;
        } catch (IOException e) {
            // TODO Add log.
            e.printStackTrace();

            throw new CouldNotReachWebServiceException("Error while trying to get all scales from web service. " + e.getMessage());
        }
    }

    public Scale getScale(String id) {
        throw new PlatesException("");
    }
}
