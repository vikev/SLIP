package net.vikev.slip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.vikev.slip.ble.HearthRateMonitor;
import net.vikev.slip.utils.WebClient;
import net.vikev.slip.utils.WebClientImpl;

public class Monitor {
    private WebClient webClient = WebClientImpl.getInstance();

    public void start() {
        for (String mac : webClient.getMACs()) {
            System.out.println("Starting connection to " + mac);
            new HearthRateMonitor(mac);
        }
    }

    public static void main(String args[]) {
        new Monitor().start();
    }
}
