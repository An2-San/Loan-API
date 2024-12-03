package loan.api.credit.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
public class LoanRequestDto {

    private String customerId;
    private BigDecimal loanAmount;
    private Float interestRate;
    private Integer numberOfInstallments;

    @JsonIgnore
    public BigDecimal getCalculatedLoanAmountWithInterest() {
        return BigDecimal.valueOf((1 + interestRate)).multiply(loanAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
