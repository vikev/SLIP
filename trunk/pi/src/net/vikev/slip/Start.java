package net.vikev.slip;

import java.util.Map;

import net.vikev.slip.ble.LEScanner;

public class Start {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");
        Thread lescan = new Thread(LEScanner.getInstance());
        lescan.setDaemon(true);
        lescan.start();

        System.out.println("Devices");

        while (true) {
            Map<String, String> devices = LEScanner.getInstance().getDevices();
            for (String mac : devices.keySet()) {
                System.out.println(mac + " " + devices.get(mac));
            }
            
            Thread.sleep(1000);
        }
    }
}
