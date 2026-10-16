package sm.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.common.ResourceNotFoundException;
import sm.studentmanagement.model.dto.ScheduleDto;
import sm.studentmanagement.model.entity.ClassEntity;
import sm.studentmanagement.model.entity.Course;
import sm.studentmanagement.model.entity.Schedule;
import sm.studentmanagement.model.entity.Semester;
import sm.studentmanagement.repository.ClassRepository;
import sm.studentmanagement.repository.CourseRepository;
import sm.studentmanagement.repository.ScheduleRepository;
import sm.studentmanagement.repository.SemesterRepository;
import sm.studentmanagement.security.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClassRepository classRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;

    @Transactional(readOnly = true)
    public List<ScheduleDto.Response> getSchedulesByClass(Long classId, Integer semesterId) {
        validateTeacherAccessToClass(classId);

        List<Schedule> schedules;
        if (semesterId != null) {
            schedules = scheduleRepository.findByClassEntityIdAndSemesterId(classId, semesterId);
        } else {
            schedules = scheduleRepository.findByClassEntityId(classId);
        }

        return schedules.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public ScheduleDto.Response createSchedule(ScheduleDto.CreateRequest request) {
        validateTeacherAccessToClass(request.getClassId());

        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp ID: " + request.getClassId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + request.getCourseId()));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học kỳ ID: " + request.getSemesterId()));

        Schedule schedule = Schedule.builder()
                .classEntity(classEntity)
                .course(course)
                .semester(semester)
                .lecturerName(request.getLecturerName())
                .room(request.getRoom())
                .dayOfWeek(request.getDayOfWeek())
                .startPeriod(request.getStartPeriod())
                .periodCount(request.getPeriodCount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return mapToResponse(scheduleRepository.save(schedule));
    }

    private void validateTeacherAccessToClass(Long classId) {
        if (SecurityUtil.isAdmin()) return;

        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null || !classRepository.existsByIdAndTeacherId(classId, teacherId)) {
            throw new AccessDeniedException("Bạn không phụ trách lớp này!");
        }
    }

    private ScheduleDto.Response mapToResponse(Schedule schedule) {
        return ScheduleDto.Response.builder()
                .id(schedule.getId())
                .classId(schedule.getClassEntity().getId())
                .classCode(schedule.getClassEntity().getClassCode())
                .className(schedule.getClassEntity().getName())
                .courseId(schedule.getCourse().getId())
                .courseCode(schedule.getCourse().getCourseCode())
                .courseName(schedule.getCourse().getName())
                .semesterId(schedule.getSemester().getId())
                .semesterName(schedule.getSemester().getName())
                .lecturerName(schedule.getLecturerName())
                .room(schedule.getRoom())
                .dayOfWeek(schedule.getDayOfWeek())
                .startPeriod(schedule.getStartPeriod())
                .periodCount(schedule.getPeriodCount())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
