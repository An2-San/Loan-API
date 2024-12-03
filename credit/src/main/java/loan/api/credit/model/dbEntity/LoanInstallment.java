package loan.api.credit.model.dbEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "LOAN_INSTALLMENT")
@Getter
@Setter
@NoArgsConstructor
public class LoanInstallment {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("Generated id for loan installment")
    @Column(name = "id", unique = true, nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Loan loan;

    @Comment("Loan installment amount")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Comment("Loan installment paid amount")
    @Column(name = "paidAmount")
    private BigDecimal paidAmount;

    @Comment("Loan due date")
    @Column(name = "dueDate", nullable = false)
    private ZonedDateTime dueDate;

    @Comment("Loan payment date")
    @Column(name = "paymentDate")
    private ZonedDateTime paymentDate;

    @Comment("Loan installment is paid , true->paid , false -> not paid")
    @Column(name = "isPaid", nullable = false)
    private Boolean isPaid;
}
