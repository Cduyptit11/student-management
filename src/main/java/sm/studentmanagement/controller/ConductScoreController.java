package sm.studentmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.ConductScoreDto;
import sm.studentmanagement.service.ConductScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conduct-scores")
@RequiredArgsConstructor
public class ConductScoreController {

    private final ConductScoreService conductScoreService;

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<ConductScoreDto.Response>> evaluateConductScore(
            @Valid @RequestBody ConductScoreDto.EvaluateRequest request) {
        ConductScoreDto.Response evaluated = conductScoreService.evaluateConductScore(request);
        return ResponseEntity.ok(ApiResponse.ok("Đánh giá điểm rèn luyện thành công", evaluated));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<List<ConductScoreDto.Response>>> getConductScoresByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(conductScoreService.getConductScoresByStudent(studentId)));
    }

    @GetMapping("/classes/{classId}")
    public ResponseEntity<ApiResponse<List<ConductScoreDto.Response>>> getConductScoresByClass(
            @PathVariable Long classId,
            @RequestParam Integer semesterId) {
        return ResponseEntity.ok(ApiResponse.ok(conductScoreService.getConductScoresByClass(classId, semesterId)));
    }
}
