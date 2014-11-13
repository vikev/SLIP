package net.vikev.slip.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebClientImpl implements WebClient {
    private static WebClientImpl webClient;
    private static String USER_AGENT = "BLEMonitor";
    private static String URL = "http://localhost:8000/";

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
        // String url = URL + "/api/mac/" + deviceMac + "/?reading=" + value;
        // try {
        // new URL(url).openConnection();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    @Override
    public List<String> getMACs() {
        // TODO Auto-generated method stub
        List<String> macs = new ArrayList<>();
        macs.add("DA:0F:85:E2:57:19");
        macs.add("C9:9A:51:A3:C3:D2");

        return macs;
    }

    private void sendGet(String mac, Short value) throws Exception {
        String url = URL + "api/mac/" + mac + "/?reading=" + value;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        // con.setRequestProperty("mac", mac);
        // con.setRequestProperty("value", value);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + URL);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        // print result
        // System.out.println(response.toString());

    }
}
