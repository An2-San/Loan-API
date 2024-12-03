package loan.api.credit.model.dbEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "CUSTOMER" )
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("Generated id for customer")
    @Column(name = "id",unique = true,nullable = false,length = 36)
    private String id;

    @Comment("Customer name")
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    @Comment("Customer surname")
    @Column(name = "surname",nullable = false,length = 50)
    private String surname;

    @Comment("Customers credit limit")
    @Column(name = "creditLimit",nullable = false)
    private BigDecimal creditLimit;

    @Comment("Customers used credit")
    @Column(name = "usedCreditLimit")
    private BigDecimal usedCreditLimit;

    @Transient
    public BigDecimal getRemainingCreditLimit(){
        return creditLimit.subtract(usedCreditLimit == null ? BigDecimal.ZERO : usedCreditLimit);
    }

    @Transient
    public void updateRemainingCreditLimit(BigDecimal loanAmount){
        usedCreditLimit = usedCreditLimit == null ? loanAmount : usedCreditLimit.add(loanAmount);
    }

}
