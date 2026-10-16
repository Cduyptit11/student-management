package sm.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.common.ResourceNotFoundException;
import sm.studentmanagement.model.dto.ClassDto;
import sm.studentmanagement.model.entity.ClassEntity;
import sm.studentmanagement.repository.ClassRepository;
import sm.studentmanagement.security.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;

    @Transactional(readOnly = true)
    public List<ClassDto.Response> getAccessibleClasses() {
        if (SecurityUtil.isAdmin()) {
            return classRepository.findAll().stream().map(this::mapToResponse).toList();
        }

        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null) {
            return List.of();
        }

        return classRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClassDto.Response getClassById(Long id) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + id));

        return mapToResponse(classEntity);
    }

    private ClassDto.Response mapToResponse(ClassEntity entity) {
        return ClassDto.Response.builder()
                .id(entity.getId())
                .classCode(entity.getClassCode())
                .name(entity.getName())
                .majorName(entity.getMajor() != null ? entity.getMajor().getName() : null)
                .teacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null)
                .teacherName(entity.getTeacher() != null ? entity.getTeacher().getFullName() : "Chưa phân công")
                .teacherCode(entity.getTeacher() != null ? entity.getTeacher().getTeacherCode() : null)
                .cohort(entity.getCohort())
                .totalStudents(entity.getStudents() != null ? entity.getStudents().size() : 0)
                .build();
    }
}
