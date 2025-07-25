package finpik.util;

public class Preconditions {
    public static <T> T require(T value, String message) {
        if (value == null) throw new IllegalArgumentException(message);
        return value;
    }

    public static Integer requirePositive(Integer value, String message) {
        if (value == null || value < 0) throw new IllegalArgumentException(message);
        return value;
    }

    public static Long requirePositive(Long value, String message) {
        if (value == null || value < 0) throw new IllegalArgumentException(message);
        return value;
    }
}
