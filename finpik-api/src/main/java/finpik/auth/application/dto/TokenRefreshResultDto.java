package finpik.auth.application.dto;

public record TokenRefreshResultDto(String newRefreshToken, String newAccessToken) {
}
