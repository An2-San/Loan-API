package loan.api.credit.controller;

import loan.api.credit.model.dto.LoanDto;
import loan.api.credit.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {


    @Autowired
    private LoanService loanService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody LoanDto loanDto) throws Exception {
        loanService.createLoan(loanDto);
        return ResponseEntity.accepted().build();
    }

}
