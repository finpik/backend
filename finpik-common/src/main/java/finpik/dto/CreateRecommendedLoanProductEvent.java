package finpik.dto;

import java.util.List;
import java.util.UUID;

import static finpik.util.Preconditions.require;

public record CreateRecommendedLoanProductEvent(
    Long profileId,
    UUID eventId,
    List<CreateRecommendedLoanProductEventContent> contentList
) {
    public record CreateRecommendedLoanProductEventContent(
        Long loanProductId,
        String productName,
        String bankName,
        Float minInterestRate,
        Float maxInterestRate,
        Long loanLimitAmount,
        Float similarity
    ) {
        public static CreateRecommendedLoanProductEventContent of(
            Long loanProductId, String productName, Float minInterestRate,
            Float maxInterestRate, Long loanLimitAmount, String bankName,
            Float similarity
        ) {
            return new CreateRecommendedLoanProductEventContent(
                loanProductId,
                productName,
                bankName,
                minInterestRate,
                maxInterestRate,
                loanLimitAmount,
                similarity
            );
        }
    }

    public static CreateRecommendedLoanProductEvent from(
        Long profileId,
        List<CreateRecommendedLoanProductEventContent> contentList
    ) {
        UUID eventId = UUID.randomUUID();

        return new CreateRecommendedLoanProductEvent(profileId, eventId, contentList);
    }

    public void validateFields() {
        require(eventId, "eventId must not be null");
        require(contentList, "contentList must not be null");
    }
}
