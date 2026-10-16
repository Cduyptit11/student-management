package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Major;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findByMajorCode(String majorCode);
    List<Major> findByDepartmentId(Integer departmentId);
}
