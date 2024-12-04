package loan.api.credit.security;

import loan.api.credit.model.dbEntity.Customer;
import loan.api.credit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {


    @Value("${security.admin.username}")
    private String ADMIN_USERNAME ;

    @Value("${security.admin.password}")
    private String ADMIN_PASSWORD ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Bonus 1 : While ADMIN users can operate for all customers, CUSTOMER role users can operate for themselves.
        if(ADMIN_USERNAME.equals(username)){
            return User
                    .withUsername(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .roles("ADMIN")
                    .build();
        }
        else {
           Optional<Customer> customer = customerRepository.findById(username);
           if(customer.isEmpty()){
               throw new UsernameNotFoundException("Username " + username + "not found");
           }
            return User
                    .withUsername(username)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .roles("CUSTOMER")
                    .build();
        }

    }
}
