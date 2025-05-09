package com.loanpick.externalapi.recommend;

import com.loanpick.externalapi.recommend.request.ProfileRequest;
import com.loanpick.externalapi.recommend.result.RecommendLoanProductResult;

import java.util.List;

public interface RecommendLoanProductPort {
    List<RecommendLoanProductResult> getRecommendations(ProfileRequest profileRequest);
}
