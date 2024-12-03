package loan.api.credit.service;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.CustomerDto;
import loan.api.credit.repository.CustomerRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final ConversionService conversionService;

    public CustomerService(CustomerRepository customerRepository, ConversionService conversionService) {
        this.customerRepository = customerRepository;
        this.conversionService = conversionService;
    }


    public void createCustomer(CustomerDto customerDto) {

        Customer customer = conversionService.convert(customerDto,Customer.class);
        assert customer != null;
        customerRepository.save(customer);

    }

}
