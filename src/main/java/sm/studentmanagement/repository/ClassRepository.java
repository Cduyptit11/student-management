package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.ClassEntity;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByClassCode(String classCode);
    List<ClassEntity> findByTeacherId(Long teacherId);
    boolean existsByIdAndTeacherId(Long classId, Long teacherId);
    boolean existsByIdAndTeacherUserId(Long classId, Long userId);
}
