package sm.studentmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentCode(String studentCode);
    boolean existsByStudentCode(String studentCode);

    List<Student> findByClassEntityId(Long classId);
    Page<Student> findByClassEntityId(Long classId, Pageable pageable);
    Page<Student> findByClassEntityIdIn(List<Long> classIds, Pageable pageable);

    boolean existsByIdAndClassEntityTeacherUserId(Long studentId, Long userId);
}
