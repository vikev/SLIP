package net.vikev.slip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.vikev.slip.ble.ScaleMonitor;
import net.vikev.slip.utils.WebClient;
import net.vikev.slip.utils.WebClientImpl;

public class Monitor {
    private static final int MACS_REFRESH_INTERVAL = 10000;
    private WebClient webClient = WebClientImpl.getInstance();
    private Map<String, ScaleMonitor> monitors = new HashMap<>();
    private List<String> macs;

    public void start() {
        while (true) {
            macs = webClient.getMACs();

            for (String mac : macs) {
                updateMonitor(mac);
            }

            removeObsolateMonitors();

            try {
                Thread.sleep(MACS_REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                // should not happen. Even if it happens it is not a problem.
            }
        }
    }

    private void removeObsolateMonitors() {
        List<String> obsolateMacs = new ArrayList<String>(monitors.keySet());
        obsolateMacs.removeAll(macs);

        for (String mac : obsolateMacs) {
            monitors.remove(mac).terminate();
        }
    }

    private void updateMonitor(String mac) {
        if (!monitors.keySet().contains(mac)) {
            monitors.put(mac, new ScaleMonitor(mac));
        }
    }

    public static void main(String args[]) {
        new Monitor().start();
    }
}
