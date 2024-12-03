package loan.api.credit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LoanResponseDto {

    private String loanId;
    private BigDecimal loanAmount;
    private ZonedDateTime createDate;
    private Integer numberOfInstallments;
    private Boolean isPaid;
}
