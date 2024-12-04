package loan.api.credit.controller;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.CustomerRequestDto;
import loan.api.credit.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> create(@RequestBody CustomerRequestDto customerRequestDto) {
        Customer customer = customerService.createCustomer(customerRequestDto);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("list-customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> listCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }

}
