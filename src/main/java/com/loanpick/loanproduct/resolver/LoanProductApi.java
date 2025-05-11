package com.loanpick.loanproduct.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;

import com.loanpick.loanproduct.resolver.result.LoanProductResult;
import com.loanpick.loanproduct.resolver.result.RecommendedLoanProductResult;
import com.loanpick.user.entity.User;

public interface LoanProductApi {
    List<RecommendedLoanProductResult> getLoanProducts(@ContextValue("user") User userInput, @Argument Long profileId);

    LoanProductResult getLoanProduct(@ContextValue("user") User userInput, @Argument Long productId);
}
