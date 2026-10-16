package sm.studentmanagement.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sm.studentmanagement.model.enums.ConductClassification;
import sm.studentmanagement.model.enums.ConductStatus;

import java.time.LocalDateTime;

public class ConductScoreDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluateRequest {
        @NotNull(message = "ID sinh viên không được để trống")
        private Long studentId;

        @NotNull(message = "ID học kỳ không được để trống")
        private Integer semesterId;

        @NotNull(message = "Điểm rèn luyện không được để trống")
        @Min(value = 0, message = "Điểm rèn luyện tối thiểu là 0")
        @Max(value = 100, message = "Điểm rèn luyện tối đa là 100")
        private Integer score;

        private String teacherNotes;
        private ConductStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long studentId;
        private String studentCode;
        private String studentName;
        private Integer semesterId;
        private String semesterName;
        private Integer score;
        private ConductClassification classification;
        private String teacherNotes;
        private ConductStatus status;
        private LocalDateTime evaluatedAt;
    }
}
