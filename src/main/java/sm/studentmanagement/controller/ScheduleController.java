package sm.studentmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sm.studentmanagement.common.ApiResponse;
import sm.studentmanagement.model.dto.ScheduleDto;
import sm.studentmanagement.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/classes/{classId}")
    public ResponseEntity<ApiResponse<List<ScheduleDto.Response>>> getSchedulesByClass(
            @PathVariable Long classId,
            @RequestParam(required = false) Integer semesterId) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.getSchedulesByClass(classId, semesterId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleDto.Response>> createSchedule(
            @Valid @RequestBody ScheduleDto.CreateRequest request) {
        ScheduleDto.Response created = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo lịch học thành công", created));
    }
}
