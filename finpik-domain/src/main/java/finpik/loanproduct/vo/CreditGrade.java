package finpik.loanproduct.vo;

public record CreditGrade(Integer maxCreditGrade, Integer minCreditGrade) {
    public CreditGrade {
        validateCreditGrade(maxCreditGrade, minCreditGrade);
    }

    private void validateCreditGrade(Integer maxCreditGrade, Integer minCreditGrade) {
        if (maxCreditGrade == null || minCreditGrade == null) {
            throw new IllegalArgumentException("CreditGrade cannot be null");
        }

        if (maxCreditGrade < 0) {
            throw new IllegalArgumentException("maxCreditGrade cannot be negative");
        }

        if (minCreditGrade < 0) {
            throw new IllegalArgumentException("minCreditGrade cannot be negative");
        }

        if (maxCreditGrade < minCreditGrade) {
            throw new IllegalArgumentException("Max credit grade must be greater than min credit grade");
        }
    }
}
