package entity.profile;

import static org.assertj.core.api.Assertions.assertThat;

import finpik.entity.Profile;
import finpik.entity.User;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.RegistrationType;
import finpik.entity.policy.ProfileCreationSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.profile.ProfileEntity;
import finpik.entity.user.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

class ProfileEntityTest {

    @DisplayName("Profile과 UserEntity로 ProfileEntity를 만들 수 있다.")
    @Test
    void from() {
        // given
        Profile profile = getDefaultProfile();
        UserEntity userEntity = UserEntity.builder().build();

        // when
        ProfileEntity result = ProfileEntity.from(profile, userEntity);

        // then
        assertThat(result).isInstanceOf(ProfileEntity.class);
    }

    @DisplayName("ProfileEntity로 Profile 도메인 객체를 만들 수 있다.")
    @Test
    void toDomain() {
        // given
        Profile profile = getDefaultProfile();
        UserEntity userEntity = UserEntity.builder().email("test@test.com").build();

        ProfileEntity entity = ProfileEntity.from(profile, userEntity);
        // when
        Profile result = entity.toDomain();

        // then
        assertThat(result).isInstanceOf(Profile.class);
    }

    private Profile getDefaultProfile() {
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            1L,
            100,
            100,
            100,
            1000,
            "profile name",
            0,
            CreditGradeStatus.EXCELLENT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY,
            1000,
            LocalDate.now(),
            LocalDate.now(),
            Occupation.OTHER,
            null,
            null,
            user,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        return Profile.withId(spec);
    }
}
