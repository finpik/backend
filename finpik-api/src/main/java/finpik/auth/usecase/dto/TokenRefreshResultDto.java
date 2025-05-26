package finpik.auth.usecase.dto;

public record TokenRefreshResultDto(String newRefreshToken, String newAccessToken) {
}
