package finpik.loanproduct.vo;

public record InterestRate(Float maxInterestRate, Float minInterestRate) {
    public InterestRate {
        validateInterestRate(maxInterestRate, minInterestRate);
    }

    private void validateInterestRate(Float maxInterestRate, Float minInterestRate) {
        if (minInterestRate == null || maxInterestRate == null) {
            throw new IllegalArgumentException("Interest rate must not be null");
        }

        if (minInterestRate < 0) {
            throw new IllegalArgumentException("Interest rate must not be negative");
        }

        if (maxInterestRate < 0) {
            throw new IllegalArgumentException("Interest rate must not be negative");
        }

        if (maxInterestRate < minInterestRate) {
            throw new IllegalArgumentException("Interest rate must be between " + minInterestRate + " and " + maxInterestRate);
        }
    }
}
