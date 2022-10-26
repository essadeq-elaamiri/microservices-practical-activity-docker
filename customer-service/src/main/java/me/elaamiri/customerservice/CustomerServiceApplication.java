package me.elaamiri.customerservice;

import me.elaamiri.customerservice.entities.Customer;
import me.elaamiri.customerservice.repositories.CustomerRepository;
import me.elaamiri.customerservice.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository){
		return args -> {
			// Create just 2 customers

			List.of("Ahmed", "Essadeq").forEach( c ->{
				Customer customer = new Customer(UUID.randomUUID().toString(), c, String.format("%s_email@gmail.com", c));
				customerRepository.save(customer);
			});
		};
	}
}
