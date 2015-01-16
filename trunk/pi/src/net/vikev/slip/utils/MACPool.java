package net.vikev.slip.utils;

import java.util.List;

public class MACPool {
    private static List<String> macs;

    public static synchronized String getNextMac() {
        while (true) {
            macs = WebClient.getInstance().getMACs();
            
            if (macs.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Not fatal. Continue without waiting.
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        return macs.remove(0);
    }
}
