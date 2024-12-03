package loan.api.credit.repository;

import loan.api.credit.model.dbEntity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    @Query("SELECT L FROM Loan L WHERE L.customerId = :customerId and (:isPaid IS NULL OR L.isPaid = :isPaid) AND (:numberOfInstallment IS NULL OR L.numberOfInstallment = :numberOfInstallment)")
    List<Loan> findByCustomerIdAndIsPaidAndNumberOfInstallment(@Param("customerId") String customerId,@Param("isPaid") Boolean isPaid,@Param("numberOfInstallment") Integer numberOfInstallment);

}
