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

public class ScaleMonitor implements Runnable {
    /**
     * How long to wait for result before restarting the process. The time is in
     * seconds.
     */
    private static final int WAIT_FOR_HANDLE_TIMEOUT = 5;
    private String mac;
    private ProcessBuilder builder;
    private Process p;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader reader;
    private WebClient webClient = WebClientImpl.getInstance();
    private boolean run = true;

    public ScaleMonitor(String mac) {

        this.mac = mac;
        builder = new ProcessBuilder(new String[] { "./gatttool", "-t", "random", "-b", mac, "--char-write-req", "--handle=0x000c",
                "--value=0100", "--listen" });
        builder.redirectErrorStream(true);
        startProccess();
        new Thread(this).start();
    }

    public void terminate() {
        run = false;
    }

    private void startProccess() {
        try {
            p = builder.start();
            is = p.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            System.out.println(mac + " New gatttool proccess started.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(mac + " Couldn't start heart rate monitor.");
        }
    }

    @Override
    public void run() {
        short prev = -100;

        while (run) {
            try {
                if (!isRunning(p)) {
                    restart();
                }

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
                    System.out.println(mac + " Timed out.");
                    restart();

                    return p;
                }
            }
        };

        Future<Short> future = executor.submit(task);
        return future.get(WAIT_FOR_HANDLE_TIMEOUT, TimeUnit.SECONDS);

    }

    private short readLine(short prev) throws IOException {
        String line;
        if ((line = reader.readLine()) != null) {
            if ("Characteristic value was written successfully".equalsIgnoreCase(line) || line.startsWith("Notification handle")) {
                if (line.startsWith("Notification handle")) {
                    short value = extractSensorValueFromNotificationHandleValue(line);
                    updateServerIfLastTwoReadingsDiffer(prev, value);
                    prev = value;
                }
            } else {
                System.out.println(mac + " Unexpected reading. " + line);
                restart();
            }
        }

        return prev;
    }

    private void updateServerIfLastTwoReadingsDiffer(short prev, short value) {
        if (Math.abs(prev - value) > 5) {
            webClient.put(mac, value);
        }
    }

    private void restart() {
        System.out.println(mac + " Connection error.");
        System.out.println(mac + " Killing gatttool proccess.");
        p.destroy();
        trySleep(1000);
        startProccess();
    }

    private void trySleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // at least tried...
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
