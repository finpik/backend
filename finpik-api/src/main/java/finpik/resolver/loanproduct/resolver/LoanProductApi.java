package finpik.resolver.loanproduct.resolver;

import java.util.List;

import finpik.entity.User;
import finpik.entity.enums.SortDirection;
import finpik.resolver.loanproduct.resolver.input.UpdateLoanProductAndPrerequisiteInput;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResultList;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;

import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RelatedLoanProductResult;
import org.springframework.graphql.data.method.annotation.MutationMapping;

public interface LoanProductApi {
    RecommendedLoanProductResultList getLoanProductList(
        @ContextValue("user") User userInput,
        @Argument Long profileId,
        @Argument Integer page,
        @Argument Integer size,
        @Argument String sortProperty,
        @Argument SortDirection sortDirection
    );

    LoanProductResult getLoanProduct(@ContextValue("user") User userInput, @Argument Long loanProductId);

    List<RelatedLoanProductResult> getRelatedLoanProductList(@ContextValue("user") User userInput,
            @Argument Long loanProductId);

    @MutationMapping
    List<LoanProductResult> updateLoanProductBadgeAndPrerequisite(
        @Argument List<UpdateLoanProductAndPrerequisiteInput> inputList
    );
}
