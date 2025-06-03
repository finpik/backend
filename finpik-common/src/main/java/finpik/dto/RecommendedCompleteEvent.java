package finpik.dto;

import java.util.List;
import java.util.UUID;

//@formatter:off
public record RecommendedCompleteEvent(
    Long profileId,
    UUID eventId,
    List<RecommendedCompleteEventContent> contentList
) {
    public record RecommendedCompleteEventContent(
        Long loanProductId,
        String productName,
        Float minInterestRate,
        Float maxInterestRate,
        Integer loanLimitAmount
    ) {
        public static RecommendedCompleteEventContent of(
            Long loanProductId, String productName, Float minInterestRate,
            Float maxInterestRate, Integer loanLimitAmount
        ) {
            return new RecommendedCompleteEventContent(
                loanProductId,
                productName,
                minInterestRate,
                maxInterestRate,
                loanLimitAmount
            );
        }
    }

    public static RecommendedCompleteEvent of(UUID eventId, Long profileId, List<RecommendedCompleteEventContent> contentList) {
        return new RecommendedCompleteEvent(profileId, eventId, contentList);
    }
}
