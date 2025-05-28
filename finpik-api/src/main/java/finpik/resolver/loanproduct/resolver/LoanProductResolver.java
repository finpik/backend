package finpik.resolver.loanproduct.resolver;

import static finpik.util.UserUtil.require;
import static finpik.util.Values.USER;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.dto.UserProductViewEvent;
import finpik.resolver.loanproduct.application.LoanProductQueryService;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RelatedLoanProductResult;
import finpik.user.entity.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {
    private final LoanProductQueryService loanProductQueryService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @QueryMapping
    public List<RecommendedLoanProductResult> getLoanProducts(@ContextValue(USER) User userInput,
            @Argument Long profileId) {
        require(userInput);

        List<RecommendedLoanProductDto> recommendedLoanProductDtoList = loanProductQueryService
                .getRecommendedLoanProducts(profileId);

        return recommendedLoanProductDtoList.stream().map(RecommendedLoanProductResult::new).toList();
    }

    @Override
    @QueryMapping
    public LoanProductResult getLoanProduct(User userInput, Long productId) {
        require(userInput);

        LoanProductDto loanProduct = loanProductQueryService.getLoanProduct(productId);

        // 유저가 본 상품에 대한 이벤트 발행
        userProductViewEvent(userInput.getId(), productId);

        return new LoanProductResult(loanProduct);
    }

    @Override
    @QueryMapping
    public List<RelatedLoanProductResult> getRelatedLoanProductList(User userInput, Long productId) {
        require(userInput);

        List<RelatedLoanProductDto> relatedLoanProductList = loanProductQueryService
                .getRelatedLoanProductList(productId);

        return relatedLoanProductList.stream().map(RelatedLoanProductResult::new).toList();
    }

    private void userProductViewEvent(Long userId, Long productId) {
        UserProductViewEvent userProductViewEvent = new UserProductViewEvent(userId, productId);
        eventPublisher.publishEvent(userProductViewEvent);
    }
}
