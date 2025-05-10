package com.loanpick.loanproduct.resolver;

import static com.loanpick.util.UserUtil.require;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.loanpick.loanproduct.entity.LoanProduct;
import com.loanpick.loanproduct.resolver.result.LoanProductResult;
import com.loanpick.loanproduct.resolver.result.RecommendedLoanProductResult;
import com.loanpick.loanproduct.service.LoanProductService;
import com.loanpick.loanproduct.service.RecommendLoanProductService;
import com.loanpick.redis.service.dto.RecommendedLoanProductDtoList;
import com.loanpick.user.entity.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {
    private final RecommendLoanProductService recommendLoanProductService;
    private final LoanProductService loanProductService;

    @Override
    @QueryMapping
    public List<RecommendedLoanProductResult> getLoanProducts(@ContextValue("user") User userInput,
            @Argument Long profileId) {
        require(userInput);

        RecommendedLoanProductDtoList recommendedLoanProductDtoList = recommendLoanProductService
                .getRecommendedLoanProducts(profileId);

        return recommendedLoanProductDtoList.dtos().stream().map(RecommendedLoanProductResult::of).toList();
    }

    @Override
    @QueryMapping
    public LoanProductResult getLoanProduct(User userInput, Long productId) {
        require(userInput);

        LoanProduct loanProduct = loanProductService.getLoanProduct(productId);

        return new LoanProductResult(loanProduct);
    }
}
