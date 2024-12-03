package loan.api.credit.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoanInstallmentResponseDto {

    private BigDecimal amount;
    private BigDecimal paidAmount;
    private ZonedDateTime dueDate;
    private ZonedDateTime paymentDate;
    private Boolean isPaid;
}
