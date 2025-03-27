package hu.david.galacz.areus.demo.repository;

import hu.david.galacz.areus.demo.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Query("SELECT AVG(c.age) FROM CustomerEntity c")
    Double getAverageAge();
}
