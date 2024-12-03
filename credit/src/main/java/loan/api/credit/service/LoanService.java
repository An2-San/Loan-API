package loan.api.credit.service;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dto.LoanRequestDto;
import loan.api.credit.model.dto.LoanResponseDto;
import loan.api.credit.repository.CustomerRepository;
import loan.api.credit.repository.LoanRepository;
import loan.api.credit.validation.LoanValidationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanValidationService loanValidationService;
    private final ConversionService conversionService;
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoanService(LoanValidationService loanValidationService, ConversionService conversionService, LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.conversionService = conversionService;
        this.loanValidationService = loanValidationService;
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    public void createLoan(LoanRequestDto loanRequestDto) {

        Optional<Customer> customerOptional = customerRepository.findById(loanRequestDto.getCustomerId());
        loanValidationService.validateLoanDto(loanRequestDto,customerOptional);

        Loan loan = conversionService.convert(loanRequestDto, Loan.class);
        assert loan != null;
        // Update loan
        loanRepository.save(loan);
        // Update customer's remaining credit limit
        Customer customer = customerOptional.get();
        customer.updateRemainingCreditLimit(loan.getLoanAmount());
        customerRepository.save(customer);

    }

    public List<LoanResponseDto> listLoans(String customerId, Boolean isPaid, Integer numberOfInstallment) {

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer does not found");
        }


        List<Loan> loanList = loanRepository.findByCustomerIdAndIsPaidAndNumberOfInstallment(customerId, isPaid, numberOfInstallment);

        List<LoanResponseDto> loanResponseDtoList = new ArrayList<>();
        loanList.forEach(loan -> {
            loanResponseDtoList.add(conversionService.convert(loan, LoanResponseDto.class));
        });

        return loanResponseDtoList;
    }

}
