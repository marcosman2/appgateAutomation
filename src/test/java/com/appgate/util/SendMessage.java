package com.appgate.util;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

public class SendMessage extends SetRabbitConnection{

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessage.class);

    public static void sendMessageToQueue(String customer){

        setRabbitMqConnection();

        try (Channel channel = createChannel()){
            String queue = Config.get("queue.request");
            String message = String.format("{ \"customer\": \"%s\" }", customer);
            channel.basicPublish("", queue, null, message.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(5000);
        }
        catch(Exception e){
            LOGGER.error("Error sending message to the queue.", e);
            throw new RuntimeException("Failed to send message to the queue.", e);
        }
    }
}
