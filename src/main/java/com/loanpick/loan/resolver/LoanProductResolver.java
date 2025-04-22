package com.loanpick.loan.resolver;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.loan.resolver.result.LoanProductResult;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {

  @Override
  @QueryMapping
  public LoanProductResult loanProducts() {
    return null;
  }
}
