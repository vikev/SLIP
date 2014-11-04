package net.vikev.slip.ble;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.vikev.slip.utils.StaticUtils;

public class LEScanner implements Runnable {
    private Process p;
    private Map<String, String> devices = new HashMap<String, String>();
    private boolean run = true;
    private static LEScanner scanner;

    private LEScanner() {
        // singleton class
    }

    public static LEScanner getInstance() {
        if (scanner == null) {
            scanner = new LEScanner();
        }

        return scanner;
    }

    /**
     * Continuously scans for LE devices and gets their MACs.
     */
    private void scan() {
        try {
            p = Runtime.getRuntime().exec("hcitool lescan");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while (run && (line = reader.readLine()) != null) {
                System.out.println("RAW: " + line);
                extractAndAddMAC(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            p.destroy();
            run = false;
        }
        System.out.println("LEScan terminated");
        p.destroy();
    }

    private void extractAndAddMAC(String line) {
        String[] split = line.split(" ", 1);

        if (split.length == 2) {
            String mac = split[0];
            String name = split[1];

            if (StaticUtils.isThisMAC(mac)) {
                if (devices.get(mac) == null || "(unknown)".equals(devices.get(mac))) {
                    devices.put(mac, name);
                }
            }

        }
    }

    @Override
    public void run() {
        scan();
    }

    public Map<String, String> getDevices() {
        return devices;
    }
}
