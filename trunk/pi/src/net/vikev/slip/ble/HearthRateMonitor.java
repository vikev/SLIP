package net.vikev.slip.ble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    // TODO: A lot of error handling!
    @Override
    public void run() {
        short prev = -100;

        while (true) {
            if (!isRunning(p)) {
                restart();
            }

            try {
                prev = waitForResultWithTimeout(prev);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                restart();
            }
        }
    }

    private short waitForResultWithTimeout(short prev) throws InterruptedException, ExecutionException, TimeoutException {
        final short p = prev;
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Short> task = new Callable<Short>() {
            public Short call() {
                try {
                    return readLine(p);
                } catch (IOException e) {
                    restart();

                    return p;
                }
            }
        };

        Future<Short> future = executor.submit(task);
        return future.get(5, TimeUnit.SECONDS);

    }

    private short readLine(short prev) throws IOException {
        String line;
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
        
        return prev;
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
