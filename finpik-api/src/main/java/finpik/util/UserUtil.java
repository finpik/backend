package finpik.util;

import finpik.entity.User;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;

public class UserUtil {
    public static User require(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return user;
    }
}
