package loan.api.credit.repository;


import loan.api.credit.model.dbEntity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // TODO : Add some functions
}
