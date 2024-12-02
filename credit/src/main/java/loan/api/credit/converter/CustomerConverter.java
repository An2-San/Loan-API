package loan.api.credit.converter;


import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.model.dto.CustomerDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter implements Converter<CustomerDto, Customer> {

    @Override
    public Customer convert(CustomerDto source) {
        Customer customer = new Customer();
        customer.setName(source.getName());
        customer.setSurname(source.getSurname());
        customer.setCreditLimit(source.getCreditLimit());
        return customer;
    }
}
