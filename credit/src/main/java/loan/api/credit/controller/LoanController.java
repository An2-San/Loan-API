package loan.api.credit.controller;

import loan.api.credit.model.dto.*;
import loan.api.credit.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {


    @Autowired
    private LoanService loanService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody LoanRequestDto loanRequestDto) throws Exception {
        loanService.createLoan(loanRequestDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("list-loans")
    public ResponseEntity<List<LoanResponseDto>> listLoans(@RequestParam String customerId,
                                                        @RequestParam(required = false) Boolean isPaid,
                                                        @RequestParam(required = false) Integer numberOfInstallment)  {
        return ResponseEntity.ok(loanService.listLoans(customerId,isPaid,numberOfInstallment));
    }

    @GetMapping("list-loan-installments")
    public ResponseEntity<List<LoanInstallmentResponseDto>> listLoanInstallments(@RequestParam String loanId,
                                                                      @RequestParam(required = false) Boolean isPaid)  {
        return ResponseEntity.ok(loanService.listLoanInstallments(loanId,isPaid));
    }

    @PostMapping("pay-loan")
    public ResponseEntity<PayLoanResponseDto> payLoan(@RequestBody PayLoanRequestDto payLoanResponseDto)  {
        return ResponseEntity.ok(loanService.payLoan(payLoanResponseDto));
    }

}
