import finpik.RecommendedLoanProduct;
import finpik.vo.InterestRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RecommendedLoanProductTest {

    @DisplayName("생성 테스트")
    @Test
    void ofRecommendedLoanProductTest() {
        //given
        Long profileId = 2L;
        Long loanProductId = 3L;
        String productName = "test product";
        InterestRate interestRate = new InterestRate(10.0f, 7.0f);
        Long maxLoanLimitAmount = 50000000L;

        //when
        RecommendedLoanProduct recommendedLoanProduct =
            RecommendedLoanProduct.of(
                profileId, loanProductId, productName,
                interestRate.maxInterestRate(), interestRate.minInterestRate(),
                maxLoanLimitAmount
            );

        //then
        assertAll(
            () -> assertThat(recommendedLoanProduct.getProfileId()).isEqualTo(profileId),
            () -> assertThat(recommendedLoanProduct.getLoanProductId()).isEqualTo(loanProductId),
            () -> assertThat(recommendedLoanProduct.getMaxLoanLimitAmount()).isEqualTo(maxLoanLimitAmount)
        );
    }
}
