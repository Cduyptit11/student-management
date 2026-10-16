package sm.studentmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.AuthDto;
import sm.studentmanagement.security.CustomUserDetails;
import sm.studentmanagement.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDto.LoginResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        AuthDto.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(ApiResponse.error("Chưa xác thực"));
        }
        Map<String, Object> data = Map.of(
                "userId", userDetails.getId(),
                "username", userDetails.getUsername(),
                "teacherId", userDetails.getTeacherId() != null ? userDetails.getTeacherId() : "N/A",
                "roles", userDetails.getAuthorities()
        );
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
