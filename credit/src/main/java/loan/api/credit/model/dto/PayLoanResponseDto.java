package loan.api.credit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PayLoanResponseDto {

    private Integer numberOfInstallmentsPaid;
    private BigDecimal totalAmountSpent;
    private Boolean isPaidCompletely;
}
