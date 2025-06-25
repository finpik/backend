package finpik.loanproduct.vo;

public record InterestRate(Float maxInterestRate, Float minInterestRate) {
    public InterestRate {
        validateMinInterestRate(minInterestRate);
        validateMaxInterestRate(maxInterestRate);
    }

    private void validateMinInterestRate(Float minInterestRate) {
        if (minInterestRate == null) return;

        if (minInterestRate < 0) {
            throw new IllegalArgumentException("Interest rate must not be negative");
        }
    }

    private void validateMaxInterestRate(Float maxInterestRate) {
        if (maxInterestRate == null) return;

        if (maxInterestRate < 0) {
            throw new IllegalArgumentException("maxInterestRate must not be negative");
        }
    }
}
