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
import java.util.List;

@Entity
@Table(name = "LOAN")
@Getter
@Setter
@NoArgsConstructor
public class Loan {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("Generated id for loan")
    @Column(name = "id", unique = true, nullable = false, length = 36)
    private String id;

    @Comment("Customer id for loan")
    @Column(name = "customerId", nullable = false, length = 36)
    private String customerId;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "loan")
    private List<LoanInstallment> loanInstallmentList;

    @Comment("Loan amount")
    @Column(name = "loanAmount", nullable = false)
    private BigDecimal loanAmount;

    @Comment("Number of installments")
    @Column(name = "numberOfInstallment")
    private Integer numberOfInstallment;

    @Comment("Create date")
    @Column(name = "createDate", nullable = false)
    private ZonedDateTime createDate;

    @Comment("Is paid , true->paid , false -> not paid")
    @Column(name = "isPaid", nullable = false)
    private Boolean isPaid;

}
