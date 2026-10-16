package sm.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.ClassDto;
import sm.studentmanagement.service.ClassService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassDto.Response>>> getAccessibleClasses() {
        return ResponseEntity.ok(ApiResponse.ok(classService.getAccessibleClasses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassDto.Response>> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(classService.getClassById(id)));
    }
}
