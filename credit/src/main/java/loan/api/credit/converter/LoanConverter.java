package loan.api.credit.converter;


import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dbEntity.LoanInstallment;
import loan.api.credit.model.dto.LoanRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanConverter implements Converter<LoanRequestDto, Loan> {

    @Override
    public Loan convert(LoanRequestDto source) {
        Loan loan = new Loan();
        ZonedDateTime currentDate = ZonedDateTime.now();
        loan.setCreateDate(currentDate);

        // Calculate total loan amount using interest rate
        BigDecimal totalLoanAmount = source.getCalculatedLoanAmountWithInterest();
        loan.setLoanAmount(totalLoanAmount);
        loan.setNumberOfInstallment(source.getNumberOfInstallments());
        loan.setIsPaid(false);
        loan.setCustomerId(source.getCustomerId());

        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        // Calculate single loan installment amount
        BigDecimal loanInstallmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(source.getNumberOfInstallments()),2,RoundingMode.HALF_UP);
        for (int i = 0; i < source.getNumberOfInstallments(); i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            loanInstallment.setIsPaid(false);
            loanInstallment.setLoan(loan);
            loanInstallment.setAmount(loanInstallmentAmount);
            // Get the next month's first day
            LocalDate today = LocalDate.now().plusMonths(i);
            TemporalAdjuster temporalAdjuster = TemporalAdjusters.firstDayOfNextMonth();
            LocalDate firstOfNextMonth = today.with(temporalAdjuster);
            ZonedDateTime firstOfNextMonthZonedDateTime = ZonedDateTime.of(firstOfNextMonth, LocalTime.of(23,59), ZoneId.systemDefault());
            loanInstallment.setDueDate(firstOfNextMonthZonedDateTime);
            loanInstallmentList.add(loanInstallment);
        }
        loan.setLoanInstallmentList(loanInstallmentList);
        return loan;
    }
}
