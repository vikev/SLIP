package net.vikev.slip;

import net.vikev.slip.ble.ScaleMonitor;

public class Comms {
    public static int MONITORS_NUMBER;

    public static void main(String[] args) {
        try {
            MONITORS_NUMBER = Integer.parseInt(System.getProperty("monitors"));
        } catch (Exception e) {
            // Property not set. Number defaults to 1.
            e.printStackTrace();
            MONITORS_NUMBER = 1;
        }

        for(int i=0; i<MONITORS_NUMBER; i++){
            new ScaleMonitor();
        }
    }
}
