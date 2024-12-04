package loan.api.credit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PayLoanRequestDto {

    private String customerId;
    private String loanId;
    private BigDecimal amount;

}
