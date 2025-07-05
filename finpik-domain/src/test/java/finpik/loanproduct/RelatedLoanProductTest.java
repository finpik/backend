package finpik.loanproduct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RelatedLoanProductTest {
    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        //given
        Long productId = 1L;
        String productName = "test product";
        Float maxInterestRate = 10.0f;
        Float minInterestRate = 7.0f;
        Long maxLoanLimitAmount = 50000000L;

        //when
        RelatedLoanProduct relatedLoanProduct = RelatedLoanProduct.of(
            productId, productName, maxInterestRate, minInterestRate, maxLoanLimitAmount
        );

        //then
        assertAll(
            () -> assertThat(relatedLoanProduct.getProductId()).isEqualTo(productId),
            () -> assertThat(relatedLoanProduct.getProductName()).isEqualTo(productName),
            () -> assertThat(relatedLoanProduct.getMaxLoanLimitAmount()).isEqualTo(maxLoanLimitAmount)
        );
    }
}
