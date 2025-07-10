package finpik.vo;

public record BankDetails(
    String bankName,
    String bankPhoneNumber,
    String loanAvailableTime,
    String bankImgUrl
) {
}
