package loan.api.credit.service;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dbEntity.LoanInstallment;
import loan.api.credit.model.dto.LoanRequestDto;
import loan.api.credit.model.dto.PayLoanRequestDto;
import loan.api.credit.model.dto.PayLoanResponseDto;
import loan.api.credit.repository.CustomerRepository;
import loan.api.credit.repository.LoanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LoanServiceTest {

    @MockitoBean
    private LoanRepository loanRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ConversionService conversionService;

    private PayLoanRequestDto payLoanRequestDto;

    private LoanRequestDto loanRequestDto;

    private Customer customer;

    private Loan loan;

    /**
     * Test case:
     * Each loan installment amount : 100
     * Interest rate : 0.2
     * Total loan amount with interest : 600
     * Number of installment : 6
     */
    @BeforeEach
    public void setup() {

        loanRequestDto = new LoanRequestDto();
        loanRequestDto.setNumberOfInstallments(6);
        loanRequestDto.setCustomerId("e7f4d123-2ff5-4555-9f42-83591b8b7c57");
        loanRequestDto.setLoanAmount(new BigDecimal(500));
        loanRequestDto.setInterestRate(0.2f);

        loan = conversionService.convert(loanRequestDto, Loan.class);

        customer = new Customer();
        customer.setName("Enes");
        customer.setSurname("Genç");
        customer.setCreditLimit(new BigDecimal(5000));
        customer.setUsedCreditLimit(loanRequestDto.getCalculatedLoanAmountWithInterest());

        Mockito.when(loanRepository.findById("e7f4d123-2ff5-4555-9f42-83591b8b7c55")).thenReturn(Optional.of(loan));
        Mockito.when(customerRepository.findById(loanRequestDto.getCustomerId())).thenReturn(Optional.of(customer));

        payLoanRequestDto = new PayLoanRequestDto();
        payLoanRequestDto.setCustomerId(loanRequestDto.getCustomerId());
        payLoanRequestDto.setLoanId("e7f4d123-2ff5-4555-9f42-83591b8b7c55");

    }

    /**
     * Pay the first loan installment on due date
     * Payment date is equal to due date so there is no discount or penalty
     */
    @Test
    public void payFirstLoanInstallmentOnDueDate() {
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        payLoanRequestDto.setAmount(firstLoanInstallment.getAmount());
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate();
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        Assertions.assertEquals(1, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));
    }

    /**
     * Pay the first 2 loan installment
     * Second payment will be failed because paidAmount is not enough , Paid Amount : 150.00 , Excepted Amount To Pay 2 Installment : 200.00
     */
    @Test
    public void payFirst2LoanInstallmentOnDueDateFailed() {
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        payLoanRequestDto.setAmount(new BigDecimal(150.00).setScale(2, RoundingMode.HALF_UP));
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate();
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        Assertions.assertEquals(1, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));
    }

    /**
     * Bonus 2: Pay the first loan installment with discount
     */
    @Test
    public void payFirstLoanInstallmentWithDiscount() {
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        payLoanRequestDto.setAmount(firstLoanInstallment.getAmount());
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate().minusDays(1);
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        Assertions.assertEquals(1, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));
        // If there is a discount , paid amount must be lower than loan installment amount
        Assertions.assertEquals(1, firstLoanInstallment.getAmount().compareTo(payLoanResponseDto.getTotalAmountSpent()));
    }

    /**
     * Bonus 2: Pay the first 2 loan installment with discount
     */
    @Test
    public void payFirst2LoanInstallmentsWithDiscount() {
        LoanInstallment loanInstallment1 = loan.getLoanInstallmentList().get(0);
        LoanInstallment loanInstallment2 = loan.getLoanInstallmentList().get(1);
        payLoanRequestDto.setAmount(loanInstallment1.getAmount().add(loanInstallment2.getAmount()));
        ZonedDateTime paymentDate = loanInstallment1.getDueDate().minusDays(1);
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        Assertions.assertEquals(2, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));

        BigDecimal totalLoanInstallmentAmount = loanInstallment1.getAmount().add(loanInstallment2.getAmount());
        // If there is a discount , paid amount must be lower than total loan installment amount
        Assertions.assertEquals(1, totalLoanInstallmentAmount.compareTo(payLoanResponseDto.getTotalAmountSpent()));
    }

    /**
     * Bonus 2: Pay the first loan installment with penalty
     */
    @Test
    public void payFirstLoanInstallmentWithPenaltyFailed() {
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        payLoanRequestDto.setAmount(firstLoanInstallment.getAmount());
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate().plusDays(1);
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        // Cannot pay the loan installment because there will be a penalty and required amount will not be enough .
        // Required amount to pay the loan installment : 100.10 , Payment Amount : 100.00
        Assertions.assertEquals(0, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));

    }

    /**
     * Bonus 2: Pay the first loan installment with penalty
     */
    @Test
    public void payFirstLoanInstallmentWithPenaltySuccess() {
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        payLoanRequestDto.setAmount(new BigDecimal(100.10).setScale(2, RoundingMode.HALF_UP));
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate().plusDays(1);
        PayLoanResponseDto payLoanResponseDto = loanService.payLoan(payLoanRequestDto, paymentDate);
        // Payment will be successful because we added the penalty amount to the paidAmount
        Assertions.assertEquals(1, payLoanResponseDto.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto.getTotalAmountSpent()));

        // If there is a penalty , paid amount must be bigger than total loan installment amount
        Assertions.assertEquals(1, payLoanResponseDto.getTotalAmountSpent().compareTo(firstLoanInstallment.getAmount()));
    }

    /**
     * Pay all loan installments
     */
    @Test
    public void payAllLoanInstallments() {
        payLoanRequestDto.setAmount(new BigDecimal(100000).setScale(2, RoundingMode.HALF_UP));

        // Pay the first 3 months
        LoanInstallment firstLoanInstallment = loan.getLoanInstallmentList().get(0);
        ZonedDateTime paymentDate = firstLoanInstallment.getDueDate();
        PayLoanResponseDto payLoanResponseDto1 = loanService.payLoan(payLoanRequestDto, paymentDate);
        Assertions.assertEquals(3, payLoanResponseDto1.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(false, payLoanResponseDto1.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto1.getTotalAmountSpent()));

        // Pay the last 3 months
        LoanInstallment loanInstallment3 = loan.getLoanInstallmentList().get(3);
        ZonedDateTime paymentDate2 = loanInstallment3.getDueDate();
        PayLoanResponseDto payLoanResponseDto2 = loanService.payLoan(payLoanRequestDto, paymentDate2);
        Assertions.assertEquals(6, payLoanResponseDto2.getNumberOfInstallmentsPaid());
        Assertions.assertEquals(true, payLoanResponseDto2.getIsPaidCompletely());
        Assertions.assertEquals(0, compareTotalAmountSpent(loan.getLoanInstallmentList(), payLoanResponseDto2.getTotalAmountSpent()));


        // Customer used credit limit needs to be set to zero because all loan installments are paid.
        Assertions.assertEquals(0, customer.getUsedCreditLimit().compareTo(BigDecimal.ZERO));
        Assertions.assertEquals(true, loan.getIsPaid());
    }


    private int compareTotalAmountSpent(List<LoanInstallment> loanInstallmentList, BigDecimal totalAmountSpent) {
        BigDecimal totalPaidAmount = calculateTotalPaidAmount(loanInstallmentList);
        return totalAmountSpent.compareTo(totalPaidAmount);

    }

    private BigDecimal calculateTotalPaidAmount(List<LoanInstallment> loanInstallmentList) {
        BigDecimal totalPaidAmount = BigDecimal.ZERO;
        for (LoanInstallment loanInstallment : loanInstallmentList) {
            if (Boolean.TRUE.equals(loanInstallment.getIsPaid())) {
                totalPaidAmount = totalPaidAmount.add(loanInstallment.getPaidAmount());
            }
        }
        return totalPaidAmount;

    }

}
