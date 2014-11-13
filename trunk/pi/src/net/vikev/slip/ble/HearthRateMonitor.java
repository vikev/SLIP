package net.vikev.slip.ble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.vikev.slip.utils.WebClient;
import net.vikev.slip.utils.WebClientImpl;

public class HearthRateMonitor implements Runnable {
    private String mac;
    private ProcessBuilder builder;
    private Process p;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader reader;
    private WebClient webClient = WebClientImpl.getInstance();

    public HearthRateMonitor(String mac) {

        this.mac = mac;
        builder = new ProcessBuilder(new String[] { "./gatttool", "-t", "random", "-b", mac, "--char-write-req", "--handle=0x000c",
                "--value=0100", "--listen" });
        builder.redirectErrorStream(true);
        startProccess();
        new Thread(this).start();
    }

    private void startProccess() {
        try {
            p = builder.start();
            is = p.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            System.out.println("Couldn't start heart rate monitor for " + mac);
        }
    }

    private void restartProccess() {
        p.destroy();
        startProccess();
    }

    // TODO: A lot of error handling!
    @Override
    public void run() {
        String line;
        short prev = -100;

        while (true) {
            if (!isRunning(p)) {
                restart();
            }

            try {
                if ((line = reader.readLine()) != null) {
                    System.out.println(mac + " " + line);
                    if ("Characteristic value was written successfully".equalsIgnoreCase(line) || line.startsWith("Notification handle")) {
                        if (line.startsWith("Notification handle")) {
                            short value = extractSensorValueFromNotificationHandleValue(line);
                            if (Math.abs(prev - value) > 5) {
                                webClient.put(mac, value);
                            }
                            prev = value;
                        }
                    } else {
                        restart();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void restart() {
        System.out.println("Error in connection with " + mac);
        System.out.println("Killing gatttool proccess for " + mac);
        p.destroy();

        trySleep(5000);

        System.out.println("Starting new gatttool proccess for " + mac);
        startProccess();
    }

    private void trySleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private short extractSensorValueFromNotificationHandleValue(String line) {
        return Short.valueOf(line.substring(39, 41), 16);
    }

    private boolean isRunning(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
