package hu.david.galacz.areus.demo;

import hu.david.galacz.areus.demo.model.CustomerEntity;
import hu.david.galacz.areus.demo.repository.CustomerRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements ApplicationRunner {

   private final CustomerRepository customerRepository;

    public DatabaseSeeder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public void run(ApplicationArguments args) {
        customerRepository.saveAll(
                List.of(
                        new CustomerEntity("Teszt Tamás", "tamas@areus.hu", 15),
                        new CustomerEntity("Teszt Tihamér", "tihamer@areus.hu", 25),
                        new CustomerEntity("Teszt Tóbiás", "tobias@areus.hu", 35),
                        new CustomerEntity( "Teszt Tamara", "tamara@areus.hu", 45),
                        new CustomerEntity( "Teszt Teréz", "reze@areus.hu", 55),
                        new CustomerEntity( "Teszt Timea", "timea@areus.hu", 65)
                )
        );
    }
}
