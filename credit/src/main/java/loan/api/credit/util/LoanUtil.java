package loan.api.credit.util;


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

        public Integer getValue(){
            return this.value;
        }
    }

}
