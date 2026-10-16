package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByStudentIdAndCourseIdAndSemesterId(Long studentId, Long courseId, Integer semesterId);
    List<Grade> findByStudentIdAndSemesterId(Long studentId, Integer semesterId);
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByStudentClassEntityIdAndSemesterId(Long classId, Integer semesterId);
    List<Grade> findByStudentClassEntityIdAndCourseIdAndSemesterId(Long classId, Long courseId, Integer semesterId);
}
