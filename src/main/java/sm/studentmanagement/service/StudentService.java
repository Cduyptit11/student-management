package sm.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sm.studentmanagement.common.BadRequestException;
import sm.studentmanagement.common.ResourceNotFoundException;
import sm.studentmanagement.model.dto.StudentDto;
import sm.studentmanagement.model.entity.ClassEntity;
import sm.studentmanagement.model.entity.Student;
import sm.studentmanagement.model.enums.StudentStatus;
import sm.studentmanagement.repository.ClassRepository;
import sm.studentmanagement.repository.StudentRepository;
import sm.studentmanagement.security.SecurityUtil;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    @Transactional(readOnly = true)
    public Page<StudentDto.Response> getStudents(Long classId, Pageable pageable) {
        if (SecurityUtil.isAdmin()) {
            if (classId != null) {
                return studentRepository.findByClassEntityId(classId, pageable).map(this::mapToResponse);
            }
            return studentRepository.findAll(pageable).map(this::mapToResponse);
        }

        // Giáo viên: Chỉ lấy sinh viên các lớp mình chủ nhiệm
        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null) {
            return Page.empty();
        }

        List<Long> assignedClassIds = classRepository.findByTeacherId(teacherId)
                .stream()
                .map(ClassEntity::getId)
                .toList();

        if (assignedClassIds.isEmpty()) {
            return Page.empty();
        }

        if (classId != null) {
            if (!assignedClassIds.contains(classId)) {
                throw new AccessDeniedException("Bạn không được phân công chủ nhiệm lớp này!");
            }
            return studentRepository.findByClassEntityId(classId, pageable).map(this::mapToResponse);
        }

        return studentRepository.findByClassEntityIdIn(assignedClassIds, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public StudentDto.Response getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));

        validateTeacherAccessToClass(student.getClassEntity().getId());

        return mapToResponse(student);
    }

    @Transactional
    public StudentDto.Response createStudent(StudentDto.CreateOrUpdateRequest request) {
        validateTeacherAccessToClass(request.getClassId());

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new BadRequestException("Mã sinh viên đã tồn tại: " + request.getStudentCode());
        }

        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + request.getClassId()));

        Student student = Student.builder()
                .studentCode(request.getStudentCode().trim().toUpperCase())
                .fullName(request.getFullName().trim())
                .classEntity(classEntity)
                .dob(request.getDob())
                .gender(request.getGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(request.getStatus() != null ? request.getStatus() : StudentStatus.STUDYING)
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentDto.Response updateStudent(Long id, StudentDto.CreateOrUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));

        validateTeacherAccessToClass(student.getClassEntity().getId());
        validateTeacherAccessToClass(request.getClassId());

        if (!student.getStudentCode().equalsIgnoreCase(request.getStudentCode()) &&
                studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new BadRequestException("Mã sinh viên đã tồn tại: " + request.getStudentCode());
        }

        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp học với ID: " + request.getClassId()));

        student.setStudentCode(request.getStudentCode().trim().toUpperCase());
        student.setFullName(request.getFullName().trim());
        student.setClassEntity(classEntity);
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            student.setStatus(request.getStatus());
        }

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với ID: " + id));

        validateTeacherAccessToClass(student.getClassEntity().getId());
        studentRepository.delete(student);
    }

    public void validateTeacherAccessToClass(Long classId) {
        if (SecurityUtil.isAdmin()) return;

        Long teacherId = SecurityUtil.getCurrentTeacherId();
        if (teacherId == null || !classRepository.existsByIdAndTeacherId(classId, teacherId)) {
            throw new AccessDeniedException("Bạn không phụ trách lớp này!");
        }
    }

    public StudentDto.Response mapToResponse(Student student) {
        return StudentDto.Response.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .classId(student.getClassEntity().getId())
                .classCode(student.getClassEntity().getClassCode())
                .className(student.getClassEntity().getName())
                .dob(student.getDob())
                .gender(student.getGender())
                .email(student.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .status(student.getStatus())
                .build();
    }
}
