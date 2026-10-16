package sm.studentmanagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AuthDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Tên đăng nhập không được để trống")
        private String username;

        @NotBlank(message = "Mật khẩu không được để trống")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        @Builder.Default
        private String tokenType = "Bearer";
        private Long userId;
        private String username;
        private Long teacherId;
        private List<String> roles;
    }
}
