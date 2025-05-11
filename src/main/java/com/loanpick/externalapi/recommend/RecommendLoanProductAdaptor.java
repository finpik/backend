package com.loanpick.externalapi.recommend;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.loanpick.externalapi.recommend.request.ProfileRequest;
import com.loanpick.externalapi.recommend.result.RecommendLoanProductResult;

@Component
public class RecommendLoanProductAdaptor implements RecommendLoanProductPort {
    @Qualifier("recommendWebClient")
    private final WebClient recommendWebClient;

    public RecommendLoanProductAdaptor(WebClient recommendWebClient) {
        this.recommendWebClient = recommendWebClient;
    }

    public List<RecommendLoanProductResult> getRecommendations(ProfileRequest profileRequest) {
        return recommendWebClient.post().uri("/recommend").bodyValue(profileRequest).retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecommendLoanProductResult>>() {
                }).block();
    }
}
