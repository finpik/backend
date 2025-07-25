package finpik.resolver.loanproduct.resolver;

import java.util.List;

import finpik.entity.User;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;

import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RelatedLoanProductResult;

public interface LoanProductApi {
    List<RecommendedLoanProductResult> getLoanProducts(@ContextValue("user") User userInput, @Argument Long profileId);

    LoanProductResult getLoanProduct(@ContextValue("user") User userInput, @Argument Long loanProductId);

    List<RelatedLoanProductResult> getRelatedLoanProductList(@ContextValue("user") User userInput,
            @Argument Long loanProductId);
}
