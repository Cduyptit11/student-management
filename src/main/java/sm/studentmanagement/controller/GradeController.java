package sm.studentmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.GradeDto;
import sm.studentmanagement.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<ApiResponse<GradeDto.Response>> saveOrUpdateGrade(
            @Valid @RequestBody GradeDto.InputRequest request) {
        GradeDto.Response saved = gradeService.saveOrUpdateGrade(request);
        return ResponseEntity.ok(ApiResponse.ok("Lưu điểm sinh viên thành công", saved));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<List<GradeDto.Response>>> getGradesByStudent(
            @PathVariable Long studentId,
            @RequestParam(required = false) Integer semesterId) {
        return ResponseEntity.ok(ApiResponse.ok(gradeService.getGradesByStudent(studentId, semesterId)));
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<ApiResponse<List<GradeDto.Response>>> getGradesByClass(
            @PathVariable Long classId,
            @RequestParam(required = false) Long courseId,
            @RequestParam Integer semesterId) {
        return ResponseEntity.ok(ApiResponse.ok(gradeService.getGradesByClass(classId, courseId, semesterId)));
    }
}
