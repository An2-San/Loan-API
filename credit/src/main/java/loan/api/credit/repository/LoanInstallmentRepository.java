package loan.api.credit.repository;

import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dbEntity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, String> {

    @Query("SELECT L FROM LoanInstallment L WHERE L.loan = :loan and (:isPaid IS NULL OR L.isPaid = :isPaid)")
    List<LoanInstallment> findByLoanAndIsPaid(@Param("loan") Loan loan, @Param("isPaid") Boolean isPaid);
    
}
