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

import net.vikev.slip.utils.WebClient;
import net.vikev.slip.utils.WebClientImpl;

public class ScaleMonitor implements Runnable {
    /**
     * How long to wait for result before restarting the process. The time is in
     * seconds.
     */
    private static final int WAIT_FOR_HANDLE_TIMEOUT = 15;
    private String mac;
    private ProcessBuilder builder;
    private Process p;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader reader;
    private WebClient webClient = WebClientImpl.getInstance();
    private boolean run = true;
    private int pid;

    public ScaleMonitor(String mac) {

        this.mac = mac;
        builder = new ProcessBuilder(new String[] { "gatttool", "-t", "random", "-b", mac, "--char-write-req", "--handle=0x000c",
                "--value=0100", "--listen" });
        builder.redirectErrorStream(true);

        new Thread(this).start();
    }

    public void terminate() {
        run = false;
    }

    private void startProccess() {
        try {
            p = builder.start();
            Thread.sleep(1500);
            try {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid= f.getInt(p);
                System.out.println(mac + " gatttool pid "+pid);
            } catch (Throwable e) {
                System.out.println("Couldn't get pid.");
            }
            is = p.getInputStream();
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            System.out.println(mac + " New gatttool proccess started.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(mac + " Couldn't start heart rate monitor.");
        }
    }

    @Override
    public void run() {
        short prev = -100;
        startProccess();

        while (run) {
            try {
                if (!isRunning(p)) {
                    restart();
                }

                prev = waitForResultWithTimeout(prev);
            } catch (Exception e) {
                e.printStackTrace();
                restart();
                System.out.println(mac + " Connection timed out.");
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
                } catch (Exception e) {
                    System.out.println(mac + " Timed out.");

                    return p;
                }
            }
        };

        Future<Short> future = executor.submit(task);

        return future.get(WAIT_FOR_HANDLE_TIMEOUT, TimeUnit.SECONDS);

    }

    private short readLine(short prev) throws Exception {
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
                throw new Exception("Unexpected value.");
            }
        }

        return prev;
    }

    private void updateServerIfLastTwoReadingsDiffer(short prev, short value) {
        if (prev != value) {
            webClient.put(mac, value);
        }
    }

    private void restart() {
        System.out.println(mac + " Connection error. Restarting...");
        System.out.println(mac + " Killing gatttool proccess.");
        killProccess();
        trySleep(10000);
        startProccess();
    }

    public String getMac() {
        return mac;
    }

    public void killProccess() {
        try {
            Runtime.getRuntime().exec("kill -2 "+pid);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(mac+" Couldn't close gatttool. Destroying istead.");
            p.destroy();
        }
    }

    private void trySleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // at least tried...
        }
    }

    private short extractSensorValueFromNotificationHandleValue(String line) {
        return (short) (Short.valueOf(line.substring(39, 41), 16) * 10);
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
