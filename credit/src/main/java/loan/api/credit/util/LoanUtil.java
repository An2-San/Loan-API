package loan.api.credit.util;


import java.util.Arrays;
import java.util.EnumSet;

public class LoanUtil {

    public enum NUMBER_OF_INSTALLMENT {
        N6(6),
        N9(9),
        N12(12),
        N24(24);

        private final Integer value;

        NUMBER_OF_INSTALLMENT(Integer numberOfInstallment) {
            this.value = numberOfInstallment;
        }

        public Integer getValue() {
            return this.value;
        }
    }

    public enum EXCEPTION_MESSAGES {
        CUSTOMER_NOT_FOUND("Customer does not found"),
        LOAN_INVALID_CREDIT_LIMIT("Customer does not have enough limit to create loan"),
        LOAN_INVALID_INSTALLMENT_NUMBER("Invalid number of installments. Valid options :" + Arrays.toString(EnumSet.allOf(LoanUtil.NUMBER_OF_INSTALLMENT.class).stream().map(LoanUtil.NUMBER_OF_INSTALLMENT::getValue).toArray())),
        LOAN_INVALID_INTEREST_RATE("Invalid interest rate. Valid interval : 0.1 - 0.5"),
        LOAN_NOT_FOUND("Loan does not found");

        private final String exceptionMessage;

        EXCEPTION_MESSAGES(String exceptionMessage) {
            this.exceptionMessage = exceptionMessage;
        }

        public String getExceptionMessage() {
            return exceptionMessage;
        }
    }

}
