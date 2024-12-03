package com.appgate.customer.service;

import com.appgate.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public boolean isCustomerAvailable(String name) {

        var result = customerRepository.findByName(name);
        return result.filter(customer -> customer.isActive() && customer.isPhishingDetection()).isPresent();
    }

}