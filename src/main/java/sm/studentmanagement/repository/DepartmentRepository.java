package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentCode(String departmentCode);
}
