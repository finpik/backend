package com.loanpick.profile.resolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpick.config.TestGraphQLContextConfig;
import com.loanpick.profile.entity.Profile;
import com.loanpick.profile.entity.enums.CreditGradeStatus;
import com.loanpick.profile.entity.enums.EmploymentForm;
import com.loanpick.profile.entity.enums.EmploymentStatus;
import com.loanpick.profile.entity.enums.LoanProductUsageStatus;
import com.loanpick.profile.entity.enums.PurposeOfLoan;
import com.loanpick.profile.service.ProfileService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Import(TestGraphQLContextConfig.class)
class ProfileResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @DisplayName("GraphQL createProfile 요청이 제대로 들어간다")
    @Test
    void createProfileGraphqlRequest() throws Exception {
        // given
        String query = """
                    mutation {
                      createProfile(input: {
                        employmentStatus: PUBLIC_SERVANT,
                        employmentForm: FULL_TIME,
                        purposeOfLoan: HOUSING,
                        desiredLoanAmount: 5000000,
                        loanProductUsageStatus: USING,
                        loanProductUsageCount: 2,
                        totalLoanUsageAmount: 10000000,
                        creditScore: 800,
                        creditGradeStatus: UPPER,
                        profileName: "프로필1",
                        profileColor: PINK_TWO
                      }) {
                        id
                        profileName
                        purposeOfLoan
                      }
                    }
                """;

        Profile mockProfile = Profile.builder().id(1L).employmentStatus(EmploymentStatus.PUBLIC_SERVANT)
                .employmentForm(EmploymentForm.FULL_TIME).purposeOfLoan(PurposeOfLoan.HOUSING)
                .desiredLoanAmount(5000000).loanProductUsageStatus(LoanProductUsageStatus.USING)
                .loanProductUsageCount(2).totalLoanUsageAmount(10000000).creditScore(800)
                .creditGradeStatus(CreditGradeStatus.UPPER).profileName("프로필1").seq(0).build();

        when(profileService.createProfile(any())).thenReturn(mockProfile);

        // when & then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of("query", query)))).andExpect(status().isOk())
                .andDo(print()).andExpect(jsonPath("$.data.createProfile.id").value(1L))
                .andExpect(jsonPath("$.data.createProfile.profileName").value("프로필1"))
                .andExpect(jsonPath("$.data.createProfile.purposeOfLoan").value("HOUSING"));
    }

    @DisplayName("GraphQL profileByUser 요청이 제대로 들어간다")
    @Test
    void profileByUserGraphqlRequest() throws Exception {
        // given
        String query = """
                    query {
                      profileByUser {
                        id
                        profileName
                        employmentStatus
                        purposeOfLoan
                        creditGradeStatus
                      }
                    }
                """;

        Profile profile1 = Profile.builder().id(1L).profileName("프로필1").employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .purposeOfLoan(PurposeOfLoan.HOUSING).creditGradeStatus(CreditGradeStatus.UPPER)
                .loanProductUsageCount(2).totalLoanUsageAmount(10000000).desiredLoanAmount(5000000).seq(0).build();

        Profile profile2 = Profile.builder().id(2L).profileName("프로필2").employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                .purposeOfLoan(PurposeOfLoan.LIVING_EXPENSES).creditGradeStatus(CreditGradeStatus.LOWER)
                .loanProductUsageCount(1).totalLoanUsageAmount(3000000).desiredLoanAmount(2000000).seq(1).build();

        when(profileService.getProfileListBy(any())).thenReturn(List.of(profile1, profile2));

        // when & then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of("query", query)))).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profileByUser[0].id").value(1))
                .andExpect(jsonPath("$.data.profileByUser[0].profileName").value("프로필1"))
                .andExpect(jsonPath("$.data.profileByUser[0].purposeOfLoan").value("HOUSING"))
                .andExpect(jsonPath("$.data.profileByUser[1].id").value(2))
                .andExpect(jsonPath("$.data.profileByUser[1].profileName").value("프로필2"))
                .andExpect(jsonPath("$.data.profileByUser[1].creditGradeStatus").value("LOWER"));
    }

    @DisplayName("GraphQL updateProfile 요청이 제대로 들어간다")
    @Test
    void updateProfileGraphqlRequest() throws Exception {
        // given
        String query = """
                mutation {
                  updateProfile(input: {
                    id: 1,
                    employmentStatus: EMPLOYEE,
                    workplaceName: "회사",
                    employmentForm: FULL_TIME,
                    income: 50000000,
                    employmentDate: "2020-01-01",
                    purposeOfLoan: HOUSING,
                    desiredLoanAmount: 10000000,
                    loanProductUsageStatus: USING,
                    loanProductUsageCount: 2,
                    totalLoanUsageAmount: 15000000,
                    creditScore: 800,
                    creditGradeStatus: UPPER,
                    profileName: "프로필수정"
                  }) {
                    id
                    profileName
                  }
                }
                """;

        Map<String, String> body = Map.of("query", query);
        String graphqlRequest = new ObjectMapper().writeValueAsString(body);

        Profile mockProfile = Profile.builder().id(1L).profileName("프로필수정")
                .employmentStatus(EmploymentStatus.SELF_EMPLOYED).build();

        when(profileService.updateProfile(any())).thenReturn(mockProfile);

        // when & then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).content(graphqlRequest)).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.updateProfile.id").value(1L))
                .andExpect(jsonPath("$.data.updateProfile.profileName").value("프로필수정"));
    }

    @DisplayName("GraphQL updateProfileSequence 요청이 제대로 들어간다")
    @Test
    void updateProfileSequenceGraphqlRequest() throws Exception {
        // given
        String graphqlRequest = """
                {
                  "query": "mutation { updateProfileSequence(input: [{ id: 1, seq: 0 }, { id: 2, seq: 1 }]) { id seq } }"
                }
                """;

        Profile profile1 = Profile.builder().id(1L).seq(0).build();
        Profile profile2 = Profile.builder().id(2L).seq(1).build();

        when(profileService.updateProfileSequence(any(), any())).thenReturn(List.of(profile1, profile2));

        // when & then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).content(graphqlRequest))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.updateProfileSequence[0].id").value(1))
                .andExpect(jsonPath("$.data.updateProfileSequence[0].seq").value(0))
                .andExpect(jsonPath("$.data.updateProfileSequence[1].id").value(2))
                .andExpect(jsonPath("$.data.updateProfileSequence[1].seq").value(1));
    }
}
