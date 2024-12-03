package loan.api.credit.converter;

import loan.api.credit.model.dbEntity.LoanInstallment;
import loan.api.credit.model.dto.LoanInstallmentResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LoanInstallmentResponseDtoConverter implements Converter<LoanInstallment,LoanInstallmentResponseDto> {
    @Override
    public LoanInstallmentResponseDto convert(LoanInstallment source) {
        LoanInstallmentResponseDto loanInstallmentResponseDto = new LoanInstallmentResponseDto();
        loanInstallmentResponseDto.setAmount(source.getAmount());
        loanInstallmentResponseDto.setDueDate(source.getDueDate());
        loanInstallmentResponseDto.setPaidAmount(source.getPaidAmount());
        loanInstallmentResponseDto.setIsPaid(source.getIsPaid());
        loanInstallmentResponseDto.setPaymentDate(source.getPaymentDate());

        return loanInstallmentResponseDto;
    }
}
