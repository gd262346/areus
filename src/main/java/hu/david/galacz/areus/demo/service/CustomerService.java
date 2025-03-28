package hu.david.galacz.areus.demo.service;

import hu.david.galacz.areus.demo.model.CustomApiResponse;
import hu.david.galacz.areus.demo.model.CustomerEntity;
import hu.david.galacz.areus.demo.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public ResponseEntity<CustomerEntity> getCustomerById(Long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);

        if (customerEntity.isPresent()) {
            return new ResponseEntity<>(customerEntity.get(), HttpStatus.OK);
        }
        throw new CustomApiResponse(HttpStatus.NOT_FOUND, "Customer not found by id!");
    }

    public ResponseEntity<CustomerEntity> createCustomer(CustomerEntity customer) {

        if (Objects.nonNull(customer.getId()) && customerRepository.findById(customer.getId()).isPresent()) {
            throw new CustomApiResponse(HttpStatus.BAD_REQUEST, "Customer cannot create with existing id!");
        }

        CustomerEntity customerEntity = customerRepository.save(customer);
        return new ResponseEntity<>(customerEntity, HttpStatus.CREATED);
    }

    public CustomerEntity updateCustomer(Long id, CustomerEntity customer) {
        Optional<CustomerEntity> customerToUpdateOptional = customerRepository.findById(id);

        if (!id.equals(customer.getId()) || customerToUpdateOptional.isEmpty()) {
            throw new CustomApiResponse(HttpStatus.BAD_REQUEST, "Customer data not eligible!");
        }

        CustomerEntity customerToUpdate = customerToUpdateOptional.get();
        customerToUpdate.setName(customer.getName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setAge(customer.getAge());

        return customerRepository.save(customerToUpdate);
    }

    public void deleteCustomer(Long id) {
        Optional<CustomerEntity> customerToUpdateOptional = customerRepository.findById(id);

        if (customerToUpdateOptional.isEmpty()) {
            throw new CustomApiResponse(HttpStatus.NOT_FOUND, "Customer not found by id!");
        }
        customerRepository.deleteById(id);
    }

    public Double getAverageAge() {
        return Optional.ofNullable(customerRepository.getAverageAge()).orElse(0.0);
    }

    public List<CustomerEntity> getCustomersBetween18And40() {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getAge() >= 18 && customer.getAge() <= 40)
                .toList();
    }
}
