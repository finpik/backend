package entity;

import finpik.entity.Profile;
import finpik.entity.ProfileList;
import finpik.entity.User;
import finpik.entity.enums.CreditGradeStatus;
import finpik.entity.enums.Gender;
import finpik.entity.enums.LoanProductUsageStatus;
import finpik.entity.enums.Occupation;
import finpik.entity.enums.ProfileColor;
import finpik.entity.enums.PurposeOfLoan;
import finpik.entity.enums.RegistrationType;
import finpik.entity.policy.ProfileCreationSpec;
import finpik.error.enums.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProfileListTest {
    @DisplayName("프로필리스트에 프로필을 추가할 수 있고 추가되면 기존의 프로필의 순번도 조정된다.")
    @Test
    void addProfileTest() {
        //given
        Profile firstProfile = getDefaultProfile(1L);
        Profile secondProfile = getDefaultProfile(2L);
        Profile thirdProfile = getDefaultProfile(3L);
        Profile newProfile = getDefaultProfile(4L);

        ProfileList profileList = new ProfileList(List.of(firstProfile, secondProfile, thirdProfile));

        //when
        ProfileList addedProfileList = profileList.addProfile(newProfile);

        //then
        assertAll(
            () -> assertThat(addedProfileList.size()).isEqualTo(4),
            () -> assertThat(addedProfileList.getProfileList().getFirst().getSeq()).isEqualTo(0),
            () -> assertThat(addedProfileList.getProfileList().getLast().getSeq()).isEqualTo(3)
        );
    }

    @DisplayName("시작값에 따라 프로필 최대 개수를 넘는지 확인한 후 넘는다면 에러가 발생한다.")
    @Test
    void exceptionBalanceSequenceTest() {
        //given
        Profile firstProfile = getDefaultProfile(1L);
        Profile secondProfile = getDefaultProfile(2L);
        Profile thirdProfile = getDefaultProfile(3L);
        Profile fourthProfile = getDefaultProfile(4L);
        Profile newProfile = getDefaultProfile(5L);

        ProfileList profileList = new ProfileList(List.of(firstProfile, secondProfile, thirdProfile, fourthProfile));

        //when
        //then
        assertThatThrownBy(() -> profileList.addProfile(newProfile))
            .hasMessage(ErrorCode.EXCEEDING_PROFILE_COUNT_LIMIT.getMessage());
    }

    @DisplayName("프로필을 삭제하면 해당 id를 가진 프로필은 삭제되고 순번을 리밸런싱한다.")
    @Test
    void deleteProfileTest() {
        //given
        Profile firstProfile = getDefaultProfile(1L);
        Profile secondProfile = getDefaultProfile(2L);
        Profile thirdProfile = getDefaultProfile(3L);

        ProfileList profileList = new ProfileList(List.of(firstProfile, secondProfile, thirdProfile));

        //when
        ProfileList deletedProfileList = profileList.deleteProfile(1L);

        //then
        assertAll(
            () -> assertThat(deletedProfileList.size()).isEqualTo(2),
            () -> assertThat(deletedProfileList.getProfileList().getFirst().getSeq()).isEqualTo(0)
        );
    }

    private Profile getDefaultProfile(Long profileId) {
        User user = User.withId(
            1L, "test", "test@test.com", Gender.MALE,
            RegistrationType.KAKAO, LocalDateTime.now(), LocalDate.of(2025, 5, 25)
        );

        ProfileCreationSpec spec = ProfileCreationSpec.rebuild(
            profileId,
            100,
            100,
            100,
            1000,
            "profile name",
            0,
            CreditGradeStatus.EXCELLENT,
            LoanProductUsageStatus.NOT_USING,
            PurposeOfLoan.LOAN_REPAYMENT,
            ProfileColor.GRAY_TWO,
            1000,
            LocalDate.now(),
            LocalDate.now(),
            Occupation.OTHER,
            null,
            user,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        return Profile.withId(spec);
    }
}
