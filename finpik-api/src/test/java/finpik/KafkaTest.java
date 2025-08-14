//package finpik;
//
//import finpik.dto.RecommendLoanProductProfileEvent;
//import finpik.entity.Profile;
//import finpik.entity.profile.ProfileEntity;
//import finpik.jpa.repository.profile.ProfileEntityJpaRepository;
//import finpik.producer.RecommendLoanProductProducer;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//class KafkaTest {
//    @Autowired
//    private ProfileEntityJpaRepository profileEntityJpaRepository;
//
//    @Autowired
//    RecommendLoanProductProducer recommendLoanProductProducer;
//
//    @DisplayName("")
//    @Test
//    void test() {
//        //given
//        Profile profile = profileEntityJpaRepository.findById(48L).get().toDomain();
//
//        RecommendLoanProductProfileEvent event = RecommendLoanProductProfileEvent.builder()
//            .profileId(profile.getId())
//            .desiredLimit(profile.getDesiredLoanAmount())
//            .creditScore(profile.getCreditScore().creditScore())
//            .occupation(profile.getOccupationDetail().getOccupation())
//            .employmentForm(profile.getOccupationDetail().getEmploymentForm())
//            .gender(profile.getUser().getGender())
//            .age(profile.getUser().getAge())
//            .build();
//
//        recommendLoanProductProducer.sendMessageForRecommendLoanProduct(event);
//
//        //when
//
//        //then
//    }
//}
