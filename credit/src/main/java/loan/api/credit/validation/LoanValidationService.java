package loan.api.credit.validation;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.LoanRequestDto;
import loan.api.credit.repository.CustomerRepository;
import loan.api.credit.util.LoanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

@Service
public class LoanValidationService {

    @Autowired
    private CustomerRepository customerRepository;

    public void validateLoanDto(LoanRequestDto loanRequestDto, Optional<Customer> customerOptional) {
        if (customerOptional.isEmpty()) {
            throwValidationException("Customer does not found");
        }
        Customer customer = customerOptional.get();

        BigDecimal remainingCreditLimit = customer.getRemainingCreditLimit();

        if (remainingCreditLimit.compareTo(loanRequestDto.getCalculatedLoanAmountWithInterest()) < 0) {
            throwValidationException("Customer does not have enough limit to create loan");
        }

        LoanUtil.NUMBER_OF_INSTALLMENT[] numberOfInstallments = LoanUtil.NUMBER_OF_INSTALLMENT.values();

        if (Arrays.stream(numberOfInstallments).
                noneMatch(numberOfInstallment -> numberOfInstallment.getValue().equals(loanRequestDto.getNumberOfInstallments()))) {
            throwValidationException("Invalid number of installments. Valid options :" + Arrays.toString(EnumSet.allOf(LoanUtil.NUMBER_OF_INSTALLMENT.class).stream().map(LoanUtil.NUMBER_OF_INSTALLMENT::getValue).toArray()));
        }

        if (loanRequestDto.getInterestRate() < 0.1 || loanRequestDto.getInterestRate() > 0.5) {
            throwValidationException("Invalid interest rate. Valid interval : 0.1 - 0.5");
        }

    }

    private void throwValidationException(String message) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

}
