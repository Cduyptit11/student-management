package sm.studentmanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sm.studentmanagement.repository.ClassRepository;
import sm.studentmanagement.repository.StudentRepository;

@Service("securityService")
@RequiredArgsConstructor
public class CustomSecurityService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    public boolean isHomeroomTeacherOfClass(Authentication authentication, Long classId) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return false;
        }

        // Admin has universal access
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        if (userDetails.getTeacherId() == null || classId == null) {
            return false;
        }

        return classRepository.existsByIdAndTeacherId(classId, userDetails.getTeacherId());
    }

    public boolean isHomeroomTeacherOfStudent(Authentication authentication, Long studentId) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return false;
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return true;
        }

        if (userDetails.getTeacherId() == null || studentId == null) {
            return false;
        }

        return studentRepository.existsByIdAndClassEntityTeacherUserId(studentId, userDetails.getId());
    }
}
