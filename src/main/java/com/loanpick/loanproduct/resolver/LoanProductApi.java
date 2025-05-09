package com.loanpick.loanproduct.resolver;

import org.springframework.graphql.data.method.annotation.ContextValue;

import com.loanpick.loanproduct.resolver.result.LoanProductResult;
import com.loanpick.user.entity.User;

public interface LoanProductApi {
    LoanProductResult getLoanProducts(@ContextValue("user") User userInput);
}
