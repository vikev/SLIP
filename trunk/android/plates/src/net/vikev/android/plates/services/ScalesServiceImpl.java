package net.vikev.android.plates.services;

import static net.vikev.android.plates.MyApplication.getServerUrl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.vikev.android.plates.entities.Scale;
import net.vikev.android.plates.exceptions.CouldNotParseJSONException;
import net.vikev.android.plates.exceptions.CouldNotReachWebServiceException;
import net.vikev.android.plates.exceptions.PlatesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScalesServiceImpl implements ScalesService {
    HttpService httpService = new HttpServiceImpl();
    ItemsService itemsService = new ItemsServiceImpl();

    public List<Scale> getAllScales() throws CouldNotParseJSONException, CouldNotReachWebServiceException {

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
                try {
                    scale.setName(jsonScale.getString("name"));
                } catch (Exception e) {
                    scale.setQuantity(0);
                }
                try {
                    scale.setQuantity((int) jsonScale.getDouble("quantity"));
                } catch (Exception e) {
                    scale.setQuantity(0);
                }
                
                try {
                    scale.setItem(itemsService.transformJsonToItem((JSONObject) jsonScale.get("item")));
                } catch (Exception e) {
                    // no item
                }
                
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
        } catch (IOException | IllegalStateException | URISyntaxException e) {
            // TODO Add log.
            e.printStackTrace();

            throw new CouldNotReachWebServiceException("Error while trying to get all scales from web service. " + e.getMessage());
        }
    }


    public void sendNewScale(String name, String mac) {
       
            String url = getServerUrl() + "api/add_scale/?scale_name=" + name + "&scale_mac=" + mac;
            try {
            httpService.httpGET(url);
            } catch (IOException | IllegalStateException | URISyntaxException e) {
                // TODO Add log.
                e.printStackTrace();

                throw new CouldNotReachWebServiceException("Error while trying to create a new scale. " + e.getMessage());
            }
    }

    public void sendScaleData(String ID, String name, String mass, String barcode) {

        try {
            String url = getServerUrl() + "api/set_item/?scale_id=" + URLEncoder.encode(ID,"UTF-8") + "&item_name=" + URLEncoder.encode(name,"UTF-8") + "&item_mass=" + URLEncoder.encode(mass,"UTF-8")+"&barcode="+URLEncoder.encode(barcode,"UTF-8");

            httpService.httpGET(url);

        } catch (IOException | IllegalStateException | URISyntaxException e) {
            // TODO Add log.
            e.printStackTrace();

            throw new CouldNotReachWebServiceException("Error while trying to get all scales from web service. " + e.getMessage());
        }
    }

    public void sendToWebService(String barcode) {
        try {
            String url = getServerUrl() + "api/put/?barcode=" + URLEncoder.encode(barcode,"UTF-8");
            httpService.httpGET(url);

        } catch (IOException | IllegalStateException | URISyntaxException e) {
            // TODO Add log.
            e.printStackTrace();

            throw new CouldNotReachWebServiceException("Error while trying to get all scales from web service. " + e.getMessage());
        }
    }

    public Scale getScale(String id) {
        throw new PlatesException("");
    }
}
