package sm.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.studentmanagement.model.entity.Schedule;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByClassEntityId(Long classId);
    List<Schedule> findByClassEntityIdAndSemesterId(Long classId, Integer semesterId);
    List<Schedule> findByClassEntityIdInAndSemesterId(List<Long> classIds, Integer semesterId);
}
