package finpik.dto;

import java.util.UUID;

public record RecommendedCompleteEvent(Long profileId, UUID eventId) {
    public static RecommendedCompleteEvent of(Long profileId) {
        UUID eventId = UUID.randomUUID();

        return new RecommendedCompleteEvent(profileId, eventId);
    }
}
