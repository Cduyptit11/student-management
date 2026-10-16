package sm.studentmanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    public static Long getCurrentUserId() {
        CustomUserDetails user = getCurrentUserDetails();
        return user != null ? user.getId() : null;
    }

    public static Long getCurrentTeacherId() {
        CustomUserDetails user = getCurrentUserDetails();
        return user != null ? user.getTeacherId() : null;
    }

    public static boolean isAdmin() {
        CustomUserDetails user = getCurrentUserDetails();
        if (user == null) return false;
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
