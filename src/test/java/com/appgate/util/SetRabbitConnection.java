package com.appgate.util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SetRabbitConnection {
    private static ConnectionFactory factory = new ConnectionFactory();
    private static Channel channel;
    public static void setRabbitMqConnection(){
        factory.setHost(Config.get("rabbitHost"));
        factory.setPort(Integer.parseInt(Config.get("rabbitPort")));
        factory.setUsername(Config.get("rabbitUser"));
        factory.setPassword(System.getenv("RABBIT_PWD"));
    }

    public static Channel createChannel() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        return channel;
    }
}
