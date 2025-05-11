package com.loanpick.externalapi.recommend;

import java.util.List;

import com.loanpick.externalapi.recommend.request.ProfileRequest;
import com.loanpick.externalapi.recommend.result.RecommendLoanProductResult;

public interface RecommendLoanProductPort {
    List<RecommendLoanProductResult> getRecommendations(ProfileRequest profileRequest);
}
