package loan.api.credit.converter;

import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dto.LoanResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LoanResponseDtoConverter implements Converter<Loan, LoanResponseDto> {
    @Override
    public LoanResponseDto convert(Loan source) {
        LoanResponseDto loanResponseDto = new LoanResponseDto();
        loanResponseDto.setLoanId(source.getId());
        loanResponseDto.setLoanAmount(source.getLoanAmount());
        loanResponseDto.setIsPaid(source.getIsPaid());
        loanResponseDto.setCreateDate(source.getCreateDate());
        loanResponseDto.setNumberOfInstallments(source.getNumberOfInstallment());
        return loanResponseDto;
    }
}
