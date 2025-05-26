package finpik.global.error.handler.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

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

class BindExceptionHandlerTest {
    private final BindExceptionHandler handler = new BindExceptionHandler();

    @DisplayName("bindException을_GraphQLError로_변환한다")
    @Test
    void handle() {
        // given
        BindException ex = new BindException(new Object(), "target");
        ex.addError(new FieldError("target", "username", "must not be blank"));

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
        List<Map<String, String>> fieldErrors = (List<Map<String, String>>) extensions.get("fieldErrors");

        // then
        assertAll(() -> assertThat(error.getErrorType().toString()).isEqualTo("BIND_EXCEPTION"),
                () -> assertThat(extensions.get("code")).isEqualTo("BIND_EXCEPTION"),
                () -> assertThat(extensions.get("fieldErrors")).isInstanceOf(List.class), () -> assertThat(fieldErrors)
                        .containsExactlyInAnyOrder(Map.of("field", "username", "message", "must not be blank")));
    }

    @DisplayName("지원하는 예외 타입이 BindException이다.")
    @Test
    void supportedExceptionType() {
        // given
        // when
        // then
        assertThat(handler.supportedExceptionType()).isEqualTo(BindException.class);
    }
}
