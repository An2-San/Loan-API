package loan.api.credit.controller;

import loan.api.credit.model.dbEntity.Loan;
import loan.api.credit.model.dto.*;
import loan.api.credit.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {


    @Autowired
    private LoanService loanService;

    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #loanRequestDto.customerId == authentication.name)")
    public ResponseEntity<Loan> create(@RequestBody LoanRequestDto loanRequestDto) {
        loanService.createLoan(loanRequestDto);
        return ResponseEntity.created(URI.create("create")).build();
    }

    @GetMapping("list-loans")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.name)")
    public ResponseEntity<List<LoanResponseDto>> listLoans(@RequestParam String customerId,
                                                        @RequestParam(required = false) Boolean isPaid,
                                                        @RequestParam(required = false) Integer numberOfInstallment)  {
        return ResponseEntity.ok(loanService.listLoans(customerId,isPaid,numberOfInstallment));
    }

    @GetMapping("list-loan-installments")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.name)")
    public ResponseEntity<List<LoanInstallmentResponseDto>> listLoanInstallments(@RequestParam String customerId ,@RequestParam String loanId,
                                                                      @RequestParam(required = false) Boolean isPaid)  {
        return ResponseEntity.ok(loanService.listLoanInstallments(loanId,isPaid));
    }

    @PostMapping("pay-loan")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #payLoanRequestDto.customerId == authentication.name)")
    public ResponseEntity<PayLoanResponseDto> payLoan(@RequestBody PayLoanRequestDto payLoanRequestDto)  {
        return ResponseEntity.ok(loanService.payLoan(payLoanRequestDto, ZonedDateTime.now()));
    }

}
