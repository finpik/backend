package finpik.util;

import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import finpik.user.entity.User;

public class UserUtil {
    public static User require(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return user;
    }
}
