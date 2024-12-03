package com.appgate.customer.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private Integer port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.ssl.enabled}")
    private boolean isSSLEnabled;

    @Bean
    public ConnectionFactory connectionFactoryOf() {

        var connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setAutomaticRecoveryEnabled(false);

        if (isSSLEnabled) {
            try {
                var sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                var sslSocketFactory = sslContext.getSocketFactory();
                connectionFactory.setSocketFactory(sslSocketFactory);
            } catch (Exception e) {
                //do not startup application
                throw new RuntimeException(e.getMessage());
            }
        }

        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
