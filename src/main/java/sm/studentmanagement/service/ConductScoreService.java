package sm.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.common.ResourceNotFoundException;
import sm.studentmanagement.model.dto.ConductScoreDto;
import sm.studentmanagement.model.entity.ConductScore;
import sm.studentmanagement.model.entity.Semester;
import sm.studentmanagement.model.entity.Student;
import sm.studentmanagement.model.enums.ConductClassification;
import sm.studentmanagement.model.enums.ConductStatus;
import sm.studentmanagement.repository.ClassRepository;
import sm.studentmanagement.repository.ConductScoreRepository;
import sm.studentmanagement.repository.SemesterRepository;
import sm.studentmanagement.repository.StudentRepository;
import sm.studentmanagement.security.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductScoreService {

    private final ConductScoreRepository conductScoreRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    private final ClassRepository classRepository;

    @Transactional
    public ConductScoreDto.Response evaluateConductScore(ConductScoreDto.EvaluateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên ID: " + request.getStudentId()));

        validateTeacherAccessToClass(student.getClassEntity().getId());

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học kỳ ID: " + request.getSemesterId()));

        ConductScore conductScore = conductScoreRepository.findByStudentIdAndSemesterId(
                request.getStudentId(), request.getSemesterId()
        ).orElseGet(() -> ConductScore.builder()
                .student(student)
                .semester(semester)
                .build());

        conductScore.setScore(request.getScore());
        conductScore.setClassification(classifyScore(request.getScore()));
        conductScore.setTeacherNotes(request.getTeacherNotes());
        if (request.getStatus() != null) {
            conductScore.setStatus(request.getStatus());
        } else if (conductScore.getStatus() == null) {
            conductScore.setStatus(ConductStatus.DRAFT);
        }

        return mapToResponse(conductScoreRepository.save(conductScore));
    }

    @Transactional(readOnly = true)
    public List<ConductScoreDto.Response> getConductScoresByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên ID: " + studentId));

        validateTeacherAccessToClass(student.getClassEntity().getId());

        return conductScoreRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConductScoreDto.Response> getConductScoresByClass(Long classId, Integer semesterId) {
        validateTeacherAccessToClass(classId);

        return conductScoreRepository.findByStudentClassEntityIdAndSemesterId(classId, semesterId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ConductClassification classifyScore(int score) {
        if (score >= 90) return ConductClassification.XUAT_SAC;
        if (score >= 80) return ConductClassification.TOT;
        if (score >= 65) return ConductClassification.KHA;
        if (score >= 50) return ConductClassification.TRUNG_BINH;
        if (score >= 35) return ConductClassification.YEU;
        return ConductClassification.KEM;
    }

    private void validateTeacherAccessToClass(Long classId) {
        if (SecurityUtil.isAdmin()) return;

        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null || !classRepository.existsByIdAndTeacherId(classId, teacherId)) {
            throw new AccessDeniedException("Bạn không phụ trách lớp này!");
        }
    }

    private ConductScoreDto.Response mapToResponse(ConductScore score) {
        return ConductScoreDto.Response.builder()
                .id(score.getId())
                .studentId(score.getStudent().getId())
                .studentCode(score.getStudent().getStudentCode())
                .studentName(score.getStudent().getFullName())
                .semesterId(score.getSemester().getId())
                .semesterName(score.getSemester().getName())
                .score(score.getScore())
                .classification(score.getClassification())
                .teacherNotes(score.getTeacherNotes())
                .status(score.getStatus())
                .evaluatedAt(score.getEvaluatedAt())
                .build();
    }
}
