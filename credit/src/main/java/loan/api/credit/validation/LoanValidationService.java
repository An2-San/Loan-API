package loan.api.credit.validation;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.LoanRequestDto;
import loan.api.credit.util.LoanUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static loan.api.credit.util.LoanUtil.EXCEPTION_MESSAGES.*;

@Service
public class LoanValidationService {

    public void validateLoanDto(LoanRequestDto loanRequestDto, Optional<Customer> customerOptional) {
        // Check if the customer exists.
        if (customerOptional.isEmpty()) {
            throwValidationException(CUSTOMER_NOT_FOUND);
        }
        Customer customer = customerOptional.get();

        BigDecimal remainingCreditLimit = customer.getRemainingCreditLimit();

        // Check if the customer has enough credit limit
        if (remainingCreditLimit.compareTo(loanRequestDto.getCalculatedLoanAmountWithInterest()) < 0) {
            throwValidationException(LOAN_INVALID_CREDIT_LIMIT);
        }

        LoanUtil.NUMBER_OF_INSTALLMENT[] numberOfInstallments = LoanUtil.NUMBER_OF_INSTALLMENT.values();

        // Check if number of installment is valid
        if (Arrays.stream(numberOfInstallments).
                noneMatch(numberOfInstallment -> numberOfInstallment.getValue().equals(loanRequestDto.getNumberOfInstallments()))) {
            throwValidationException(LOAN_INVALID_INSTALLMENT_NUMBER);
        }

        // Check if interest rate is valid
        if (loanRequestDto.getInterestRate() < 0.1 || loanRequestDto.getInterestRate() > 0.5) {
            throwValidationException(LOAN_INVALID_INTEREST_RATE);
        }

    }

    private void throwValidationException(LoanUtil.EXCEPTION_MESSAGES exceptionMessage) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exceptionMessage.getExceptionMessage());
    }

}
