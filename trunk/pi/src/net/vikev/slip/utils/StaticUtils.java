package net.vikev.slip.utils;

public class StaticUtils {
    private StaticUtils() {
        // uninstantiatable class
    }

    public static boolean isThisMAC(String s) {
        return s.matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
    }
}
