package finpik.resolver.loanproduct.resolver;

import static finpik.util.UserUtil.require;
import static finpik.util.Values.USER;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.loanproduct.application.LoanProductQueryService;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
import finpik.user.entity.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {
    private final LoanProductQueryService loanProductQueryService;

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

        return new LoanProductResult(loanProduct);
    }
}
