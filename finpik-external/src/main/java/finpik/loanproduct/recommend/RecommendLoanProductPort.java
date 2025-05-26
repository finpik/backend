package finpik.loanproduct.recommend;

import java.util.List;

import finpik.loanproduct.recommend.request.ProfileRequest;
import finpik.loanproduct.recommend.result.RecommendLoanProductResult;

public interface RecommendLoanProductPort {
    List<RecommendLoanProductResult> getRecommendations(ProfileRequest profileRequest);
}
