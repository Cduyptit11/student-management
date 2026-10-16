package sm.studentmanagement.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class GradeDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputRequest {
        @NotNull(message = "ID sinh viên không được để trống")
        private Long studentId;

        @NotNull(message = "ID môn học không được để trống")
        private Long courseId;

        @NotNull(message = "ID học kỳ không được để trống")
        private Integer semesterId;

        @DecimalMin(value = "0.0", message = "Điểm phải >= 0.0")
        @DecimalMax(value = "10.0", message = "Điểm phải <= 10.0")
        private BigDecimal attendanceScore;

        @DecimalMin(value = "0.0", message = "Điểm phải >= 0.0")
        @DecimalMax(value = "10.0", message = "Điểm phải <= 10.0")
        private BigDecimal assignmentScore;

        @DecimalMin(value = "0.0", message = "Điểm phải >= 0.0")
        @DecimalMax(value = "10.0", message = "Điểm phải <= 10.0")
        private BigDecimal midtermScore;

        @DecimalMin(value = "0.0", message = "Điểm phải >= 0.0")
        @DecimalMax(value = "10.0", message = "Điểm phải <= 10.0")
        private BigDecimal finalScore;

        private Boolean lock;
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
        private Long courseId;
        private String courseCode;
        private String courseName;
        private Integer credits;
        private Integer semesterId;
        private String semesterName;

        private BigDecimal attendanceScore;
        private BigDecimal assignmentScore;
        private BigDecimal midtermScore;
        private BigDecimal finalScore;

        private BigDecimal totalScore10;
        private BigDecimal totalScore4;
        private String letterGrade;
        private Boolean isLocked;
    }
}
