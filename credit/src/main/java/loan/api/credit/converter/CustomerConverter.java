package loan.api.credit.converter;


import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.CustomerRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter implements Converter<CustomerRequestDto, Customer> {

    @Override
    public Customer convert(CustomerRequestDto source) {
        Customer customer = new Customer();
        customer.setName(source.getName());
        customer.setSurname(source.getSurname());
        customer.setCreditLimit(source.getCreditLimit());
        return customer;
    }
}
