package finpik.dto;

import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RecommendLoanProductProfileEvent {
    Long profileId;
    UUID eventId;
    Integer desiredLimit;
    Integer creditScore;
    Occupation occupation;
    EmploymentForm employmentForm;
    Gender gender;
    Integer age;

    @Builder
    public RecommendLoanProductProfileEvent(
        Long profileId, Integer desiredLimit, Integer creditScore,
        Occupation occupation, EmploymentForm employmentForm,
        Integer age, Gender gender
    ) {
        this.eventId = UUID.randomUUID();
        this.profileId = profileId;
        this.desiredLimit = desiredLimit;
        this.creditScore = creditScore;
        this.occupation = occupation;
        this.employmentForm = employmentForm;
        this.gender = gender;
        this.age = age;
    }
}
