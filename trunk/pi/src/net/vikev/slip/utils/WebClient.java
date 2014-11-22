package net.vikev.slip.utils;

import java.util.List;

public interface WebClient {
    /**
     * Send the data to the web server
     */
    void put(String deviceMac, short value);
    
    /**
     * Get the list of devices' MAC addresses from the web server.
     * @return
     */
    List<String> getMACs();
}
