package finpik.resolver.loanproduct.resolver;

import static finpik.util.UserUtil.require;

import java.util.List;

import finpik.entity.User;
import finpik.resolver.loanproduct.application.CreateRelatedLoanProductUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.loanproduct.application.GetLoanProductUseCase;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RelatedLoanProductResult;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {
    private final GetLoanProductUseCase getLoanProductUseCase;
    private final CreateRelatedLoanProductUseCase createRelatedLoanProductUseCase;

    @Override
    @QueryMapping
    public List<RecommendedLoanProductResult> getLoanProducts(User userInput, @Argument Long profileId) {
        require(userInput);

        List<RecommendedLoanProductDto> recommendedLoanProductDtoList =
            getLoanProductUseCase.getRecommendedLoanProducts(profileId);

        return recommendedLoanProductDtoList.stream().map(RecommendedLoanProductResult::new).toList();
    }

    @Override
    @QueryMapping
    public LoanProductResult getLoanProduct(User userInput, Long loanProductId) {
        require(userInput);

        LoanProductDto loanProduct = getLoanProductUseCase.getLoanProduct(loanProductId);

        createRelatedLoanProductUseCase.createUserProductViewAsync(userInput.getId(), loanProductId);

        return new LoanProductResult(loanProduct);
    }

    @Override
    @QueryMapping
    public List<RelatedLoanProductResult> getRelatedLoanProductList(User userInput, Long loanProductId) {
        require(userInput);

        List<RelatedLoanProductDto> relatedLoanProductList = getLoanProductUseCase.getRelatedLoanProductList(loanProductId);

        return relatedLoanProductList.stream().map(RelatedLoanProductResult::new).toList();
    }
}
