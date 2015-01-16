package net.vikev.slip.ble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.vikev.slip.utils.MACPool;
import net.vikev.slip.utils.WebClient;

public class ScaleMonitor implements Runnable {
    /**
     * How long to wait for result before restarting the process. The time is in
     * seconds.
     */
    private static final int WAIT_FOR_HANDLE_TIMEOUT = 15;
    private ProcessBuilder builder;
    private Process process;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader reader;
    private WebClient webClient = WebClient.getInstance();
    private int pid;
    private String mac;
    
    public ScaleMonitor() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            mac = MACPool.getNextMac();
            readScale(mac);
            trySleep(1000);
        }
    }

    private void readScale(String mac) {
        try {
            startGatttoolProcess(mac);
            trySleep(1500);
            getPID();
            setStreamAndReader();
            getReadingAndSendToWebAPIWithTimeout();
            closeProccess();
        } catch (Exception e) {
            // nothing; continue to next scale
            e.printStackTrace();
        }
    }

    private void setStreamAndReader() {
        is = process.getInputStream();
        isr = new InputStreamReader(is);
        reader = new BufferedReader(isr);
    }

    private void startGatttoolProcess(String mac) throws IOException {
        builder = new ProcessBuilder(new String[] { "gatttool", "-t", "random", "-b", mac, "--char-write-req", "--handle=0x000c",
                "--value=0100", "--listen" });
        builder.redirectErrorStream(true);
        process = builder.start();
    }

    private void getPID() {
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            pid = f.getInt(process);
        } catch (Throwable e) {
            System.out.println("Couldn't get pid.");
        }
    }

    private void trySleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // just continue
        }
    }

    private void getReadingAndSendToWebAPIWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();

        Callable<Short> task = new Callable<Short>() {
            public Short call() {
                try {
                    return readResponse();
                } catch (Exception e) {
                    return 0;
                }
            }
        };

        Future<Short> future = executor.submit(task);

        future.get(WAIT_FOR_HANDLE_TIMEOUT, TimeUnit.SECONDS);

    }

    private short readResponse() throws Exception {
        String line;
        short value = 0;
        while (true) {
            if ((line = reader.readLine()) != null) {
                if (line.startsWith("Notification handle")) {
                    value = extractSensorValueFromNotificationHandleValue(line);
                    updateReading(value);
                    
                    return value;
                }
            }
        }
    }

    private void updateReading(short value) {
        System.out.println("Sending "+value);
        webClient.put(mac, value);
    }

    public void closeProccess() {
        try {
            Runtime.getRuntime().exec("kill -2 " + pid);
        } catch (Exception e) {
            // close forcefully if SIGINT can't be sent for some reason
            e.printStackTrace();
            process.destroy();
        }
    }

    private short extractSensorValueFromNotificationHandleValue(String line) {
        return (short) (Short.valueOf(line.substring(39, 41), 16) * 10);
    }

}
