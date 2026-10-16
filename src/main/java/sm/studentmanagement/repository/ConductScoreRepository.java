package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.ConductScore;

import java.util.List;
import java.util.Optional;

public interface ConductScoreRepository extends JpaRepository<ConductScore, Long> {
    Optional<ConductScore> findByStudentIdAndSemesterId(Long studentId, Integer semesterId);
    List<ConductScore> findByStudentId(Long studentId);
    List<ConductScore> findByStudentClassEntityIdAndSemesterId(Long classId, Integer semesterId);
}
