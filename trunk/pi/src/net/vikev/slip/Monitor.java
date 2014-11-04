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
            new HearthRateMonitor(mac);
        }
    }

    public static void main(String args[]) {
        new Monitor().start();

        // try {
        // ProcessBuilder builder = new ProcessBuilder(args);
        // builder.redirectErrorStream(true);
        // Process p = builder.start();
        //
        // InputStream is = p.getInputStream();
        // InputStreamReader isr = new InputStreamReader(is);
        //
        // final BufferedReader reader = new BufferedReader(isr);
        //
        // Thread t = new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // System.out.println("Printing...");
        // String line;
        // while (true) {
        // System.out.flush();
        // try {
        // if ((line = reader.readLine()) != null)
        // System.out.println("OUTPUT: " + line);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // }
        // });
        //
        // t.start();
        //
        // } catch (Throwable t) {
        // t.printStackTrace();
        // }
    }
}
