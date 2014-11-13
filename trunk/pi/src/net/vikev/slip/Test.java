package net.vikev.slip;

public class Test {

    public static void main(String[] args) {
        String line = "Notification handle = 0x000b value: 17 0f 01 77 01 78 01 79 01";
        System.out.println(Short.valueOf(line.substring(39,41),16));
    }

}
