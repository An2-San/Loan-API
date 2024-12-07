package loan.api.credit.validation;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.LoanRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static loan.api.credit.util.LoanUtil.EXCEPTION_MESSAGES.*;

@SpringBootTest
public class LoanValidationTest {

    @Autowired
    private LoanValidationService loanValidationService;

    private LoanRequestDto loanRequestDto;

    private Optional<Customer> customerOptional;

    @BeforeEach
    public void setup() {
        loanRequestDto = new LoanRequestDto();
        loanRequestDto.setLoanAmount(new BigDecimal(300));
        loanRequestDto.setInterestRate(0.2f);
        loanRequestDto.setCustomerId("e7f4d254-2ff5-4e5b-9f42-83591b8b7c57");
        loanRequestDto.setNumberOfInstallments(6);

        Customer customer = new Customer();
        customer.setName("Enes");
        customer.setSurname("Genç");
        customer.setCreditLimit(new BigDecimal(4000));
        customer.setId("e7f4d254-2ff5-4e5b-9f42-83591b8b7c57");
        customerOptional = Optional.of(customer);
    }

    @Test
    public void validateLoanDto() {
        loanValidationService.validateLoanDto(loanRequestDto, customerOptional);
    }

    @Test
    public void validateCustomerException() {
        ResponseStatusException exception = Assertions.assertThrowsExactly(ResponseStatusException.class, () -> loanValidationService.validateLoanDto(loanRequestDto, Optional.empty()));
        Assertions.assertEquals(exception.getReason(), CUSTOMER_NOT_FOUND.getExceptionMessage());
    }

    @Test
    public void validateCreditException() {
        loanRequestDto.setLoanAmount(new BigDecimal(10000));
        ResponseStatusException exception = Assertions.assertThrowsExactly(ResponseStatusException.class, () -> loanValidationService.validateLoanDto(loanRequestDto, customerOptional));
        Assertions.assertEquals(exception.getReason(), LOAN_INVALID_CREDIT_LIMIT.getExceptionMessage());
    }

    @Test
    public void validateNumberOfInstallmentException() {
        loanRequestDto.setNumberOfInstallments(2);
        ResponseStatusException exception = Assertions.assertThrowsExactly(ResponseStatusException.class, () -> loanValidationService.validateLoanDto(loanRequestDto, customerOptional));
        Assertions.assertEquals(exception.getReason(), LOAN_INVALID_INSTALLMENT_NUMBER.getExceptionMessage());
    }

    @Test
    public void validateInterestException() {
        loanRequestDto.setInterestRate(0.6f);
        ResponseStatusException exception = Assertions.assertThrowsExactly(ResponseStatusException.class, () -> loanValidationService.validateLoanDto(loanRequestDto, customerOptional));
        Assertions.assertEquals(exception.getReason(), LOAN_INVALID_INTEREST_RATE.getExceptionMessage());
    }


}
