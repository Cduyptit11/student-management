package sm.studentmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.StudentDto;
import sm.studentmanagement.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentDto.Response>>> getStudents(
            @RequestParam(required = false) Long classId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudents(classId, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto.Response>> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDto.Response>> createStudent(
            @Valid @RequestBody StudentDto.CreateOrUpdateRequest request) {
        StudentDto.Response created = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo mới sinh viên thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDto.Response>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDto.CreateOrUpdateRequest request) {
        StudentDto.Response updated = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật sinh viên thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa sinh viên thành công", null));
    }
}
