package sm.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.common.BadRequestException;
import sm.studentmanagement.common.ResourceNotFoundException;
import sm.studentmanagement.model.dto.GradeDto;
import sm.studentmanagement.model.entity.Course;
import sm.studentmanagement.model.entity.Grade;
import sm.studentmanagement.model.entity.Semester;
import sm.studentmanagement.model.entity.Student;
import sm.studentmanagement.repository.*;
import sm.studentmanagement.security.SecurityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final ClassRepository classRepository;

    @Transactional
    public GradeDto.Response saveOrUpdateGrade(GradeDto.InputRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên ID: " + request.getStudentId()));

        validateTeacherAccessToClass(student.getClassEntity().getId());

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + request.getCourseId()));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học kỳ ID: " + request.getSemesterId()));

        Grade grade = gradeRepository.findByStudentIdAndCourseIdAndSemesterId(
                request.getStudentId(), request.getCourseId(), request.getSemesterId()
        ).orElseGet(() -> Grade.builder()
                .student(student)
                .course(course)
                .semester(semester)
                .build());

        if (Boolean.TRUE.equals(grade.getIsLocked()) && !SecurityUtil.isAdmin()) {
            throw new BadRequestException("Bảng điểm của môn học này đã bị khóa, chỉ ADMIN mới có thể cập nhật!");
        }

        grade.setAttendanceScore(request.getAttendanceScore());
        grade.setAssignmentScore(request.getAssignmentScore());
        grade.setMidtermScore(request.getMidtermScore());
        grade.setFinalScore(request.getFinalScore());

        if (request.getLock() != null) {
            grade.setIsLocked(request.getLock());
        }

        // Tính toán tổng điểm linh hoạt theo tỷ trọng môn học
        calculateTotalScores(grade, course);

        return mapToResponse(gradeRepository.save(grade));
    }

    @Transactional(readOnly = true)
    public List<GradeDto.Response> getGradesByStudent(Long studentId, Integer semesterId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên ID: " + studentId));

        validateTeacherAccessToClass(student.getClassEntity().getId());

        List<Grade> grades = (semesterId != null)
                ? gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId)
                : gradeRepository.findByStudentId(studentId);

        return grades.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<GradeDto.Response> getGradesByClass(Long classId, Long courseId, Integer semesterId) {
        validateTeacherAccessToClass(classId);

        List<Grade> grades;
        if (courseId != null) {
            grades = gradeRepository.findByStudentClassEntityIdAndCourseIdAndSemesterId(classId, courseId, semesterId);
        } else {
            grades = gradeRepository.findByStudentClassEntityIdAndSemesterId(classId, semesterId);
        }

        return grades.stream().map(this::mapToResponse).toList();
    }

    private void calculateTotalScores(Grade grade, Course course) {
        BigDecimal att = grade.getAttendanceScore() != null ? grade.getAttendanceScore() : BigDecimal.ZERO;
        BigDecimal ass = grade.getAssignmentScore() != null ? grade.getAssignmentScore() : BigDecimal.ZERO;
        BigDecimal mid = grade.getMidtermScore() != null ? grade.getMidtermScore() : BigDecimal.ZERO;
        BigDecimal fin = grade.getFinalScore() != null ? grade.getFinalScore() : BigDecimal.ZERO;

        // Điểm tổng kết hệ 10 = (att * attW) + (ass * assW) + (mid * midW) + (fin * finW)
        BigDecimal total10 = att.multiply(course.getAttendanceWeight())
                .add(ass.multiply(course.getAssignmentWeight()))
                .add(mid.multiply(course.getMidtermWeight()))
                .add(fin.multiply(course.getFinalWeight()))
                .setScale(2, RoundingMode.HALF_UP);

        grade.setTotalScore10(total10);

        // Quy đổi sang hệ 4 và điểm chữ
        double score = total10.doubleValue();
        if (score >= 8.5) {
            grade.setTotalScore4(new BigDecimal("4.0"));
            grade.setLetterGrade("A");
        } else if (score >= 8.0) {
            grade.setTotalScore4(new BigDecimal("3.5"));
            grade.setLetterGrade("B+");
        } else if (score >= 7.0) {
            grade.setTotalScore4(new BigDecimal("3.0"));
            grade.setLetterGrade("B");
        } else if (score >= 6.5) {
            grade.setTotalScore4(new BigDecimal("2.5"));
            grade.setLetterGrade("C+");
        } else if (score >= 5.5) {
            grade.setTotalScore4(new BigDecimal("2.0"));
            grade.setLetterGrade("C");
        } else if (score >= 5.0) {
            grade.setTotalScore4(new BigDecimal("1.5"));
            grade.setLetterGrade("D+");
        } else if (score >= 4.0) {
            grade.setTotalScore4(new BigDecimal("1.0"));
            grade.setLetterGrade("D");
        } else {
            grade.setTotalScore4(new BigDecimal("0.0"));
            grade.setLetterGrade("F");
        }
    }

    private void validateTeacherAccessToClass(Long classId) {
        if (SecurityUtil.isAdmin()) return;

        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null || !classRepository.existsByIdAndTeacherId(classId, teacherId)) {
            throw new AccessDeniedException("Bạn không phụ trách lớp này!");
        }
    }

    private GradeDto.Response mapToResponse(Grade grade) {
        return GradeDto.Response.builder()
                .id(grade.getId())
                .studentId(grade.getStudent().getId())
                .studentCode(grade.getStudent().getStudentCode())
                .studentName(grade.getStudent().getFullName())
                .courseId(grade.getCourse().getId())
                .courseCode(grade.getCourse().getCourseCode())
                .courseName(grade.getCourse().getName())
                .credits(grade.getCourse().getCredits())
                .semesterId(grade.getSemester().getId())
                .semesterName(grade.getSemester().getName())
                .attendanceScore(grade.getAttendanceScore())
                .assignmentScore(grade.getAssignmentScore())
                .midtermScore(grade.getMidtermScore())
                .finalScore(grade.getFinalScore())
                .totalScore10(grade.getTotalScore10())
                .totalScore4(grade.getTotalScore4())
                .letterGrade(grade.getLetterGrade())
                .isLocked(grade.getIsLocked())
                .build();
    }
}
