package com.loanpick.loanproduct.resolver;

import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.loanproduct.resolver.result.LoanProductResult;
import com.loanpick.user.entity.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {

    @Override
    @QueryMapping
    public LoanProductResult getLoanProducts(@ContextValue("user") User userInput) {
        return null;
    }
}
