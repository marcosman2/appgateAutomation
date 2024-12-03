package com.appgate.customer.rabbit;

import com.appgate.customer.dto.CustomerRequest;
import com.appgate.customer.dto.CustomerResponse;
import com.appgate.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CustomerListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.out.customer.exchange}")
    private String exchange;

    @Value("${app.out.customer.response.routing-key}")
    private String responseRoutingKey;


    @RabbitListener(queues = "${app.in.customer.query.queue}")
    public void handleRequest(@Payload CustomerRequest requestMessage) {
        try {

            System.out.println("request message: " + requestMessage.getCustomer());
            var customer = requestMessage.getCustomer();

            var isAvailable = customerService.isCustomerAvailable(customer);

            var response = new CustomerResponse(customer, isAvailable);
            var responseMessage = objectMapper.writeValueAsString(response);

            rabbitTemplate.convertAndSend(exchange, responseRoutingKey, responseMessage);

            System.out.println("response message: " + responseMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}