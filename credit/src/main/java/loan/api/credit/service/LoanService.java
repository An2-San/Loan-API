package loan.api.credit.service;

import jakarta.transaction.Transactional;
import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dbEntity.LoanInstallment;
import loan.api.credit.model.dto.*;
import loan.api.credit.repository.CustomerRepository;
import loan.api.credit.repository.LoanInstallmentRepository;
import loan.api.credit.repository.LoanRepository;
import loan.api.credit.validation.LoanValidationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
public class LoanService {

    private final LoanValidationService loanValidationService;
    private final ConversionService conversionService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final CustomerRepository customerRepository;

    public LoanService(LoanValidationService loanValidationService, ConversionService conversionService,
                       LoanRepository loanRepository, LoanInstallmentRepository loanInstallmentRepository, CustomerRepository customerRepository) {
        this.conversionService = conversionService;
        this.loanValidationService = loanValidationService;
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
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

    public List<LoanInstallmentResponseDto> listLoanInstallments(String loanId, Boolean isPaid) {

        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan does not found");
        }

        List<LoanInstallment> loanInstallmentList = loanOptional.get().getLoanInstallmentList().stream().
                filter(loanInstallment -> isPaid == null || loanInstallment.getIsPaid().equals(isPaid)).collect(Collectors.toList());

        List<LoanInstallmentResponseDto> loanInstallmentResponseDtoList = new ArrayList<>();
        loanInstallmentList.forEach(loanInstallment -> {
            loanInstallmentResponseDtoList.add(conversionService.convert(loanInstallment, LoanInstallmentResponseDto.class));
        });

        return loanInstallmentResponseDtoList;
    }

    @Transactional
    public PayLoanResponseDto payLoan(PayLoanRequestDto payLoanRequestDto) {

        Optional<Loan> loanOptional = loanRepository.findById(payLoanRequestDto.getLoanId());
        if (loanOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan does not found");
        }

        Loan loan = loanOptional.get();
        List<LoanInstallment> loanInstallmentList = loan.getLoanInstallmentList();
        loanInstallmentList.sort((o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate()));

        BigDecimal totalPaidAmount = payLoanRequestDto.getAmount();
        BigDecimal totalLoanAmount = BigDecimal.ZERO;
        BigDecimal amountSpent = BigDecimal.ZERO;
        ZonedDateTime currentDateTime = ZonedDateTime.now();

        List<Month> avaliableMonthList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            avaliableMonthList.add(currentDateTime.getMonth().plus(i));
        }

        for (LoanInstallment loanInstallment : loanInstallmentList) {
            if (avaliableMonthList.contains(loanInstallment.getDueDate().getMonth())
                    && Boolean.FALSE.equals(loanInstallment.getIsPaid())) {

                BigDecimal paidAmount = loanInstallment.getAmount();
                // Bonus 1 If an installment is paid before due date:
                if(currentDateTime.isBefore(loanInstallment.getDueDate())){
                    long numberOfDaysBeforeDueDate = ChronoUnit.DAYS.between(currentDateTime,loanInstallment.getDueDate());
                    BigDecimal discountAmount = calculateBonusAmount(loanInstallment.getAmount(),numberOfDaysBeforeDueDate);
                    paidAmount = paidAmount.subtract(discountAmount);
                }

                // Bonus 2 If an installment is paid after due date:
                else if(currentDateTime.isAfter(loanInstallment.getDueDate())){
                    long numberOfDaysAfterDueDate = ChronoUnit.DAYS.between(loanInstallment.getDueDate(),currentDateTime);
                    BigDecimal penaltyAmount = calculateBonusAmount(loanInstallment.getAmount(),numberOfDaysAfterDueDate);
                    paidAmount = paidAmount.add(penaltyAmount);
                }

                if(totalPaidAmount.compareTo(paidAmount) >= 0){
                    loanInstallment.setPaidAmount(paidAmount);
                    loanInstallment.setIsPaid(true);
                    loanInstallment.setPaymentDate(ZonedDateTime.now());
                    amountSpent = amountSpent.add(paidAmount);
                    totalPaidAmount = totalPaidAmount.subtract(loanInstallment.getAmount());
                    totalLoanAmount = totalLoanAmount.add(loanInstallment.getAmount());
                }
                else{
                    break;
                }

            }
        }


        int numberOfInstallmentsPaid = 0;
        BigDecimal totalAmountSpent = BigDecimal.ZERO;
        for(LoanInstallment loanInstallment : loanInstallmentList){
            if(Boolean.TRUE.equals(loanInstallment.getIsPaid())){
                numberOfInstallmentsPaid++;
            }
            totalAmountSpent = totalAmountSpent.add(loanInstallment.getPaidAmount() == null ? BigDecimal.ZERO : loanInstallment.getPaidAmount());
        }
        Boolean isPaidCompeletly = numberOfInstallmentsPaid == loanInstallmentList.size();
        if(Boolean.TRUE.equals(isPaidCompeletly)){
            loan.setIsPaid(isPaidCompeletly);
        }

        Customer customer = customerRepository.findById(loan.getCustomerId()).get();
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().subtract(totalLoanAmount));

        loanRepository.save(loan);
        customerRepository.save(customer);

        PayLoanResponseDto payLoanResponseDto = new PayLoanResponseDto();
        payLoanResponseDto.setIsPaidCompletely(isPaidCompeletly);
        payLoanResponseDto.setTotalAmountSpent(totalAmountSpent);
        payLoanResponseDto.setNumberOfInstallmentsPaid(numberOfInstallmentsPaid);
        return payLoanResponseDto;
    }


    private BigDecimal calculateBonusAmount(BigDecimal amount,long numberOfDays){
        return amount.multiply(new BigDecimal(0.001)).multiply(new BigDecimal(numberOfDays)).setScale(2,RoundingMode.HALF_UP);
    }

}
