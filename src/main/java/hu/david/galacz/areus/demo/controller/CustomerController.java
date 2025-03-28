package hu.david.galacz.areus.demo.controller;

import hu.david.galacz.areus.demo.model.CustomerEntity;
import hu.david.galacz.areus.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerEntity> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerEntity> create(@Valid @RequestBody CustomerEntity customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public CustomerEntity update(@PathVariable Long id, @Valid @RequestBody CustomerEntity customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @GetMapping("/average-age")
    public Double getAverageAge() {
        return customerService.getAverageAge();
    }

    @GetMapping("/between-18-40")
    public List<CustomerEntity> getCustomersBetween18And40() {
        return customerService.getCustomersBetween18And40();
    }
}
