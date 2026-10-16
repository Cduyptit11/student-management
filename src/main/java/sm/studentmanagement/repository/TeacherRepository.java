package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);
    Optional<Teacher> findByTeacherCode(String teacherCode);
}
