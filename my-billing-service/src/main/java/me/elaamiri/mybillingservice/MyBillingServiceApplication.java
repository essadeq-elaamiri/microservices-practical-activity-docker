package me.elaamiri.mybillingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


// Activating openFeign
@EnableFeignClients
@SpringBootApplication
public class MyBillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBillingServiceApplication.class, args);
    }

}
