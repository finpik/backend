package finpik.resolver.loanproduct.application;

import finpik.resolver.loanproduct.application.dto.LoanProductDto;
import finpik.resolver.loanproduct.resolver.input.UpdateLoanProductAndPrerequisiteInput;

import java.util.List;

public interface UpdateLoanProductUseCase {
    List<LoanProductDto> execute(List<UpdateLoanProductAndPrerequisiteInput> inputList);
}
