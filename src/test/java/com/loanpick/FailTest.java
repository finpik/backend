package com.loanpick;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FailTest {

    @DisplayName("github actions 푸쉬 시 테스트 실패하면 푸쉬가 되지 않는 rule set이 제대로 적용되는지 확인하기 위한 코드")
    @Test
    void test() {
        //given

        //when
        int a = 1;
        //then
        Assertions.assertEquals(1, 2);
    }
}
