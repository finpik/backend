package finpik;

import finpik.dto.RecommendLoanProductProfileEvent;
import finpik.entity.enums.EmploymentForm;
import finpik.entity.enums.Gender;
import finpik.entity.enums.Occupation;
import finpik.producer.RecommendLoanProductProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerTest {
    @Autowired
    private RecommendLoanProductProducer recommendLoanProductProducer;

    @DisplayName("")
    @Test
    void test() {
        //given
        RecommendLoanProductProfileEvent event = RecommendLoanProductProfileEvent.builder()
            .profileId(1L)
            .desiredLimit(100000000)
            .creditScore(980)
            .occupation(Occupation.EMPLOYEE)
            .employmentForm(EmploymentForm.FULL_TIME)
            .gender(Gender.MALE)
            .age(30)
            .build();

        //when
        recommendLoanProductProducer.sendMessageForRecommendLoanProduct(event);
        //then
    }
}
