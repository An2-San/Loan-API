package loan.api.credit.service;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.CustomerRequestDto;
import loan.api.credit.repository.CustomerRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;

    public CustomerService(CustomerRepository customerRepository, ConversionService conversionService) {
        this.customerRepository = customerRepository;
        this.conversionService = conversionService;
    }


    public Customer createCustomer(CustomerRequestDto customerRequestDto) {

        Customer customer = conversionService.convert(customerRequestDto,Customer.class);
        assert customer != null;
        return customerRepository.save(customer);

    }

    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

}
