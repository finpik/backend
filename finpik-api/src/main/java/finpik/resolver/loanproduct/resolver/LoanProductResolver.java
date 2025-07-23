package finpik.resolver.loanproduct.resolver;

import static finpik.util.UserUtil.require;

import java.util.List;

import finpik.entity.User;
import finpik.entity.enums.SortDirection;
import finpik.resolver.loanproduct.application.CreateRelatedLoanProductUseCase;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResultList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import finpik.resolver.loanproduct.application.GetLoanProductUseCase;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.application.dto.RecommendedLoanProductDto;
import finpik.resolver.loanproduct.application.dto.RelatedLoanProductDto;
import finpik.resolver.loanproduct.resolver.result.LoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RecommendedLoanProductResult;
import finpik.resolver.loanproduct.resolver.result.RelatedLoanProductResult;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoanProductResolver implements LoanProductApi {
    private final GetLoanProductUseCase getLoanProductUseCase;
    private final CreateRelatedLoanProductUseCase createRelatedLoanProductUseCase;

    @Override
    @QueryMapping
    public RecommendedLoanProductResultList getLoanProductList(
        User userInput,
        @Argument Long profileId,
        @Argument Integer page,
        @Argument Integer size,
        @Argument String sortProperty,
        @Argument SortDirection sortDirection
    ) {
        require(userInput);
        Sort sort = convertToSpringSort(sortProperty, sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<RecommendedLoanProductDto> recommendedLoanProductDtoList =
            getLoanProductUseCase.getRecommendedLoanProductList(profileId, pageable);

        return new RecommendedLoanProductResultList(
            recommendedLoanProductDtoList.stream().map(RecommendedLoanProductResult::new).toList(),
            recommendedLoanProductDtoList.hasNext(),
            page,
            size
        );
    }

    @Override
    @QueryMapping
    public LoanProductResult getLoanProduct(User userInput, @Argument Long loanProductId) {
        require(userInput);

        LoanProductDto loanProduct = getLoanProductUseCase.getLoanProduct(loanProductId);

        createRelatedLoanProductUseCase.createUserProductViewAsync(userInput.getId(), loanProductId);

        return new LoanProductResult(loanProduct);
    }

    @Override
    @QueryMapping
    public List<RelatedLoanProductResult> getRelatedLoanProductList(User userInput, @Argument Long loanProductId) {
        require(userInput);

        List<RelatedLoanProductDto> relatedLoanProductList = getLoanProductUseCase.getRelatedLoanProductList(loanProductId);

        return relatedLoanProductList.stream().map(RelatedLoanProductResult::new).toList();
    }

    public Sort convertToSpringSort(String sortProperty, SortDirection sortDirection) {
        if (sortProperty == null || sortProperty.isBlank()) {
            return Sort.unsorted();
        }

        Sort.Direction direction = (sortDirection == SortDirection.DESC)
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        return Sort.by(new Sort.Order(direction, sortProperty));
    }
}
