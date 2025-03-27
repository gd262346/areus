package hu.david.galacz.areus.demo.service;

import hu.david.galacz.areus.demo.model.CustomerEntity;
import hu.david.galacz.areus.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public CustomerEntity getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public CustomerEntity createCustomer(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    public CustomerEntity updateCustomer(Long id, CustomerEntity customer) {
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Double getAverageAge() {
        return customerRepository.getAverageAge().orElse(0.0);
    }

    public List<CustomerEntity> getCustomersBetween18And40() {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getAge() >= 18 && customer.getAge() <= 40)
                .toList();
    }
}
