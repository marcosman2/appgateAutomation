package com.appgate.util;
import java.io.File;
import java.io.IOException;

public class DockerContainers {

    private static ProcessBuilder pb;

    public static void startContainersUp() throws IOException, InterruptedException {
        pb = new ProcessBuilder("docker-compose", "up");
        pb.directory(new File("deployment"));
        Process process = pb.start();
        Thread.sleep(5000);
    }

    public static void stopContainers() throws IOException, InterruptedException {
        pb = new ProcessBuilder("docker-compose", "down");
        pb.directory(new File("deployment"));
        Process process = pb.start();
        Thread.sleep(5000);
    }
}
