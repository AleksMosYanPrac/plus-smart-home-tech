package ru.yandex.practicum.sht.commerce.warehouse.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class AddressConfig {

    @Bean
    Address getAddress(){
         String[] addresses = new String[]{"ADDRESS_1", "ADDRESS_2"};
         String current_address =
                addresses[Random.from(new SecureRandom()).nextInt(0, 1)];
        return new Address(current_address);
    }

    @Value
    public static class Address{
        String address;
    }
}