package vo;

import finpik.vo.BankDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BankDetailsTest {
    @DisplayName("기본 생성 테스트")
    @Test
    void create() {
        //given
        String bankName = "신한은행";
        String bankPhoneNumber = "010-1234-5678";
        String loanAvailableTime = "09:00~18:00";
        String bankImgUr = "https://finpik.bank/img/bank.png";

        //when
        BankDetails bankDetails = new BankDetails(bankName, bankPhoneNumber, loanAvailableTime, bankImgUr);

        //then
        assertAll(
            () -> assertThat(bankDetails.bankName()).isEqualTo(bankName),
            () -> assertThat(bankDetails.bankImgUrl()).isEqualTo(bankImgUr),
            () -> assertThat(bankDetails.loanAvailableTime()).isEqualTo(loanAvailableTime),
            () -> assertThat(bankDetails.bankPhoneNumber()).isEqualTo(bankPhoneNumber)
        );
    }
}
