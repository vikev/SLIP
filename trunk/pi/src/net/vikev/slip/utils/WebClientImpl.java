package net.vikev.slip.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebClientImpl implements WebClient {
    private static WebClientImpl webClient;
    private static String USER_AGENT = "BLEMonitor";
    private static String URL = "http://localhost:8000/";
    private List<String> macs;

    private WebClientImpl() {
        // no instantiation
    }

    public static WebClientImpl getInstance() {
        if (webClient == null) {
            webClient = new WebClientImpl();
        }

        return webClient;
    }

    @Override
    public void put(String deviceMac, short value) {
        // TODO
        System.out.println(deviceMac + ": " + value);
        try {
            sendGet(deviceMac, value);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getMACs() {
        getAndParseMacsFromWebServer();
        System.out.println(macs);
        
        return this.macs;
    }

    private void getAndParseMacsFromWebServer() {
        try {
            List<String> macs = new ArrayList<>();
            JSONObject serverReply;
            serverReply = new JSONObject(httpGET(URL + "api/allmac/"));
            JSONArray macsArray = serverReply.getJSONArray("addresses");

            for (int i = 0; i < macsArray.length(); i++) {
                macs.add(macsArray.getString(i));
            }

            this.macs = macs;
        } catch (JSONException | IllegalStateException | IOException e) {
            // If can't connect it won't change the values from the previous run
            // so that is not a problem.
        }
    }

    private void sendGet(String mac, Short value) throws Exception {
        String url = URL + "api/mac/" + mac + "/?reading=" + value;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
    }

    public String httpGET(String URL) throws IOException, IllegalStateException {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse response = httpclient.execute(new HttpGet(URL));
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            response.getEntity().getContent().close();

            return out.toString();
        } else {
            // Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }
}
