package finpik.global.error.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.MergedField;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;

class BusinessExceptionHandlerTest {
    BusinessExceptionHandler handler = new BusinessExceptionHandler();

    @DisplayName("businessException 을_GraphQLError로_변환한다")
    @Test
    void handle() {
        // given
        BusinessException ex = new BusinessException(ErrorCode.TEST_EXCEPTION);

        Field field = Field.newField("dummyField").sourceLocation(new SourceLocation(1, 1)).build();
        MergedField mergedField = MergedField.newMergedField(field).build();

        GraphQLOutputType graphQLOutputType = GraphQLObjectType.newObject().name("dummyType").build();

        ExecutionStepInfo stepInfo = ExecutionStepInfo.newExecutionStepInfo().type(graphQLOutputType)
                .path(ResultPath.fromList(List.of("test"))).build();

        DataFetchingEnvironment env = DataFetchingEnvironmentImpl.newDataFetchingEnvironment().mergedField(mergedField)
                .executionStepInfo(stepInfo).build();

        // when
        GraphQLError error = handler.handle(ex, env);
        Map<String, Object> extensions = error.getExtensions();

        // then
        assertAll(() -> assertThat(error.getErrorType().toString()).isEqualTo("BUSINESS_EXCEPTION"),
                () -> assertThat(extensions.get("code")).isEqualTo("TEST_EXCEPTION"),
                () -> assertThat(extensions.get("httpStatusCode")).isEqualTo(400),
                () -> assertThat(extensions.get("httpStatusErrorCode")).isEqualTo("BAD_REQUEST"));
    }

    @DisplayName("지원하는 예외 타입이 BusinessException 이다.")
    @Test
    void supportedExceptionType() {
        // given
        // when
        // then
        assertThat(handler.supportedExceptionType()).isEqualTo(BusinessException.class);
    }
}
