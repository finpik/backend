package finpik.domain;

import finpik.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alarm {
    private final Long id;
    private final User user;
    private final String message;

    public static Alarm createNew(User user, String message) {
        return new Alarm(null, user, message);
    }

    public static Alarm rebuild(Long id, User user, String message) {
        return new Alarm(id, user, message);
    }
}
