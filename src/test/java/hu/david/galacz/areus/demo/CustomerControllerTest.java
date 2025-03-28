package hu.david.galacz.areus.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.david.galacz.areus.demo.model.CustomerEntity;
import hu.david.galacz.areus.demo.repository.CustomerRepository;
import hu.david.galacz.areus.demo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private RequestPostProcessor httpBasicAuth;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        httpBasicAuth = httpBasic(username, password);
    }

    @Test
    void testBasicAuthUnauthorized() throws Exception {
        httpBasicAuth = httpBasic("bad-username", "bad-password");
        mockMvc.perform(get("/customer").with(httpBasicAuth))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customer").with(httpBasicAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Teszt Tamás")))
                .andExpect(jsonPath("$[0].email", is("tamas@areus.hu")))
                .andExpect(jsonPath("$[0].age", is(20)))
                .andExpect(jsonPath("$[5].name", is("Teszt Tímea")))
                .andExpect(jsonPath("$[5].email", is("timea@areus.hu")))
                .andExpect(jsonPath("$[5].age", is(65)))
                .andExpect(jsonPath("$.length()", is(6)))
        ;
    }

    @Test
    void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/customer/2").with(httpBasicAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Teszt Tihamér")))
                .andExpect(jsonPath("$.email", is("tihamer@areus.hu")))
                .andExpect(jsonPath("$.age", is(24)))
        ;
    }

    @Test
    void testCreateCustomer() throws Exception {

        CustomerEntity createdCustomer = new CustomerEntity();
        createdCustomer.setName("Teszt Tasziló");
        createdCustomer.setEmail("taszilo@areus.hu");
        createdCustomer.setAge(44);

        mockMvc.perform(post("/customer")
                        .with(httpBasicAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdCustomer))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Teszt Tasziló")))
                .andExpect(jsonPath("$.email", is("taszilo@areus.hu")))
                .andExpect(jsonPath("$.age", is(44)));

        assertEquals(7, customerRepository.findAll().size());
    }

    @Test
    void testCreateCustomerWithExistingId() throws Exception {

        CustomerEntity createdCustomer = new CustomerEntity();
        createdCustomer.setId(1L);
        createdCustomer.setName("Teszt Tasziló");
        createdCustomer.setEmail("taszilo@areus.hu");
        createdCustomer.setAge(44);

        mockMvc.perform(post("/customer")
                        .with(httpBasicAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdCustomer))
                )
                .andExpect(status().isBadRequest());

        assertEquals(6, customerRepository.findAll().size());
    }

    @Test
    void testUpdateCustomer() throws Exception {

        CustomerEntity updatedCustomer = new CustomerEntity();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Módosult Teszt Tamás");
        updatedCustomer.setEmail("modosulttamas@areus.hu");
        updatedCustomer.setAge(20);

        mockMvc.perform(put("/customer/1")
                        .with(httpBasicAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Módosult Teszt Tamás")))
                .andExpect(jsonPath("$.email", is("modosulttamas@areus.hu")))
                .andExpect(jsonPath("$.age", is(20)));

        assertEquals(6, customerRepository.findAll().size());
    }

    @Test
    void testUpdateCustomerWithBadId() throws Exception {

        CustomerEntity updatedCustomer = new CustomerEntity();
        updatedCustomer.setId(5L);
        updatedCustomer.setName("Módosult Teszt Tamás");
        updatedCustomer.setEmail("modosulttamas@areus.hu");
        updatedCustomer.setAge(55);

        mockMvc.perform(put("/customer/1")
                        .with(httpBasicAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isBadRequest());

        assertEquals(6, customerRepository.findAll().size());
    }

    @Test
    void testDeleteCustomer() throws Exception {

        mockMvc.perform(delete("/customer/1").with(httpBasicAuth))
                .andExpect(status().isOk());

        assertTrue(customerRepository.findById(1L).isEmpty());
        assertEquals(5, customerRepository.findAll().size());

        mockMvc.perform(delete("/customer/1").with(httpBasicAuth))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAverageAge() throws Exception {

        mockMvc.perform(get("/customer/average-age").with(httpBasicAuth))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("41.1666666")));
    }

    @Test
    void testGetCustomersBetween18And40() throws Exception {

        mockMvc.perform(get("/customer/between-18-40").with(httpBasicAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Teszt Tamás")))
                .andExpect(jsonPath("$.length()", is(3)))
        ;
    }
}
