package finpik.loanproduct.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import finpik.error.enums.ErrorCode;
import finpik.loanproduct.entity.LoanProduct;
import finpik.loanproduct.entity.RecommendedLoanProduct;
import finpik.loanproduct.entity.RelatedLoanProduct;
import finpik.loanproduct.repository.LoanProductCacheRepository;
import finpik.loanproduct.repository.LoanProductRepository;
import finpik.loanproduct.repository.RelatedLoanProductRepository;

@ExtendWith(SpringExtension.class)
class LoanProductServiceImplTest {
    @InjectMocks
    private LoanProductServiceImpl loanProductService;

    @Mock
    private LoanProductRepository loanProductRepository;

    @Mock
    private LoanProductCacheRepository loanProductCacheRepository;

    @Mock
    private RelatedLoanProductRepository relatedLoanProductRepository;

    @DisplayName("id로 조회된 대출 상품이 있으면 이를 리턴한다.")
    @Test
    void getLoanProduct() {
        // given
        Long id = 1L;
        LoanProduct loanProduct = LoanProduct.builder().id(id).build();

        when(loanProductRepository.findByIdWithDescription(id)).thenReturn(Optional.of(loanProduct));

        // when
        LoanProduct domain = loanProductService.getLoanProduct(id);

        // then
        assertThat(domain.getId()).isEqualTo(id);
    }

    @DisplayName("id로 조회된 값이 없으면 NOT_FOUND_LOAN_PRODUCT에러가 발생한다.")
    @Test
    void getLoanProductWithNotFound() {
        // given
        Long id = 1L;

        when(loanProductRepository.findByIdWithDescription(id)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> loanProductService.getLoanProduct(id))
                .hasMessage(ErrorCode.NOT_FOUND_LOAN_PRODUCT.getMessage());
    }

    @DisplayName("profileId로 검색했을 때 추천 상품 목록이 존재하면 리턴한다.")
    @Test
    void getRecommendedLoanProducts() {
        // given
        Long profileId = 1L;
        RecommendedLoanProduct firstRecommendedProduct = RecommendedLoanProduct.builder().loanProductId(1L)
                .loanLimitAmount(1000).productName("first test product").minInterestRate(3.0f).maxInterestRate(5.0f)
                .build();

        RecommendedLoanProduct secondRecommendedProduct = RecommendedLoanProduct.builder().loanProductId(2L)
                .loanLimitAmount(2000).productName("second test product").minInterestRate(4.0f).maxInterestRate(6.0f)
                .build();

        when(loanProductCacheRepository.getRecommendations(profileId))
                .thenReturn(List.of(firstRecommendedProduct, secondRecommendedProduct));

        // when
        List<RecommendedLoanProduct> results = loanProductService.getRecommendedLoanProducts(profileId);

        // then
        assertAll(() -> assertThat(results.size()).isEqualTo(2),

                () -> assertThat(results.getFirst().getLoanProductId()).isEqualTo(1L),
                () -> assertThat(results.getFirst().getProductName()).isEqualTo("first test product"),
                () -> assertThat(results.getFirst().getLoanLimitAmount()).isEqualTo(1000),
                () -> assertThat(results.getFirst().getMinInterestRate()).isEqualTo(3.0f),
                () -> assertThat(results.getFirst().getMaxInterestRate()).isEqualTo(5.0f),

                () -> assertThat(results.get(1).getLoanProductId()).isEqualTo(2L),
                () -> assertThat(results.get(1).getProductName()).isEqualTo("second test product"),
                () -> assertThat(results.get(1).getLoanLimitAmount()).isEqualTo(2000),
                () -> assertThat(results.get(1).getMinInterestRate()).isEqualTo(4.0f),
                () -> assertThat(results.get(1).getMaxInterestRate()).isEqualTo(6.0f));
    }

    @DisplayName("연관된 상품이 존재할 경우 값을 리턴한다.")
    @Test
    void getRelatedLoanProductList() {
        // given
        Long productId = 1L;

        RelatedLoanProduct firstRelatedProduct = RelatedLoanProduct.builder().productName("first test product").build();

        RelatedLoanProduct secondRelatedProduct = RelatedLoanProduct.builder().productName("second test product")
                .build();

        when(relatedLoanProductRepository.getRelatedLoanProducts(productId))
                .thenReturn(List.of(firstRelatedProduct, secondRelatedProduct));

        // when
        List<RelatedLoanProduct> results = loanProductService.getRelatedLoanProductList(productId);

        // then
        assertAll(() -> assertThat(results.size()).isEqualTo(2),

                () -> assertThat(results.getFirst().getProductName()).isEqualTo("first test product"),
                () -> assertThat(results.get(1).getProductName()).isEqualTo("second test product"));
    }
}
