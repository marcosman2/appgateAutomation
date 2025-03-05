package com.appgate.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

public class FetchResponse extends SetRabbitConnection{

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchResponse.class);

    public static String fetchResponseMessage() throws Exception {

        String message;
        setRabbitMqConnection();

        GetResponse delivery = null;
        try (Channel channel = createChannel()) {

            String queue = Config.get("queue.response");

            int retries = 0;
            delivery = null;
            while (delivery == null && retries < 10) {
                delivery = channel.basicGet(queue, true);
                if (delivery == null) {
                    Thread.sleep(1000);
                    retries++;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching the respond message.", e);
            throw new RuntimeException("Failed to fetch the respond message.", e);
        }
        message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        return message;
    }

    public static String convertResponseToJson(String response) throws JsonProcessingException {
        String stringToJson;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode firstParse = mapper.readTree(response);

        if(firstParse.isTextual()){
            String innerJson = firstParse.asText();
            JsonNode jsonNode = mapper.readTree(innerJson);
            stringToJson = jsonNode.toString();
        }else {
            stringToJson = firstParse.toString();
        }
        return stringToJson;
    }
}
