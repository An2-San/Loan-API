package loan.api.credit.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRequestDto {

    private String name;
    private String surname;
    private BigDecimal creditLimit;
}
