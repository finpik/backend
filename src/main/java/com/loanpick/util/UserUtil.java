package com.loanpick.util;

import com.loanpick.error.enums.ErrorCode;
import com.loanpick.error.exception.BusinessException;
import com.loanpick.user.entity.User;

public class UserUtil {
    public static User require(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        }
        return user;
    }
}
