package com.appgate.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class StartStopApplication {

    private static Process application;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartStopApplication.class);

    public static void buildApplication() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ProcessBuilder pb = new ProcessBuilder("mvn.cmd", "clean", "package");
        pb.directory(new File("."));
        pb.inheritIO();
        Process process = pb.start();
        CompletableFuture<Process> onExitFuture = process.onExit();
        onExitFuture.get(30, TimeUnit.SECONDS);
    }

    public static Process startApplication() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "target/customer-0.0.1-SNAPSHOT.jar");
        pb.directory(new File("."));
        pb.inheritIO();
        return pb.start();
    }

    public static ExecutorService startApplicationAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                buildApplication();
                application = startApplication();
                CompletableFuture<Process> onExitFuture = application.onExit();
                onExitFuture.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to start the application", e);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return executor;
    }

    public static void stopApplication() {
        if (application != null) {
            application.destroy();
        } else {
            LOGGER.info("No running application process to stop.");
        }
    }
}
