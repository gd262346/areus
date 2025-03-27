package hu.david.galacz.areus.demo.repository;

import hu.david.galacz.areus.demo.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Query("SELECT COALESCE(AVG(c.age), 0) FROM CustomerEntity c")
    Optional<Double> getAverageAge();
}
