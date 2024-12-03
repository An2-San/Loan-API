package loan.api.credit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class LoanDto {

    private String customerId;
    private BigDecimal loanAmount;
    private Float interestRate;
    private Integer numberOfInstallments;
}
