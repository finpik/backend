package finpik.resolver.loanproduct.application.impl;

import finpik.LoanProduct;
import finpik.repository.loanproduct.LoanProductRepository;
import finpik.resolver.loanproduct.application.UpdateLoanProductUseCase;
import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.resolver.input.UpdateLoanProductAndPrerequisiteInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateLoanProductUseCaseImpl implements UpdateLoanProductUseCase {
    private final LoanProductRepository loanProductRepository;

    @Override
    public List<LoanProductDto> execute(List<UpdateLoanProductAndPrerequisiteInput> inputList) {
        List<LoanProduct> loanProductList =
            loanProductRepository.findAllById(
                inputList.stream().map(
                    UpdateLoanProductAndPrerequisiteInput::loanProductId
                ).toList()
            );

        Map<Long, UpdateLoanProductAndPrerequisiteInput> inputMap = inputList.stream().collect(Collectors.toMap(
            UpdateLoanProductAndPrerequisiteInput::loanProductId,
            input -> input
        ));

        loanProductList.forEach(loanProduct -> {
            UpdateLoanProductAndPrerequisiteInput input = inputMap.get(loanProduct.getId());

            loanProduct.changeLoanProductBadgeList(input.loanProductBadgeList());
            loanProduct.changeLoanProductPrerequisite(input.loanPrerequisite());
        });

        return loanProductRepository.updateAll(loanProductList)
            .stream().map(LoanProductDto::new).toList();
    }
}
