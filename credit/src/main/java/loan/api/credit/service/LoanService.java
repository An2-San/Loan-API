package loan.api.credit.service;

import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dto.LoanDto;
import loan.api.credit.repository.LoanRepository;
import loan.api.credit.validation.LoanValidationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanValidationService loanValidationService;
    private final ConversionService conversionService;
    private final LoanRepository loanRepository;

    public LoanService(LoanValidationService loanValidationService, ConversionService conversionService ,LoanRepository loanRepository) {
        this.conversionService = conversionService;
        this.loanValidationService = loanValidationService;
        this.loanRepository = loanRepository;
    }

    public void createLoan(LoanDto loanDto) {
        loanValidationService.validateLoanDto(loanDto);

        Loan loan = conversionService.convert(loanDto,Loan.class);
        assert loan != null;
        loanRepository.save(loan);

    }

}
