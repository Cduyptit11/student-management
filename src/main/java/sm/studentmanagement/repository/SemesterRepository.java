package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Semester;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Optional<Semester> findBySemesterCode(String semesterCode);
    Optional<Semester> findByIsActiveTrue();
}
