package net.vikev.slip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.vikev.slip.ble.ScaleMonitor;
import net.vikev.slip.utils.WebClient;
import net.vikev.slip.utils.WebClientImpl;

public class Monitor2 {
    private static final int MACS_REFRESH_INTERVAL = 10000;
    private WebClient webClient = WebClientImpl.getInstance();
    private Map<String, ScaleMonitor> monitors = new HashMap<>();
    private List<String> macs;
    private ProcessBuilder builder;
    private Process process;
    private int pid;
    private InputStreamReader isr;
    private BufferedReader reader;
    private InputStream is;
    private String mac;

    public void start() {
        while (true) {
            try {
                macs = webClient.getMACs();

                for (String mac : macs) {
                    try {
                        this.mac = mac;

                        builder = new ProcessBuilder(new String[] { "gatttool", "-t", "random", "-b", mac, "--char-write-req",
                                "--handle=0x000c", "--value=0100", "--listen" });
                        builder.redirectErrorStream(true);
                        process = builder.start();
                        Thread.sleep(1500);
                        try {
                            Field f = process.getClass().getDeclaredField("pid");
                            f.setAccessible(true);
                            pid = f.getInt(process);
                            System.out.println(mac + " gatttool pid " + pid);
                        } catch (Throwable e) {
                            System.out.println("Couldn't get pid.");
                        }
                        is = process.getInputStream();
                        isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);
                        waitForResultWithTimeout();
                        killProccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                        killProccess();
                    }
                    trySleep(1000);
                }

                trySleep(5000);
            } catch (Exception e) {
                // start again
                e.printStackTrace();
            }
        }
    }

    private void waitForResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();

        Callable<Short> task = new Callable<Short>() {
            public Short call() {
                try {
                    return readLine();
                } catch (Exception e) {
                    System.out.println(mac + " Timed out.");

                    return 0;
                }
            }
        };

        Future<Short> future = executor.submit(task);

        future.get(15, TimeUnit.SECONDS);

    }

    private short readLine() throws Exception {
        String line;
        short value = 0;
        while (true) {
            if ((line = reader.readLine()) != null) {
                if (line.startsWith("Notification handle")) {
                    value = extractSensorValueFromNotificationHandleValue(line);
                    updateServerIfLastTwoReadingsDiffer(value);
                    return value;
                }
            }
        }
    }

    private void updateServerIfLastTwoReadingsDiffer(short value) {
        webClient.put(mac, value);
    }

    public void killProccess() {
        try {
            Runtime.getRuntime().exec("kill -2 " + pid);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(mac + " Couldn't close gatttool. Destroying istead.");
            process.destroy();
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

    public static void main(String args[]) {
        final Monitor2 m = new Monitor2();
        // Runtime.getRuntime().addShutdownHook(new Thread() {
        // public void run() {
        // for (ScaleMonitor sm : m.getMonitors()) {
        // System.out.println("Killing " + sm.getMac());
        // sm.terminate();
        // sm.killProccess();
        // }
        // }
        // });
        m.start();

    }
}
